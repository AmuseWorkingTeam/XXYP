package com.xxyp.xxyp.map.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.AppContextUtils;
import com.xxyp.xxyp.map.config.CommonFilePathConfig;
import com.xxyp.xxyp.map.contract.MapControlContract;
import com.xxyp.xxyp.map.location.ToonLocationUtil;
import com.xxyp.xxyp.map.location.beans.GpsBean;
import com.xxyp.xxyp.map.location.beans.PluginMapLocationBean;
import com.xxyp.xxyp.map.location.beans.TNPUserCommonPosition;
import com.xxyp.xxyp.map.location.interfaces.LocationChangeListener;
import com.xxyp.xxyp.map.location.interfaces.LocationMapCallBack;
import com.xxyp.xxyp.map.utils.BitmapUtils;
import com.xxyp.xxyp.map.utils.NetworkUtils;
import com.xxyp.xxyp.map.utils.ThreadPool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Description : 地图选择功能
 * Created by 135033 on 2016/11/22.
 * Job number：135033
 * Phone ：18601413765
 * Email：wangyue@syswin.com
 * Person in charge : 135033
 * Leader：135033
 */

public class MapControlPresenter implements MapControlContract.MapControlPresenter {

    private MapControlContract.MapControlView mView;

    private HashMap<String, String> mCityMap;

    /**
     * 获取首次定位的城市信息
     */
    private String mFirstGetCity = "";
    private boolean isGetFirstCity = false;

    /**
     * 首次进入地图
     */
    private boolean mIsOpenedMap;

    /**
     * 当前经纬度信息
     */
    private double lat = 0, lon = 0;

    /**
     * 当前的城市；当前的城市编码；地址名称；接口要传入的位置地图及地图内容;搜索框中输入的内容
     */
    private String mCurrentCity, mCurrentAdCode, addressName, mLocation;

    private LocationMapCallBack mCallback;

    /**
     * 是否正在请求数据
     */
    private boolean getMapData = true;

    private ToonLocationUtil mToonLocationUtil;


    public MapControlPresenter(MapControlContract.MapControlView view, LocationMapCallBack callback) {

        mView = view;
        mCallback = callback;
        initCityMap();
    }

    @Override
    public void initCityMap() {


        if (mCityMap == null) {
            mCityMap = new HashMap<>();
        } else {
            mCityMap.clear();
        }

        Context context = mView.getContext();
        mCityMap.put(context.getString(R.string.beijing), context.getString(R.string.beijing));// 110000
        mCityMap.put(context.getString(R.string.tianjin), context.getString(R.string.tianjin));// 120000
        mCityMap.put(context.getString(R.string.shanghai), context.getString(R.string.shanghai));// 310000
        mCityMap.put(context.getString(R.string.chongqing), context.getString(R.string.chongqing));// 500000
    }

    @Override
    public void getLocation() {
        mView.showLoadingDialog(false);
        mToonLocationUtil = new ToonLocationUtil(mView.getContext(), new LocationChangeListener() {
            @Override
            public void mapLocation(GpsBean bean, int errorCode) {
                // 只有触发过这里后数据才会显示
                GpsBean gpsBean = new GpsBean();
                if (errorCode == ToonLocationUtil.LOCATION_SUCCESS) {
                    gpsBean = bean;
                } else {
                    gpsBean.setMapLatitude(39.996598);      //默认思源大厦的纬度
                    gpsBean.setMapLongitude(116.457803);    //默认思源大厦的经度
                    gpsBean.setCity("");
                    gpsBean.setAddress("");
                }

                if (mCallback != null) {
                    mCallback.onLocationChanged();
                }
                lat = gpsBean.getMapLatitude();
                lon = gpsBean.getMapLongitude();
                mCurrentCity = gpsBean.getCity();
                if (!isGetFirstCity && NetworkUtils.isNetworkAvailable(AppContextUtils.getAppContext())) {
                    mFirstGetCity = mCurrentCity;
                    isGetFirstCity = true;
                }
                mLocation = gpsBean.getAddress();
                mCurrentAdCode = gpsBean.getAdCode();
                mView.showMap(lat, lon, true);
                mToonLocationUtil.stopLocation();
                mView.dismissLoadingDialog();
            }
        }, ToonLocationUtil.DEFAULT_LOCATION_TIME);
    }

    @Override
    public boolean calculateError(CameraPosition position) {
        if (!mIsOpenedMap) {
            mIsOpenedMap = true;
            return true;
        }
        boolean change = position != null && position.target != null && (Math.abs(lat - position.target.latitude) > 0.0001f
                || Math.abs(lon - position.target.longitude) > 0.0001f);
        if (change && lat != 0 && lon != 0) {
            lat = position.target.latitude;
            lon = position.target.longitude;
        }
        return change;
    }

    @Override
    public void searchNear(boolean searchState, String searchText, int page) {

        if (!NetworkUtils.isNetworkAvailable(AppContextUtils.getAppContext())) {
            mView.showDialog();
            return;
        }
        if (getMapData && lat != 0 && lon != 0) {
            getMapData = false;
            if (searchState && TextUtils.isEmpty(searchText)) {
                // 如果是搜索页面 没有关键词数据需要查询时，初始化页面不进行查询
                getMapData = true;
                return;
            }
            /** poi查询的关键词 */
            String mKeyword = "";// 大楼|大厦|小区|酒店|广场|美食|百货
            /** Poi查询条件类*/
            PoiSearch.Query mQuery;
            PoiSearch mPoiSearch;
            // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            if (searchState) {
                mQuery = new PoiSearch.Query(searchText, mKeyword, mFirstGetCity);
            } else {
                mQuery = new PoiSearch.Query(mKeyword, mKeyword, "");
            }
            mQuery.setPageSize(10);// 设置每页最多返回多少条poiitem
            mQuery.setPageNum(page);// 设置查第几页
            mPoiSearch = new PoiSearch(mView.getContext(), mQuery);
            if (!searchState || TextUtils.isEmpty(searchText)) {
                mPoiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lon), 5000, true));//搜索周边，5000为搜索半径
            }
            mPoiSearch.setOnPoiSearchListener(mMapPOIListener);
            mPoiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    @Override
    public void onMapListItemClick(PoiItem poiItem, boolean isShowMap, boolean isSearchNear) {

        if (poiItem != null && poiItem.getLatLonPoint() != null) {
            setLocationInfo(poiItem);
            lat = poiItem.getLatLonPoint().getLatitude();
            lon = poiItem.getLatLonPoint().getLongitude();
            if (isShowMap && mView != null) {
                mView.showMap(lat, lon, isSearchNear);
            }
        }
    }

    @Override
    public void setLocationInfo(PoiItem poiItem) {

        mCurrentAdCode = poiItem.getAdCode();
        mCurrentCity = poiItem.getCityName();
        mLocation = getLocation(poiItem);
        addressName = poiItem.getTitle();
    }

    @Override
    public void clearSearchStatus() {

    }

    @Override
    public void backData(int type) {

        GpsBean gpsBean = ToonLocationUtil.gcjToGps84(lat, lon);
        if (type == 2) {
            PluginMapLocationBean locationBean = new PluginMapLocationBean();
            locationBean.setLatitude(gpsBean.getLatitude());
            locationBean.setLongitude(gpsBean.getLongitude());
            locationBean.setLocation(mLocation);
            locationBean.setContent(addressName);
            locationBean.setCity(mCurrentCity);
            locationBean.setAdCode(mCurrentAdCode);
            if (mCallback != null) {
                mCallback.onBackLatLon(locationBean);
            }
        } else if (type == 3) {
            if (mCallback != null) {
                TNPUserCommonPosition locationBean = new TNPUserCommonPosition();
                locationBean.setName(addressName);

                locationBean.setAddress(mLocation);
                locationBean.setLocationDetail(mLocation);
                locationBean.setLocationCoordinate(
                        gpsBean.getLatitude() + "," + gpsBean.getLongitude());
                locationBean.setAdcodes(mCurrentAdCode);
                mCallback.onBackCommonLocation(locationBean);
            }
        }
    }

    @Override
    public void backLatLonAndImageUrl(String filePath) {

        GpsBean gpsBean = ToonLocationUtil.gcjToGps84(lat, lon);
        PluginMapLocationBean locationBean = new PluginMapLocationBean();
        locationBean.setLatitude(gpsBean.getLatitude());
        locationBean.setLongitude(gpsBean.getLongitude());
        locationBean.setLocation(addressName);
        locationBean.setContent(mLocation);
        if (mCallback != null && !TextUtils.isEmpty(filePath)) {
            locationBean.setUrl(filePath);
            mCallback.onScreenShot(locationBean);
        }
    }

    @Override
    public boolean checkPOIDataEquals(PoiItem item) {
        if (TextUtils.equals(mCurrentAdCode, item.getAdCode())
                && TextUtils.equals(mCurrentCity, item.getCityName())
                && TextUtils.equals(addressName, item.getTitle())) {
            return true;
        }
        return false;
    }

    /**
     * 查询poi监听
     */
    private PoiSearch.OnPoiSearchListener mMapPOIListener = new PoiSearch.OnPoiSearchListener() {

        @Override
        public void onPoiSearched(PoiResult poiResult, int rCode) {

            if (rCode == 1000) {
                if (poiResult != null && mView != null) {
                    mView.showPoiData(poiResult);
                }
            }
            getMapData = true;
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }
    };

    private String getLocation(PoiItem item) {

        StringBuilder addr = new StringBuilder();
        if (item.getProvinceName() != null && !mCityMap.containsValue(item.getProvinceName())) {
            addr.append(item.getProvinceName());
        }
        if (item.getCityName() != null) {
            addr.append(item.getCityName());
        }
        if (item.getAdName() != null) {
            addr.append(item.getAdName());
        }
        if (item.getSnippet() != null) {
            addr.append(item.getSnippet());
        }
        return addr.toString();
    }


    @Override
    public void onDestroyPresenter() {
        if (mCityMap != null && !mCityMap.isEmpty()) {
            mCityMap.clear();
        }
        mCityMap = null;
        mToonLocationUtil = null;
        mView = null;
        isGetFirstCity = false;
    }
}
