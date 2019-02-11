package com.xxyp.xxyp.map.location.beans;

import java.io.Serializable;

public class TNPUserCommonPosition implements Serializable {
    private static final long serialVersionUID = 1L;
    private String commonLocationId;
    private String userId;
    private String name;
    private String address;
    private String locationDetail;
    private String locationCoordinate;
    private String screenshotsUrl;
    private String createTime;
    private String updateTime;
    private String status;
    private String adcodes;

    public TNPUserCommonPosition() {
    }

    public String getCommonLocationId() {
        return this.commonLocationId;
    }

    public void setCommonLocationId(String commonLocationId) {
        this.commonLocationId = commonLocationId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationDetail() {
        return this.locationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

    public String getLocationCoordinate() {
        return this.locationCoordinate;
    }

    public void setLocationCoordinate(String locationCoordinate) {
        this.locationCoordinate = locationCoordinate;
    }

    public String getScreenshotsUrl() {
        return this.screenshotsUrl;
    }

    public void setScreenshotsUrl(String screenshotsUrl) {
        this.screenshotsUrl = screenshotsUrl;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdcodes() {
        return this.adcodes;
    }

    public void setAdcodes(String adcodes) {
        this.adcodes = adcodes;
    }
}
