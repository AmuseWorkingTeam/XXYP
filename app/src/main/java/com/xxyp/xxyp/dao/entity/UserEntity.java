
package com.xxyp.xxyp.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserEntity {
    @Id
    private Long id;

    @NotNull
    private String userId;

    private Integer userIdentity;

    private String userName;

    private String userIntroduction;

    private String userImage;

    private Integer userSource;

    private String userSourceId;

    private String email;

    private String mobile;

    private Integer status;

    private int gender;

    private String location;

    @Generated(hash = 616174801)
    public UserEntity(Long id, @NotNull String userId, Integer userIdentity,
            String userName, String userIntroduction, String userImage,
            Integer userSource, String userSourceId, String email, String mobile,
            Integer status, int gender, String location) {
        this.id = id;
        this.userId = userId;
        this.userIdentity = userIdentity;
        this.userName = userName;
        this.userIntroduction = userIntroduction;
        this.userImage = userImage;
        this.userSource = userSource;
        this.userSourceId = userSourceId;
        this.email = email;
        this.mobile = mobile;
        this.status = status;
        this.gender = gender;
        this.location = location;
    }

    @Generated(hash = 1433178141)
    public UserEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUserIdentity() {
        return this.userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIntroduction() {
        return this.userIntroduction;
    }

    public void setUserIntroduction(String userIntroduction) {
        this.userIntroduction = userIntroduction;
    }

    public String getUserImage() {
        return this.userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Integer getUserSource() {
        return this.userSource;
    }

    public void setUserSource(Integer userSource) {
        this.userSource = userSource;
    }

    public String getUserSourceId() {
        return this.userSourceId;
    }

    public void setUserSourceId(String userSourceId) {
        this.userSourceId = userSourceId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
