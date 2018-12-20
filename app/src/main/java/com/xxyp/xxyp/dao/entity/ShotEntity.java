
package com.xxyp.xxyp.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description : 我的约拍
 * Created by sunpengfei on 2017/8/11.
 * Person in charge : sunpengfei
 */
@Entity
public class ShotEntity {

    @Id
    private long id;

    /* 约拍id */
    private String datingShotId;

    /* 发布约拍用户id */
    private String userId;

    /* 约拍地址 */
    private String datingShotAddress;

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
    private String datingShotImages;

    private long releaseTime;

    /* 状态 */
    private int status;

    @Generated(hash = 1896408738)
    public ShotEntity(long id, String datingShotId, String userId,
            String datingShotAddress, String purpose, String paymentMethod,
            String datingShotIntroduction, String description, String datingUserId,
            String datingShotImages, long releaseTime, int status) {
        this.id = id;
        this.datingShotId = datingShotId;
        this.userId = userId;
        this.datingShotAddress = datingShotAddress;
        this.purpose = purpose;
        this.paymentMethod = paymentMethod;
        this.datingShotIntroduction = datingShotIntroduction;
        this.description = description;
        this.datingUserId = datingUserId;
        this.datingShotImages = datingShotImages;
        this.releaseTime = releaseTime;
        this.status = status;
    }

    @Generated(hash = 329070028)
    public ShotEntity() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDatingShotId() {
        return this.datingShotId;
    }

    public void setDatingShotId(String datingShotId) {
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

    public String getDatingShotImages() {
        return this.datingShotImages;
    }

    public void setDatingShotImages(String datingShotImages) {
        this.datingShotImages = datingShotImages;
    }

    public long getReleaseTime() {
        return this.releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
