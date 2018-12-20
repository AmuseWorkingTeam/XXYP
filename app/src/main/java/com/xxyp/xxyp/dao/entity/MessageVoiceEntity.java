
package com.xxyp.xxyp.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description : 消息语音
 * Created by sunpengfei on 2017/8/22.
 * Person in charge : sunpengfei
 */
@Entity
public class MessageVoiceEntity {

    @Id
    private Long voiceId;

    /* 语音本地路径 */
    private String voiceLocalPath;

    /* 语音url */
    private String voiceUrl;

    /* 语音时长 */
    private Integer voiceLen;

    @NotNull
    private String belongTo;

    private Long lastModifyTime;

    private Integer status;

    @Generated(hash = 1486216304)
    public MessageVoiceEntity(Long voiceId, String voiceLocalPath, String voiceUrl,
            Integer voiceLen, @NotNull String belongTo, Long lastModifyTime,
            Integer status) {
        this.voiceId = voiceId;
        this.voiceLocalPath = voiceLocalPath;
        this.voiceUrl = voiceUrl;
        this.voiceLen = voiceLen;
        this.belongTo = belongTo;
        this.lastModifyTime = lastModifyTime;
        this.status = status;
    }

    @Generated(hash = 1595008266)
    public MessageVoiceEntity() {
    }

    public Long getVoiceId() {
        return this.voiceId;
    }

    public void setVoiceId(Long voiceId) {
        this.voiceId = voiceId;
    }

    public String getVoiceLocalPath() {
        return this.voiceLocalPath;
    }

    public void setVoiceLocalPath(String voiceLocalPath) {
        this.voiceLocalPath = voiceLocalPath;
    }

    public String getVoiceUrl() {
        return this.voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public Integer getVoiceLen() {
        return this.voiceLen;
    }

    public void setVoiceLen(Integer voiceLen) {
        this.voiceLen = voiceLen;
    }

    public String getBelongTo() {
        return this.belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public Long getLastModifyTime() {
        return this.lastModifyTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
