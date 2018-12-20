
package com.xxyp.xxyp.main.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

import java.util.List;

/**
 * Description : 约拍bean
 * Created by sunpengfei on 2017/8/10.
 * Person in charge : sunpengfei
 */
public class ShotBean extends BaseBean {

    /* 约拍id */
    private String datingShotId;

    /* 发布约拍用户id */
    private String userId;

    /* 用户名称 */
    private String userName;

    /* 用户头像 */
    private String userImage;

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
    private List<ShotPhotoBean> datingShotImages;

    private long releaseTime;

    /* 状态 */
    private int status;

    public String getDatingShotId() {
        return datingShotId;
    }

    public void setDatingShotId(String datingShotId) {
        this.datingShotId = datingShotId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDatingShotAddress() {
        return datingShotAddress;
    }

    public void setDatingShotAddress(String datingShotAddress) {
        this.datingShotAddress = datingShotAddress;
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

    public List<ShotPhotoBean> getDatingShotImages() {
        return datingShotImages;
    }

    public void setDatingShotImages(List<ShotPhotoBean> datingShotImages) {
        this.datingShotImages = datingShotImages;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
