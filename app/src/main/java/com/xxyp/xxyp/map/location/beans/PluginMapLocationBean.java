package com.xxyp.xxyp.map.location.beans;

import com.xxyp.xxyp.common.bean.BaseBean;

import java.io.Serializable;

public class PluginMapLocationBean extends BaseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mapLocationId;
    private String userObjId;
    private String userObjType;
    private double longitude;
    private double latitude;
    private String url;
    private String location;
    private String content;
    private String source;
    private int status;
    private String createTime;
    private String upStringTime;
    private String myPluginId;
    private String componentDataId;
    private String styleContent;
    private String msgId;
    private String city;
    private String adCode;

    public PluginMapLocationBean() {
    }

    public String getAdCode() {
        return this.adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMapLocationId() {
        return this.mapLocationId;
    }

    public void setMapLocationId(String mapLocationId) {
        this.mapLocationId = mapLocationId;
    }

    public String getUserObjId() {
        return this.userObjId;
    }

    public void setUserObjId(String userObjId) {
        this.userObjId = userObjId;
    }

    public String getUserObjType() {
        return this.userObjType;
    }

    public void setUserObjType(String userObjType) {
        this.userObjType = userObjType;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpStringTime() {
        return this.upStringTime;
    }

    public void setUpStringTime(String upStringTime) {
        this.upStringTime = upStringTime;
    }

    public String getMyPluginId() {
        return this.myPluginId;
    }

    public void setMyPluginId(String myPluginId) {
        this.myPluginId = myPluginId;
    }

    public String getComponentDataId() {
        return this.componentDataId;
    }

    public void setComponentDataId(String componentDataId) {
        this.componentDataId = componentDataId;
    }

    public String getStyleContent() {
        return this.styleContent;
    }

    public void setStyleContent(String styleContent) {
        this.styleContent = styleContent;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}