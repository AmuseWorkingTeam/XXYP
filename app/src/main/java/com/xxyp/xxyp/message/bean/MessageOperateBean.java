
package com.xxyp.xxyp.message.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 语音操作类型 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageOperateBean extends BaseBean {

    /* 操作者id */
    private String operateUserId;

    /* 操作消息的msgId */
    private String operateMsgId;

    /* 操作类型 */
    private int operateType;

    private String content;

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    public String getOperateMsgId() {
        return operateMsgId;
    }

    public void setOperateMsgId(String operateMsgId) {
        this.operateMsgId = operateMsgId;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
