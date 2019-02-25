
package com.xxyp.xxyp.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description : 聊天约拍 Created by sunpengfei on 2017/8/22. Person in charge :
 * sunpengfei
 */
@Entity
public class MessageShotEntity {
    /* 约拍id */
    @Id
    private Long shotId;

    private Long datingShotId;

    /* 发布约拍用户id */
    private String userId;

    /* 约拍地址 */
    private String datingShotAddress;

    /* 约拍时间 */
    private Long datingShotTime;

    /* 约拍目的 摄影师 模特 */
    private String purpose;

    /* 付款方式 */
    private String paymentMethod;

    /* 约拍信息 */
    private String datingShotIntroduction;

    /* 约拍描述 */
    private String description;

    /* 接受约拍userId */
    private String datingUserId;

    /* 约拍图片 */
    private String datingShotImage;

    /* 状态 */
    private Integer status;

    @NotNull
    private String belongTo;

    @Generated(hash = 649426365)
    public MessageShotEntity(Long shotId, Long datingShotId, String userId,
            String datingShotAddress, Long datingShotTime, String purpose,
            String paymentMethod, String datingShotIntroduction, String description,
            String datingUserId, String datingShotImage, Integer status,
            @NotNull String belongTo) {
        this.shotId = shotId;
        this.datingShotId = datingShotId;
        this.userId = userId;
        this.datingShotAddress = datingShotAddress;
        this.datingShotTime = datingShotTime;
        this.purpose = purpose;
        this.paymentMethod = paymentMethod;
        this.datingShotIntroduction = datingShotIntroduction;
        this.description = description;
        this.datingUserId = datingUserId;
        this.datingShotImage = datingShotImage;
        this.status = status;
        this.belongTo = belongTo;
    }

    @Generated(hash = 1461572445)
    public MessageShotEntity() {
    }

    public Long getShotId() {
        return this.shotId;
    }

    public void setShotId(Long shotId) {
        this.shotId = shotId;
    }

    public Long getDatingShotId() {
        return this.datingShotId;
    }

    public void setDatingShotId(Long datingShotId) {
        this.datingShotId = datingShotId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDatingShotAddress() {
        return this.datingShotAddress;
    }

    public void setDatingShotAddress(String datingShotAddress) {
        this.datingShotAddress = datingShotAddress;
    }

    public Long getDatingShotTime() {
        return this.datingShotTime;
    }

    public void setDatingShotTime(Long datingShotTime) {
        this.datingShotTime = datingShotTime;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDatingShotIntroduction() {
        return this.datingShotIntroduction;
    }

    public void setDatingShotIntroduction(String datingShotIntroduction) {
        this.datingShotIntroduction = datingShotIntroduction;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatingUserId() {
        return this.datingUserId;
    }

    public void setDatingUserId(String datingUserId) {
        this.datingUserId = datingUserId;
    }

    public String getDatingShotImage() {
        return this.datingShotImage;
    }

    public void setDatingShotImage(String datingShotImage) {
        this.datingShotImage = datingShotImage;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBelongTo() {
        return this.belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

}
