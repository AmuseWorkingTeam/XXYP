
package com.xxyp.xxyp.message.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 消息实体bean Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageBean extends BaseBean {

    private String avimMsgId;

    private String conversationId;

    private String msgId;

    private int chatType;

    private String fromId;

    private String toId;

    private long messageTime;

    private String content;

    private MessageBodyBean mBodyBean;

    private MessageContentBean mContentBean;

    public String getAvimMsgId() {
        return avimMsgId;
    }

    public void setAvimMsgId(String avimMsgId) {
        this.avimMsgId = avimMsgId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageBodyBean getBodyBean() {
        return mBodyBean;
    }

    public void setBodyBean(MessageBodyBean bodyBean) {
        mBodyBean = bodyBean;
    }

    public MessageContentBean getContentBean() {
        return mContentBean;
    }

    public void setContentBean(MessageContentBean contentBean) {
        mContentBean = contentBean;
    }

    public void setMessageBean(MessageBean bean) {
        if (bean == null) {
            return;
        }
        setAvimMsgId(bean.getAvimMsgId());
        setConversationId(bean.getConversationId());
        setMsgId(bean.getMsgId());
        setChatType(bean.getChatType());
        setFromId(bean.getFromId());
        setToId(bean.getToId());
        setMessageTime(bean.getMessageTime());
        setContent(bean.getContent());
        setBodyBean(bean.getBodyBean());
        setContentBean(bean.getContentBean());
    }
}
