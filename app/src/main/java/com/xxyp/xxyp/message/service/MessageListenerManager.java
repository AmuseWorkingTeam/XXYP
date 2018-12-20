
package com.xxyp.xxyp.message.service;

import com.xxyp.xxyp.message.bean.ChatMessageBean;

import java.util.List;

/**
 * Description : 消息发送接收监听
 * Created by sunpengfei on 2017/8/14.
 * Person in charge : sunpengfei
 */
public interface MessageListenerManager {

    /**
     * 接收监听
     */
    interface OnMsgReceiveListener {

        /**
         * 收到单个消息
         * @param bean
         */
        void onReceiveMessage(ChatMessageBean bean);

        /**
         * 收到消息列表
         * @param beans
         */
        void onReceiveMessages(List<ChatMessageBean> beans);
    }

    /**
     * 发送监听
     */
    interface OnMsgSendListener {

        /**
         * 发送成功
         * 
         * @param chatId 会话id
         * @param chatType 会话类型
         * @param msgId 消息id
         * @param conversationId conversationId
         */
        void onSendSuccess(String chatId, int chatType, String msgId, String conversationId);

        /**
         * 发送失败
         *
         * @param chatId 会话id
         * @param chatType 会话类型
         * @param msgId 消息id
         * @param conversationId conversationId
         */
        void onSendFail(String chatId, int chatType,String msgId, String conversationId);

        /**
         * 发送进度
         *
         * @param chatId 会话id
         * @param chatType 会话类型
         * @param msgId 消息id
         */
        void onSendProgress(String chatId, int chatType,String msgId);
    }
}
