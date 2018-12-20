package com.xxyp.xxyp.message.service;

import android.text.TextUtils;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.message.bean.MessageBean;
import com.xxyp.xxyp.message.utils.MessageUtils;

import java.util.Arrays;

/**
 * Description : 消息服务
 * Created by sunpengfei on 2017/8/14.
 * Person in charge : sunpengfei
 */
public class MsgServiceManager {

    private String TAG = getClass().getName();
    
    private static MsgServiceManager mInstance;

    /**
     * LeanCloud
     */
    private AVIMClient mClient;

    /**
     * 是否连接
     */
    private boolean mIsConnect = false;

    public static MsgServiceManager getInstance() {
        if (mInstance == null) {
            synchronized (MsgServiceManager.class) {
                if (mInstance == null) {
                    mInstance = new MsgServiceManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 开启消息服务
     * @param userId  用户id
     */
    public void startMsgService(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        if (mClient == null) {
            mClient = AVIMClient.getInstance(userId);
        }
        mClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    mIsConnect = true;
                    AVIMMessageManager.registerDefaultMessageHandler(new MsgServiceHandler());
                    return;
                }
                XXLog.log_e(TAG, "start msg service failed " + e.getMessage());
            }
        });
    }

    /**
     * 关闭消息服务
     */
    public void closeMsgService() {
        if (mClient == null) {
            return;
        }
        mClient.close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    mIsConnect = false;
                    mClient = null;
                    return;
                }
                XXLog.log_e(TAG, "close msg service failed " + e.getMessage());
            }
        });
    }

    /**
     * 注册消息监听
     * @param onMsgReceiveListener  收消息监听
     * @param onMsgSendListener     发消息监听
     */
    public void registerIMListener(MessageListenerManager.OnMsgReceiveListener onMsgReceiveListener,
            MessageListenerManager.OnMsgSendListener onMsgSendListener)    {
        MsgReceiveModel.getInstance().registerReceiveListener(onMsgReceiveListener);
        MsgSendModel.getInstance().registerSendListener(onMsgSendListener);
    }

    /**
     * 取消消息监听
     * @param onMsgReceiveListener  收消息监听
     * @param onMsgSendListener     发消息监听
     */
    public void removeIMListener(MessageListenerManager.OnMsgReceiveListener onMsgReceiveListener,
            MessageListenerManager.OnMsgSendListener onMsgSendListener)    {
        MsgReceiveModel.getInstance().removeReceiveListener(onMsgReceiveListener);
        MsgSendModel.getInstance().removeSendListener(onMsgSendListener);
    }

    /**
     * 发送成功
     * 
     * @param chatId 会话id
     * @param chatType 会话类型
     * @param msgId 消息id
     * @param conversationId conversationId
     */
    private void handleSendSuccess(String chatId, int chatType, String msgId,
            String conversationId) {
        MsgSendModel.getInstance().onSendSuccess(chatId, chatType, msgId, conversationId);
    }

    /**
     * 发送失败
     * 
     * @param chatId 会话id
     * @param chatType 会话类型
     * @param msgId 消息id
     * @param conversationId conversationId
     */
    private void handleSendFail(String chatId, int chatType, String msgId, String conversationId) {
        MsgSendModel.getInstance().onSendFail(chatId, chatType, msgId, conversationId);
    }

    /**
     * 收到消息
     * @param messageBean  消息体
     */
    public void receiveMessage(MessageBean messageBean) {
        if (messageBean == null) {
            return;
        }
        MsgReceiveModel.getInstance().receiveMessage(messageBean);
    }


    /**
     * 发送消息
     * @param messageBean 消息体
     */
    public void sendMessage(final MessageBean messageBean) {
        if(messageBean == null){
            return;
        }
        XXLog.log_d(TAG, "send message content:" + messageBean.getContent());
        if (!mIsConnect || mClient == null) {
            XXLog.log_d(TAG, "send fail : msgService not connect:");
            startMsgService(SharePreferenceUtils.getInstance().getUserId());
            handleSendFail(messageBean.getToId(), messageBean.getChatType(), messageBean.getMsgId(),
                    messageBean.getConversationId());
            return;
        }
        if (!TextUtils.isEmpty(messageBean.getConversationId())) {
            AVIMConversation conversation = mClient
                    .getConversation(messageBean.getConversationId());
            if (conversation != null) {
                conversation.sendMessage(MessageUtils.buildPacketMessage(messageBean),
                        new AVIMConversationCallback() {
                            public void done(AVIMException e) {
                                if (e == null) {
                                    handleSendSuccess(messageBean.getToId(), messageBean.getChatType(), messageBean.getMsgId(),
                                            messageBean.getConversationId());
                                    return;
                                }
                                XXLog.log_d(TAG, "send fail : " + e.getMessage());
                                handleSendFail(messageBean.getToId(), messageBean.getChatType(), messageBean.getMsgId(),
                                        messageBean.getConversationId());
                            }
                        });
                return;
            }
        }
        mClient.createConversation(Arrays.asList(messageBean.getToId()), "", null,
                new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if (avimConversation != null
                                && !TextUtils.isEmpty(avimConversation.getConversationId())) {
                            messageBean.setConversationId(avimConversation.getConversationId());
                        }
                        if (e != null) {
                            XXLog.log_d(TAG, "send fail : " + e.getMessage());
                            handleSendFail(messageBean.getToId(), messageBean.getChatType(), messageBean.getMsgId(),
                                    messageBean.getConversationId());
                            return;
                        }
                        avimConversation.sendMessage(MessageUtils.buildPacketMessage(messageBean),
                                new AVIMConversationCallback() {
                                    @Override
                                    public void done(AVIMException e) {
                                        if (e == null) {
                                            handleSendSuccess(messageBean.getToId(), messageBean.getChatType(), messageBean.getMsgId(),
                                                    messageBean.getConversationId());
                                            return;
                                        }
                                        XXLog.log_d(TAG, "send fail : " + e.getMessage());
                                        handleSendFail(messageBean.getToId(), messageBean.getChatType(), messageBean.getMsgId(),
                                                messageBean.getConversationId());
                                    }
                                });
                    }
                });
    }

}
