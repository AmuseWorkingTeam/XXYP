
package com.xxyp.xxyp.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description : 单聊数据
 * Created by sunpengfei on 2017/8/22.
 * Person in charge : sunpengfei
 */
@Entity
public class ChatMsgEntity {

    @Id
    private Long id;
    
    @Index
    /* 消息id */
    private String msgId;

    /* 会话者的id */
    private String chatId;

    /* 发送者id */
    private String sendId;

    /* 消息类型 */
    private Integer msgType;

    /* 消息内容 */
    private String content;

    /* 时间 */
    private Long createTime;

    /* 发送状态 */
    private Integer sendStatus;

    /* 是否已读 */
    private Integer isRead;

    /* 发送者 自己或别人 */
    private Integer sender;

    /* 资源id */
    private Long relationSourceId;

    /* leanCloud消息id */
    private String avimMsgId;

    /* leanCloud会话id */
    private String conversationId;

    @Generated(hash = 1004457155)
    public ChatMsgEntity(Long id, String msgId, String chatId, String sendId,
            Integer msgType, String content, Long createTime, Integer sendStatus,
            Integer isRead, Integer sender, Long relationSourceId, String avimMsgId,
            String conversationId) {
        this.id = id;
        this.msgId = msgId;
        this.chatId = chatId;
        this.sendId = sendId;
        this.msgType = msgType;
        this.content = content;
        this.createTime = createTime;
        this.sendStatus = sendStatus;
        this.isRead = isRead;
        this.sender = sender;
        this.relationSourceId = relationSourceId;
        this.avimMsgId = avimMsgId;
        this.conversationId = conversationId;
    }

    @Generated(hash = 215499640)
    public ChatMsgEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getChatId() {
        return this.chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSendId() {
        return this.sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public Integer getMsgType() {
        return this.msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getSendStatus() {
        return this.sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getIsRead() {
        return this.isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Integer getSender() {
        return this.sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Long getRelationSourceId() {
        return this.relationSourceId;
    }

    public void setRelationSourceId(Long relationSourceId) {
        this.relationSourceId = relationSourceId;
    }

    public String getAvimMsgId() {
        return this.avimMsgId;
    }

    public void setAvimMsgId(String avimMsgId) {
        this.avimMsgId = avimMsgId;
    }

    public String getConversationId() {
        return this.conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

}
