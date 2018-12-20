
package com.xxyp.xxyp.message.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 消息第一层内容 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageContentBean extends BaseBean {

    private String content;

    private int contentType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }
}
