
package com.xxyp.xxyp.message.bean;

/**
 * Description : 聊天消息实体 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class ChatMessageBean extends MessageBean {

    private int msgType;

    private String chatId;

    private String sendId;

    private int isRead;

    private long relationSourceId = -1;

    private MessageImageBean mImageBean;

    private MessageVoiceBean mVoiceBean;

    private MessageShotBean mShotBean;

    private int sendStatus;

    private int sender;

    public ChatMessageBean(){}

    public ChatMessageBean(MessageBean messageBean){
        setMessageBean(messageBean);
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public long getRelationSourceId() {
        return relationSourceId;
    }

    public void setRelationSourceId(long relationSourceId) {
        this.relationSourceId = relationSourceId;
    }

    public MessageImageBean getImageBean() {
        return mImageBean;
    }

    public void setImageBean(MessageImageBean imageBean) {
        mImageBean = imageBean;
    }

    public MessageVoiceBean getVoiceBean() {
        return mVoiceBean;
    }

    public void setVoiceBean(MessageVoiceBean voiceBean) {
        mVoiceBean = voiceBean;
    }

    public MessageShotBean getShotBean() {
        return mShotBean;
    }

    public void setShotBean(MessageShotBean shotBean) {
        mShotBean = shotBean;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public void setChatMessageBean(ChatMessageBean bean) {
        if (bean == null) {
            return;
        }
        setMsgType(bean.getMsgType());
        setChatId(bean.getChatId());
        setSendId(bean.getSendId());
        setIsRead(bean.getIsRead());
        setRelationSourceId(bean.getRelationSourceId());
        setImageBean(bean.getImageBean());
        setVoiceBean(bean.getVoiceBean());
        setShotBean(bean.getShotBean());
        setSendStatus(bean.getSendStatus());
        setSender(bean.getSender());
    }
}
