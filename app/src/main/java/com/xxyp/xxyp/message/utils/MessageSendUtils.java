
package com.xxyp.xxyp.message.utils;

import android.text.TextUtils;

import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.MessageBodyBean;
import com.xxyp.xxyp.message.bean.MessageContentBean;
import com.xxyp.xxyp.message.bean.MessageImageBean;
import com.xxyp.xxyp.message.bean.MessageShotBean;
import com.xxyp.xxyp.message.bean.MessageVoiceBean;
import com.xxyp.xxyp.message.service.MsgSendModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description : 发送消息帮助类 Created by sunpengfei on 2017/8/16. Person in charge :
 * sunpengfei
 */
public class MessageSendUtils {

    private int mChatType;

    private String mMyUserId;

    private String mChatId;

    private MsgSendModel mSendModel;

    public MessageSendUtils() {
        mSendModel = MsgSendModel.getInstance();
    }

    public void setChatInfo(int chatType, String myUserId, String chatId) {
        mChatType = chatType;
        mMyUserId = myUserId;
        mChatId = chatId;
    }

    /**
     * 发送文本
     * 
     * @param text 文本内容
     * @return ChatMessageBean
     */
    public ChatMessageBean sendText(String text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        ChatMessageBean bean = getChatMessageBean();
        bean.getBodyBean().setText(text);
        bean.setMsgType(MessageConfig.MessageType.MSG_TEXT);
        return buildChatMessageBean(bean);
    }

    /**
     * 发送单张图片
     * 
     * @param localPath 图片路径
     * @return ChatMessageBean
     */
    public ChatMessageBean sendImage(String localPath) {
        if (TextUtils.isEmpty(localPath)) {
            return null;
        }
        ChatMessageBean bean = getChatMessageBean();
        MessageImageBean imageBean = new MessageImageBean();
        imageBean.setLocalImagePath(localPath);
        bean.setImageBean(imageBean);
        bean.setMsgType(MessageConfig.MessageType.MSG_IMAGE);
        buildChatMessageBean(bean);
        mSendModel.sendMessage(bean);
        return bean;
    }

    /**
     * 发送多张张图片
     * 
     * @param localPaths 图片路径
     * @return ChatMessageBean
     */
    public List<ChatMessageBean> sendImages(List<String> localPaths) {
        if (localPaths == null || localPaths.size() == 0) {
            return null;
        }
        List<ChatMessageBean> beans = new ArrayList<>();
        for (String path : localPaths) {
            ChatMessageBean bean = getChatMessageBean();
            MessageImageBean imageBean = new MessageImageBean();
            imageBean.setLocalImagePath(path);
            bean.setImageBean(imageBean);
            bean.setMsgType(MessageConfig.MessageType.MSG_IMAGE);
            buildChatMessageBean(bean);
            beans.add(bean);
        }
        Observable.from(beans).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChatMessageBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ChatMessageBean chatMessageBean) {
                        mSendModel.sendMessage(chatMessageBean);
                    }
                });
        return beans;
    }

    /**
     * 发送语音
     * 
     * @param voiceLocalPath 语音路径
     * @param voiceUrl 语音url
     * @param voiceTime 语音时间
     * @return ChatMessageBean
     */
    public ChatMessageBean sendVoice(String voiceLocalPath, String voiceUrl, int voiceTime) {
        if (TextUtils.isEmpty(voiceLocalPath)) {
            return null;
        }
        ChatMessageBean bean = getChatMessageBean();
        MessageVoiceBean voiceBean = new MessageVoiceBean();
        voiceBean.setVoiceLocalPath(voiceLocalPath);
        voiceBean.setVoiceLen(voiceTime);
        voiceBean.setVoiceUrl(voiceUrl);
        voiceBean.setStatus(MessageConfig.VoiceStatus.VOICE_READED);
        bean.setVoiceBean(voiceBean);
        bean.setMsgType(MessageConfig.MessageType.MSG_VOICE);
        return buildChatMessageBean(bean);
    }

    /**
     * 发送约拍
     * 
     * @param shotBean 约拍
     * @return ChatMessageBean
     */
    public ChatMessageBean sendShot(ShotBean shotBean) {
        if (shotBean == null || shotBean.getDatingShotImages() == null
                || shotBean.getDatingShotImages().isEmpty()) {
            return null;
        }
        ChatMessageBean bean = getChatMessageBean();
        MessageShotBean messageShotBean = new MessageShotBean();

        messageShotBean.setDatingShotId(Long.valueOf(shotBean.getDatingShotId()));
        messageShotBean.setUserId(shotBean.getUserId());
        messageShotBean.setDatingShotAddress(shotBean.getDatingShotAddress());
        messageShotBean.setDatingShotTime(shotBean.getReleaseTime());
        messageShotBean.setPurpose(shotBean.getPurpose());
        messageShotBean.setPaymentMethod(shotBean.getPaymentMethod());
        messageShotBean.setDatingShotIntroduction(shotBean.getDatingShotIntroduction());
        messageShotBean.setDescription(shotBean.getDescription());
        messageShotBean.setDatingUserId(shotBean.getDatingUserId());
        messageShotBean.setStatus(MessageConfig.ShotStatus.SHOT_CREATE);
        String url = shotBean.getDatingShotImages().get(0).getDatingShotPhoto();
        messageShotBean.setDatingShotImage(url);

        bean.setShotBean(messageShotBean);
        bean.setMsgType(MessageConfig.MessageType.MSG_APPOINTMENT);
        return buildChatMessageBean(bean);
    }

    private ChatMessageBean getChatMessageBean() {
        ChatMessageBean bean = new ChatMessageBean();
        bean.setBodyBean(new MessageBodyBean());
        bean.setContentBean(new MessageContentBean());
        return bean;
    }

    /**
     * 构造发送消息
     * 
     * @param bean 消息体
     * @return ChatMessageBean
     */
    private ChatMessageBean buildChatMessageBean(ChatMessageBean bean) {
        // 设置fromId为自己
        bean.setFromId(mMyUserId);
        // 设置发送者id为自己
        bean.setSendId(mMyUserId);
        bean.setToId(mChatId);
        bean.setChatId(mChatId);
        bean.setChatType(mChatType);
        bean.setSender(MessageConfig.MessageSender.MY_SEND);
        bean.setMessageTime(System.currentTimeMillis());
        bean.setMsgId(MessageUtils.buildMsgId());
        bean.setSendStatus(MessageConfig.MessageSendStatus.SEND_MSG_ING);
        handlerChatMessage(bean);
        return bean;
    }

    /**
     * 发送消息到model
     * 
     * @param bean 消息体
     */
    private void handlerChatMessage(ChatMessageBean bean) {
        switch (bean.getMsgType()) {
            // 图片发送单独处理
            case MessageConfig.MessageType.MSG_IMAGE:
                break;
            default:
                mSendModel.sendMessage(bean);
                break;
        }
    }

}
