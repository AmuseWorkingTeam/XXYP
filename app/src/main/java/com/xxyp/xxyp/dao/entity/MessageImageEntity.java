
package com.xxyp.xxyp.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description : 聊天图片 Created by sunpengfei on 2017/8/22. Person in charge :
 * sunpengfei
 */
@Entity
public class MessageImageEntity {

    @Id
    private Long imgId;

    @NotNull
    private String belongTo;

    /* 原图本地路径 */
    private String localImagePath;

    /* 压缩后的图片路径 */
    private String bigImagePath;

    /* 图片url */
    private String imageUrl;

    /* 缩略图url */
    private String thumbImageUrl;

    /* 图片格式 */
    private String format;

    /* 图片宽 */
    private Integer imageHeight;

    /* 图片高 */
    private Integer imageWidth;

    /* 图片描述 */
    private String digest;

    private Long lastModifyTime;

    private Integer status;

    @Generated(hash = 431945912)
    public MessageImageEntity(Long imgId, @NotNull String belongTo,
            String localImagePath, String bigImagePath, String imageUrl,
            String thumbImageUrl, String format, Integer imageHeight,
            Integer imageWidth, String digest, Long lastModifyTime,
            Integer status) {
        this.imgId = imgId;
        this.belongTo = belongTo;
        this.localImagePath = localImagePath;
        this.bigImagePath = bigImagePath;
        this.imageUrl = imageUrl;
        this.thumbImageUrl = thumbImageUrl;
        this.format = format;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.digest = digest;
        this.lastModifyTime = lastModifyTime;
        this.status = status;
    }

    @Generated(hash = 516608281)
    public MessageImageEntity() {
    }

    public Long getImgId() {
        return this.imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    public String getBelongTo() {
        return this.belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public String getLocalImagePath() {
        return this.localImagePath;
    }

    public void setLocalImagePath(String localImagePath) {
        this.localImagePath = localImagePath;
    }

    public String getBigImagePath() {
        return this.bigImagePath;
    }

    public void setBigImagePath(String bigImagePath) {
        this.bigImagePath = bigImagePath;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbImageUrl() {
        return this.thumbImageUrl;
    }

    public void setThumbImageUrl(String thumbImageUrl) {
        this.thumbImageUrl = thumbImageUrl;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getImageHeight() {
        return this.imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Integer getImageWidth() {
        return this.imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getDigest() {
        return this.digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
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
