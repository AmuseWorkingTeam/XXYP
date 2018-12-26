package com.xxyp.xxyp.common.utils.amap;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 定位工具类
 */
public class LocationUtils implements ILocationListener {

    private AMapLocationClientOption mLocationOption;
    private AMapLocationClient mLocationClient;

    public LocationUtils() {
    }

    public void setLocationListener(final Context context, final LocationChangeListener listener, final int time) {
        if (this.mLocationClient == null) {
            this.mLocationClient = new AMapLocationClient(context);
        }

        if (this.mLocationOption == null) {
            this.mLocationOption = new AMapLocationClientOption();
        }

        this.mLocationClient.setLocationListener(new AMapLocationListener() {
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if ((0.0D != aMapLocation.getLatitude() || 0.0D != aMapLocation.getLongitude()) && (1.0D != aMapLocation.getLatitude() || 1.0D != aMapLocation.getLongitude())) {
                        if (TextUtils.isEmpty(aMapLocation.getAddress())) {
                            LocationUtils.this.geocodeSearchLocation(context, aMapLocation.getLatitude(), aMapLocation.getLongitude(), listener);
                        } else {
                            GpsBean bean = TransformLocationUtil.gcjToGps84(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                            bean.setAddress(aMapLocation.getAddress());
                            bean.setPoiName(TextUtils.isEmpty(aMapLocation.getPoiName()) ? "" : aMapLocation.getPoiName());
                            bean.setAoiName(TextUtils.isEmpty(aMapLocation.getAoiName()) ? "" : aMapLocation.getAoiName());
                            bean.setCity(aMapLocation.getCity());
                            bean.setMapLatitude(aMapLocation.getLatitude());
                            bean.setMapLongitude(aMapLocation.getLongitude());
                            bean.setAdCode(aMapLocation.getAdCode());
                            bean.setCityCode(aMapLocation.getCityCode());
                            bean.setProvince(aMapLocation.getProvince());
                            bean.setDistrict(aMapLocation.getDistrict());
                            LocationUtils.this.behaviorRecord(bean);
                            listener.mapLocation(bean, 0);
                        }
                    } else {
                        listener.mapLocation((GpsBean) null, 1);
                    }
                } else {
                    listener.mapLocation((GpsBean) null, 1);
                }

                if (time == -1) {
                    LocationUtils.this.mLocationClient.stopLocation();
                }

            }
        });
        this.mLocationOption.setOnceLocation(false);
        if (time > 1000) {
            this.mLocationOption.setInterval((long) time);
        }

        this.mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        this.mLocationClient.setLocationOption(this.mLocationOption);
        if (!this.mLocationClient.isStarted()) {
            this.mLocationClient.startLocation();
        }

    }

    private void geocodeSearchLocation(Context context, final double latitude, final double longtitude, final LocationChangeListener listener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longtitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200.0F, "autonavi");
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                if (rCode == 1000) {
                    if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
                        GpsBean bean = TransformLocationUtil.gcjToGps84(latitude, longtitude);
                        bean.setAddress(result.getRegeocodeAddress().getFormatAddress());
                        bean.setCity(result.getRegeocodeAddress().getCity());
                        bean.setMapLatitude(latitude);
                        bean.setMapLongitude(longtitude);
                        bean.setAdCode(result.getRegeocodeAddress().getAdCode());
                        bean.setCityCode(result.getRegeocodeAddress().getCityCode());
                        bean.setProvince(result.getRegeocodeAddress().getProvince());
                        bean.setDistrict(result.getRegeocodeAddress().getDistrict());
                        listener.mapLocation(bean, 0);
                    } else {
                        listener.mapLocation((GpsBean) null, 1);
                    }
                } else {
                    listener.mapLocation((GpsBean) null, 1);
                }

            }

            public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
                listener.mapLocation((GpsBean) null, 1);
            }
        });
        geocoderSearch.getFromLocationAsyn(query);
    }

    public void behaviorRecord(GpsBean bean) {
        JSONObject jsonObject = new JSONObject();
        String var3 = "LocationData";

        try {
            jsonObject.put("horizontal_coordinate", bean.getLatitude());
            jsonObject.put("vertical_coordinate", bean.getLongitude());
            jsonObject.put("province", bean.getProvince());
            jsonObject.put("city", bean.getCity());
            jsonObject.put("area", bean.getDistrict());
        } catch (JSONException var5) {
            var5.printStackTrace();
        }

    }

    public void startLocation(Context context, LocationChangeListener listener, int time) {
        this.setLocationListener(context, listener, time);
    }

    public void cancelLocation() {
        this.stopLocation();
    }

    public void stopLocation() {
        if (this.mLocationClient != null) {
            this.mLocationClient.stopLocation();
            this.mLocationClient.onDestroy();
        }

        this.mLocationClient = null;
        this.mLocationOption = null;
    }

}
