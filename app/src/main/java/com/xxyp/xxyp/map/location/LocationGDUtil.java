
package com.xxyp.xxyp.map.location;

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
import com.xxyp.xxyp.map.location.beans.GpsBean;
import com.xxyp.xxyp.map.location.interfaces.ILocationListener;
import com.xxyp.xxyp.map.location.interfaces.LocationChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description : 高德地图定位 (TODO 需要定位使用ToonLocationUtil)
 * Created by 程磊 on 2016/8/17.
 * Job number：139268
 * Phone ：13141390126
 * Email：chenglei@syswin.com
 * Person in charge : 程磊
 * Leader：李晓
 */
public class LocationGDUtil implements ILocationListener {

    private AMapLocationClientOption mLocationOption;

    private AMapLocationClient mLocationClient;

    public LocationGDUtil() {
    }

    /**
     * 设置定位监听
     *
     * @param context  上下文
     * @param listener 监听事件
     * @param time     定位间隔,单位毫秒,一般2000
     */
    public void setLocationListener(final Context context, final LocationChangeListener listener,
                                    final int time) {
        //初始化定位参数
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(context);
        }
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
        }
        //设置定位监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if ((0.0 == aMapLocation.getLatitude() && 0.0 == aMapLocation.getLongitude())
                            || (1.0 == aMapLocation.getLatitude()
                            && 1.0 == aMapLocation.getLongitude())) {
                        listener.mapLocation(null, ToonLocationUtil.LOCATION_FAIL);
                    } else if (TextUtils.isEmpty(aMapLocation.getAddress())) {
                        geocodeSearchLocation(context, aMapLocation.getLatitude(),
                                aMapLocation.getLongitude(), listener);
                    } else {
                        GpsBean bean = ToonLocationUtil.gcjToGps84(aMapLocation.getLatitude(),
                                aMapLocation.getLongitude());
                        bean.setAddress(aMapLocation.getAddress());//地址
                        bean.setPoiName(TextUtils.isEmpty(aMapLocation.getPoiName()) ? "" : aMapLocation.getPoiName());
                        bean.setAoiName(TextUtils.isEmpty(aMapLocation.getAoiName()) ? "" : aMapLocation.getAoiName());
                        bean.setCity(aMapLocation.getCity());//城市信息
                        bean.setMapLatitude(aMapLocation.getLatitude());
                        bean.setMapLongitude(aMapLocation.getLongitude());
                        bean.setAdCode(aMapLocation.getAdCode());//地区编码
                        bean.setCityCode(aMapLocation.getCityCode());//城市编码
                        bean.setProvince(aMapLocation.getProvince());
                        bean.setDistrict(aMapLocation.getDistrict());
                        listener.mapLocation(bean, ToonLocationUtil.LOCATION_SUCCESS);
                    }
                } else {
                    listener.mapLocation(null, ToonLocationUtil.LOCATION_FAIL);
                }
                if (time == -1){
                    mLocationClient.stopLocation();
                }
            }
        });

        mLocationOption.setOnceLocation(false);
        //设置定位间隔,单位毫秒 最小时间间隔 1000
        if (time > 1000){
            mLocationOption.setInterval((long) (time));
        }
        //设置定位模式为高精度模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        if (!mLocationClient.isStarted()) {
            mLocationClient.startLocation();
        }
    }

    /**
     * 逆地理编码回调
     */
    private void geocodeSearchLocation(Context context, final double latitude,
                                       final double longtitude, final LocationChangeListener listener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);// 传入context
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longtitude);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                if (rCode == 1000) {
                    if (result != null && result.getRegeocodeAddress() != null
                            && result.getRegeocodeAddress().getFormatAddress() != null) {
                        GpsBean bean = ToonLocationUtil.gcjToGps84(latitude, longtitude);
                        bean.setAddress(result.getRegeocodeAddress().getFormatAddress());
                        bean.setCity(result.getRegeocodeAddress().getCity());
                        bean.setMapLatitude(latitude);
                        bean.setMapLongitude(longtitude);
                        bean.setAdCode(result.getRegeocodeAddress().getAdCode());
                        bean.setCityCode(result.getRegeocodeAddress().getCityCode());
                        bean.setProvince(result.getRegeocodeAddress().getProvince());
                        bean.setDistrict(result.getRegeocodeAddress().getDistrict());
                        listener.mapLocation(bean, ToonLocationUtil.LOCATION_SUCCESS);
                    } else {
                        listener.mapLocation(null, ToonLocationUtil.LOCATION_FAIL);
                    }
                } else {
                    listener.mapLocation(null, ToonLocationUtil.LOCATION_FAIL);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
                // TODO Auto-generated method stub
                listener.mapLocation(null, ToonLocationUtil.LOCATION_FAIL);
            }

        });
        geocoderSearch.getFromLocationAsyn(query);
    }

    @Override
    public void startLocation(Context context, LocationChangeListener listener, int time) {
        setLocationListener(context, listener, time);
    }

    @Override
    public void cancelLocation() {
        stopLocation();
    }

    /**
     * 销毁定位
     */
    @SuppressWarnings("deprecation")
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
        mLocationOption = null;
    }
}
