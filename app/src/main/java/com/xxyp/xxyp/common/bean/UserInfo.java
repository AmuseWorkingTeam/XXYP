
package com.xxyp.xxyp.common.bean;

/**
 * Description : 用户实体类
 * Created by sunpengfei on 2017/7/27.
 * Person in charge : sunpengfei
 */
public class UserInfo extends BaseBean {

    private String email;

    private String mobile;

    private int status;

    private String userId;

    private int userIdentity;

    private String userImage;

    private String userIntroduction;

    private String userName;

    private int userSource;

    private String userSourceId;

    private int gender;

    private String address;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(int userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserIntroduction() {
        return userIntroduction;
    }

    public void setUserIntroduction(String userIntroduction) {
        this.userIntroduction = userIntroduction;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserSource() {
        return userSource;
    }

    public void setUserSource(int userSource) {
        this.userSource = userSource;
    }

    public String getUserSourceId() {
        return userSourceId;
    }

    public void setUserSourceId(String userSourceId) {
        this.userSourceId = userSourceId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
