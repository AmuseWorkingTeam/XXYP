package com.xxyp.xxyp.common.utils.amap;

public class GpsBean {

    private double latitude;
    private double longitude;
    private String address;
    private String province;
    private String district;
    private String city;
    private double mapLatitude;
    private double mapLongitude;
    private String adCode;
    private String cityCode;
    private String poiName;
    private String aoiName;

    public String getAoiName() {
        return this.aoiName;
    }

    public void setAoiName(String aoiName) {
        this.aoiName = aoiName;
    }

    public String getPoiName() {
        return this.poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public double getMapLatitude() {
        return this.mapLatitude;
    }

    public void setMapLatitude(double mapLatitude) {
        this.mapLatitude = mapLatitude;
    }

    public double getMapLongitude() {
        return this.mapLongitude;
    }

    public void setMapLongitude(double mapLongitude) {
        this.mapLongitude = mapLongitude;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAdCode() {
        return this.adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public GpsBean(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public GpsBean() {
    }
}
