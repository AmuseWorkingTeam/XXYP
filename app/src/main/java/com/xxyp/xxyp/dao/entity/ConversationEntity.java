
package com.xxyp.xxyp.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description : 会话列表
 * Created by sunpengfei on 2017/8/24.
 * Person in charge : sunpengfei
 */
@Entity
public class ConversationEntity {

    @Id
    private Long id;

    @NotNull
    @Unique
    /* 会话者的id */
    private String chatId;

    @NotNull
    /* 会话类型 */
    private Integer chatType;

    /* 会话者头像 */
    private String conversationAvatar;

    /* 会话者名称 */
    private String conversationName;

    /* 消息描述 */
    private String msgDigest;

    /* 时间 */
    private Long createTime;

    /* 未读数 */
    private Integer unReadCount;

    /* leanCloud会话id */
    private String conversationId;

    @Generated(hash = 294054985)
    public ConversationEntity(Long id, @NotNull String chatId,
            @NotNull Integer chatType, String conversationAvatar,
            String conversationName, String msgDigest, Long createTime,
            Integer unReadCount, String conversationId) {
        this.id = id;
        this.chatId = chatId;
        this.chatType = chatType;
        this.conversationAvatar = conversationAvatar;
        this.conversationName = conversationName;
        this.msgDigest = msgDigest;
        this.createTime = createTime;
        this.unReadCount = unReadCount;
        this.conversationId = conversationId;
    }

    @Generated(hash = 2044044276)
    public ConversationEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChatId() {
        return this.chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Integer getChatType() {
        return this.chatType;
    }

    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }

    public String getConversationAvatar() {
        return this.conversationAvatar;
    }

    public void setConversationAvatar(String conversationAvatar) {
        this.conversationAvatar = conversationAvatar;
    }

    public String getConversationName() {
        return this.conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public String getMsgDigest() {
        return this.msgDigest;
    }

    public void setMsgDigest(String msgDigest) {
        this.msgDigest = msgDigest;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getUnReadCount() {
        return this.unReadCount;
    }

    public void setUnReadCount(Integer unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getConversationId() {
        return this.conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
