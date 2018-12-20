
package com.xxyp.xxyp.message.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 语音消息类型 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageVoiceBean extends BaseBean {

    private long voiceId;

    private String voiceLocalPath;

    private int voiceLen;

    private String voiceUrl;

    private String belongTo;

    private long lastModifyTime;

    private int status;

    public long getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(long voiceId) {
        this.voiceId = voiceId;
    }

    public String getVoiceLocalPath() {
        return voiceLocalPath;
    }

    public void setVoiceLocalPath(String voiceLocalPath) {
        this.voiceLocalPath = voiceLocalPath;
    }

    public int getVoiceLen() {
        return voiceLen;
    }

    public void setVoiceLen(int voiceLen) {
        this.voiceLen = voiceLen;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
