
package com.xxyp.xxyp.message.service;

import com.avos.avoscloud.im.v2.AVIMMessageField;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

/**
 * Description : 消息传输包
 * Created by sunpengfei on 2017/8/14.
 * Person in charge : sunpengfei
 */
@AVIMMessageType(type = 1)
public class PacketMessage extends AVIMTypedMessage {

    @AVIMMessageField(name = "catalog")
    private int catalog;

    @AVIMMessageField(name = "fromId")
    private String fromId;

    @AVIMMessageField(name = "message")
    private String message;

    @AVIMMessageField(name = "msgId")
    private String msgId;

    @AVIMMessageField(name = "msgTime")
    private String msgTime;

    @AVIMMessageField(name = "toId")
    private String toId;

    public int getCatalog() {
        return catalog;
    }

    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }
}
