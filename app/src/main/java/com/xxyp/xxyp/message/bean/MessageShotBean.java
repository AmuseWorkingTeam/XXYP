
package com.xxyp.xxyp.message.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 约拍类型的消息 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageShotBean extends BaseBean {

    /* 约拍id */
    private long datingShotId;

    /* 发布约拍用户id */
    private String userId;

    /* 约拍地址 */
    private String datingShotAddress;

    /* 约拍时间 */
    private long datingShotTime;

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
    private int status;

    private String belongTo;

    public long getDatingShotId() {
        return datingShotId;
    }

    public void setDatingShotId(long datingShotId) {
        this.datingShotId = datingShotId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDatingShotAddress() {
        return datingShotAddress;
    }

    public void setDatingShotAddress(String datingShotAddress) {
        this.datingShotAddress = datingShotAddress;
    }

    public long getDatingShotTime() {
        return datingShotTime;
    }

    public void setDatingShotTime(long datingShotTime) {
        this.datingShotTime = datingShotTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDatingShotIntroduction() {
        return datingShotIntroduction;
    }

    public void setDatingShotIntroduction(String datingShotIntroduction) {
        this.datingShotIntroduction = datingShotIntroduction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatingUserId() {
        return datingUserId;
    }

    public void setDatingUserId(String datingUserId) {
        this.datingUserId = datingUserId;
    }

    public String getDatingShotImage() {
        return datingShotImage;
    }

    public void setDatingShotImage(String datingShotImage) {
        this.datingShotImage = datingShotImage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }
}
