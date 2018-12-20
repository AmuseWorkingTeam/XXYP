
package com.xxyp.xxyp.message.service;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.GsonUtils;
import com.xxyp.xxyp.message.utils.MessageUtils;

/**
 * Description : LeanCloud收消息回调 Created by sunpengfei on 2017/8/15.
 * Leader：wangxiaohui
 */
public class MsgServiceHandler extends AVIMMessageHandler {

    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        if (message == null) {
            return;
        }
        PacketMessage packetMessage = GsonUtils.jsonToObject(message.getContent(),
                PacketMessage.class);
        if (packetMessage == null) {
            return;
        }
        XXLog.log_d("MsgServiceHandler", "receive msg : " + packetMessage.getMsgId());
        MsgServiceManager.getInstance().receiveMessage(MessageUtils.buildMessageBeanByPacket(
                packetMessage, message.getMessageId(), message.getConversationId()));
    }

}
