
package com.xxyp.xxyp.message.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 会话列表bean
 * Created by sunpengfei on 2017/8/24.
 * Person in charge : sunpengfei
 */
public class ConversationBean extends BaseBean {

    /* 会话者的id */
    private String chatId;

    /* 会话type */
    private int chatType;

    /* 会话者头像 */
    private String conversationAvatar;

    /* 会话者名称 */
    private String conversationName;

    /* 消息描述 */
    private String msgDigest;

    /* 时间 */
    private long createTime;

    /* 未读数 */
    private int unReadCount;

    /* leanCloud会话id */
    private String conversationId;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getConversationAvatar() {
        return conversationAvatar;
    }

    public void setConversationAvatar(String conversationAvatar) {
        this.conversationAvatar = conversationAvatar;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public String getMsgDigest() {
        return msgDigest;
    }

    public void setMsgDigest(String msgDigest) {
        this.msgDigest = msgDigest;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
