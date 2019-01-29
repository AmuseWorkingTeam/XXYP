
package com.xxyp.xxyp.message.service;

import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.ImageUtils;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuManager;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuUploadCallback;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.MessageImageBean;
import com.xxyp.xxyp.message.bean.MessageVoiceBean;
import com.xxyp.xxyp.message.model.ChatBaseModel;
import com.xxyp.xxyp.message.model.ChatSingleModel;
import com.xxyp.xxyp.message.model.MessageModel;
import com.xxyp.xxyp.message.utils.ChatUtils;
import com.xxyp.xxyp.message.utils.MessageConfig;
import com.xxyp.xxyp.message.utils.MessageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Description : 发送消息model Created by sunpengfei on 2017/8/14. Person in charge
 * : sunpengfei
 */
public class MsgSendModel {

    private static MsgSendModel mInstance;

    private Map<String, MessageListenerManager.OnMsgSendListener> sendMsgListeners = new HashMap<>();

    public static MsgSendModel getInstance() {
        if (mInstance == null) {
            synchronized (MsgSendModel.class) {
                if (mInstance == null)
                    mInstance = new MsgSendModel();
            }
        }
        return mInstance;
    }

    private MsgSendModel() {

    }

    /**
     * 发送消息
     * 
     * @param chatMessageBean 消息体
     */
    public void sendMessage(ChatMessageBean chatMessageBean) {
        if (chatMessageBean == null) {
            return;
        }
        MessageUtils.buildSendMessage(chatMessageBean);
        switch (chatMessageBean.getChatType()) {
            case MessageConfig.MessageCatalog.CHAT_SINGLE:
                new ChatSingleModel().addChatMessage(chatMessageBean);
                break;
            default:
                break;
        }
        // 获取会话leanCloud的conversationId
        MessageModel messageModel = new MessageModel();
        chatMessageBean.setConversationId(
                messageModel.getConversationIdByChatId(chatMessageBean.getChatId()));
        // 增加会话数据
        messageModel.addOrUpdateConversation(chatMessageBean);
        switch (chatMessageBean.getMsgType()) {
            // 语音 图片发送上传
            case MessageConfig.MessageType.MSG_VOICE:
            case MessageConfig.MessageType.MSG_IMAGE:
                handleUpload(chatMessageBean);
                break;
            default:
                MsgServiceManager.getInstance().sendMessage(chatMessageBean);
                break;
        }
    }

    /**
     * 上传消息
     * 
     * @param bean 消息体
     */
    private void handleUpload(final ChatMessageBean bean) {
        if (bean == null) {
            return;
        }
        try {
            switch (bean.getMsgType()) {
                case MessageConfig.MessageType.MSG_VOICE:
                    // 语音
                    final MessageVoiceBean voiceBean = bean.getVoiceBean();
                    if (voiceBean == null) {
                        return;
                    }
                    QiNiuManager.getInstance().uploadVoice(voiceBean.getVoiceLocalPath(),
                            new QiNiuUploadCallback() {
                                @Override
                                public void onProgress(int progress) {

                                }

                                @Override
                                public void onSuccess(String file) {
                                    handleVoiceFile(voiceBean, file);
                                    MessageUtils.buildSendMessage(bean);
                                    switch (bean.getChatType()) {
                                        case MessageConfig.MessageCatalog.CHAT_SINGLE:
                                            // 更新content
                                            new ChatSingleModel().updateMessageContent(
                                                    bean.getChatType(), bean.getMsgId(),
                                                    bean.getContent());
                                            // 更新语音数据表
                                            new ChatSingleModel()
                                                    .updateMessageVoice(bean.getVoiceBean());
                                            break;
                                        default:
                                            break;
                                    }
                                    // 上传成功发送消息
                                    MsgServiceManager.getInstance().sendMessage(bean);
                                }

                                @Override
                                public void onError(int errorCode, String msg) {
                                    MessageUtils.buildSendMessage(bean);
                                    switch (bean.getChatType()) {
                                        case MessageConfig.MessageCatalog.CHAT_SINGLE:
                                            new ChatSingleModel().updateMessageContent(
                                                    bean.getChatType(), bean.getContent(),
                                                    bean.getMsgId());
                                            break;
                                        default:
                                            break;
                                    }
                                    onSendFail(bean.getChatId(), bean.getChatType(),
                                            bean.getMsgId(), bean.getConversationId());
                                }
                            });
                    break;
                case MessageConfig.MessageType.MSG_IMAGE:
                    // 图片
                    final MessageImageBean imageBean = bean.getImageBean();
                    if (imageBean == null) {
                        return;
                    }
                    // 首先压缩图片
                    ChatUtils.getInstance().setImgChatInfo(bean)
                            .flatMap(new Func1<File, Observable<File>>() {
                                @Override
                                public Observable<File> call(File file) {
                                    // 设置图片宽高
                                    int[] imageSize = ChatUtils.getInstance()
                                            .getImageSize(file.getAbsolutePath());

                                    bean.getImageBean().setImageWidth(imageSize[0]);
                                    bean.getImageBean().setImageHeight(imageSize[1]);
                                    return Observable.just(file);
                                }
                            }).subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<File>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    MessageUtils.buildSendMessage(bean);
                                    switch (bean.getChatType()) {
                                        case MessageConfig.MessageCatalog.CHAT_SINGLE:
                                            new ChatSingleModel().updateMessageContent(
                                                    bean.getChatType(), bean.getContent(),
                                                    bean.getMsgId());
                                            // 更新图片数据表
                                            new ChatSingleModel()
                                                    .updateMessageImage(bean.getImageBean());
                                            break;
                                        default:
                                            break;
                                    }
                                    onSendFail(bean.getChatId(), bean.getChatType(),
                                            bean.getMsgId(), bean.getConversationId());
                                }

                                @Override
                                public void onNext(File file) {
                                    if (!file.exists()) {
                                        return;
                                    }
                                    final String imgPath = file.getAbsolutePath();
                                    bean.getImageBean().setBigImagePath(imgPath);
                                    // 上传
                                    QiNiuManager.getInstance().uploadImage(imgPath,
                                            new QiNiuUploadCallback() {
                                                @Override
                                                public void onProgress(int progress) {

                                                }

                                                @Override
                                                public void onSuccess(String file) {
                                                    handleImgFile(imageBean, imgPath, file);
                                                    MessageUtils.buildSendMessage(bean);
                                                    switch (bean.getChatType()) {
                                                        case MessageConfig.MessageCatalog.CHAT_SINGLE:
                                                            // 更新content
                                                            new ChatSingleModel()
                                                                    .updateMessageContent(
                                                                            bean.getChatType(),
                                                                            bean.getMsgId(),
                                                                            bean.getContent());
                                                            // 更新图片数据表
                                                            new ChatSingleModel()
                                                                    .updateMessageImage(
                                                                            bean.getImageBean());
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    // 上传成功发送消息
                                                    MsgServiceManager.getInstance()
                                                            .sendMessage(bean);
                                                }

                                                @Override
                                                public void onError(int errorCode, String msg) {
                                                    MessageUtils.buildSendMessage(bean);
                                                    switch (bean.getChatType()) {
                                                        case MessageConfig.MessageCatalog.CHAT_SINGLE:
                                                            new ChatSingleModel()
                                                                    .updateMessageContent(
                                                                            bean.getChatType(),
                                                                            bean.getContent(),
                                                                            bean.getMsgId());
                                                            // 更新图片数据表
                                                            new ChatSingleModel()
                                                                    .updateMessageImage(
                                                                            bean.getImageBean());
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    onSendFail(bean.getChatId(), bean.getChatType(),
                                                            bean.getMsgId(),
                                                            bean.getConversationId());
                                                }
                                            });
                                }
                            });
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            XXLog.log_e("database", "handleUpload is failed:" + e.getMessage());
            onSendFail(bean.getChatId(), bean.getChatType(), bean.getMsgId(),
                    bean.getConversationId());
        }
    }

    /**
     * 处理上传后图片
     *
     * @param voiceBean 语音
     * @param url 网络位置
     */
    private void handleVoiceFile(MessageVoiceBean voiceBean, String url) {
        if (voiceBean != null) {
            voiceBean.setVoiceUrl(url);
        }
    }

    /**
     * 处理上传后语音
     *
     * @param imgInfo 图片
     * @param filePath 本地路径
     * @param url 网络位置
     */
    private void handleImgFile(MessageImageBean imgInfo, String filePath, String url) {
        if (imgInfo != null) {
            imgInfo.setBigImagePath(filePath);
            imgInfo.setImageUrl(url);
            imgInfo.setThumbImageUrl(ImageUtils.getImgThumbUrl(url));
        }
    }

    /**
     * 注册发送消息监听
     * 
     * @param onMsgSendListener 发送监听
     */
    public void registerSendListener(MessageListenerManager.OnMsgSendListener onMsgSendListener) {
        if (onMsgSendListener != null) {
            sendMsgListeners.put(onMsgSendListener.getClass().getName(), onMsgSendListener);
        }
    }

    /**
     * 取消发送消息监听
     * 
     * @param onMsgSendListener 发送监听
     */
    public void removeSendListener(MessageListenerManager.OnMsgSendListener onMsgSendListener) {
        if (onMsgSendListener != null) {
            sendMsgListeners.remove(onMsgSendListener.getClass().getName());
        }
    }

    /**
     * 发送成功
     * 
     * @param chatId 会话id
     * @param chatType 会话类型
     * @param msgId 消息id
     * @param conversationId conversationId
     */
    public void onSendSuccess(String chatId, int chatType, String msgId, String conversationId) {
        // 更新消息发送成功状态
        ChatBaseModel model = null;
        switch (chatType) {
            case MessageConfig.MessageCatalog.CHAT_SINGLE:
                model = new ChatSingleModel();
                break;
            default:
                break;
        }
        // 修改会话的conversationId
        new MessageModel().updateConversationIdByChatId(chatId, conversationId);
        // 发送状态
        if (model != null) {
            model.updateMessageSendStatus(chatType,
                    MessageConfig.MessageSendStatus.SEND_MSG_SUCCESS, msgId);
        }
        for (MessageListenerManager.OnMsgSendListener onMsgSendListener : sendMsgListeners
                .values()) {
            onMsgSendListener.onSendSuccess(chatId, chatType, msgId, conversationId);
        }
    }

    /**
     * 发送失败
     * 
     * @param chatId 会话id
     * @param chatType 会话类型
     * @param msgId 消息id
     * @param conversationId conversationId
     */
    public void onSendFail(String chatId, int chatType, String msgId, String conversationId) {
        // 更新消息发送失败状态
        ChatBaseModel model = null;
        switch (chatType) {
            case MessageConfig.MessageCatalog.CHAT_SINGLE:
                model = new ChatSingleModel();
                break;
            default:
                break;
        }
        // 修改会话的conversationId
        new MessageModel().updateConversationIdByChatId(chatId, conversationId);
        // 发送状态
        if (model != null) {
            model.updateMessageSendStatus(chatType, MessageConfig.MessageSendStatus.SEND_NSG_FAIL,
                    msgId);
        }
        for (MessageListenerManager.OnMsgSendListener onMsgSendListener : sendMsgListeners
                .values()) {
            onMsgSendListener.onSendFail(chatId, chatType, msgId, conversationId);
        }
    }

}
