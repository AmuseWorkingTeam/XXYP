
package com.xxyp.xxyp.user.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.PermissionActivity;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.common.utils.dialog.CommonDialog;
import com.xxyp.xxyp.common.utils.dialog.DialogUtils;
import com.xxyp.xxyp.common.utils.permissions.PermissionsConstant;
import com.xxyp.xxyp.user.contract.LocationContract;
import com.xxyp.xxyp.user.utils.FrameConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description : 位置presenter Created by sunpengfei on 2018/4/4.
 */
public class LocationPresenter implements LocationContract.Presenter {

    private LocationContract.View mView;

    /* 地图client */
    private AMapLocationClient mLocationClient;

    /* 地图配置 */
    private AMapLocationClientOption mLocationOption;

    /* 定位监听 */
    private AMapLocationListener mLocationListener;

    /* 地址 市区*/
    private String mAddress;

    /* 详细地址 */
    private String mDetailAddress;

    public LocationPresenter(LocationContract.View view){
        mView = view;
        initLocation();
    }

    /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void initLocation(){
        initLocationListener();
        //初始化client
        mLocationClient = new AMapLocationClient(mView.getContext());
        mLocationOption = getDefaultOption();
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 设置定位监听
        mLocationClient.setLocationListener(mLocationListener);
    }

    /**
     * 判断定位权限是否开启
     * @return boolean
     */
    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(mView.getContext().getContentResolver(),
                        Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                XXLog.log_e("LocationPresenter", "isLocationEnabled is failed " + e);
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(mView.getContext().getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * 定位监听
     */
    private void initLocationListener(){
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                if (null != location && location.getErrorCode() == 0) {
                    //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                    showMapLocation(location);
                    StringBuilder sb = new StringBuilder();
                    mAddress = sb.append(location.getCity()).append("·")
                            .append(location.getDistrict()).toString();
                    mDetailAddress = location.getAddress();
                } else {
                    //定位失败
                    StringBuilder sb = new StringBuilder();
                    if(location != null){
                        sb.append("错误码:" + location.getErrorCode() + "\n");
                        sb.append("错误信息:" + location.getErrorInfo() + "\n");
                        sb.append("错误描述:" + location.getLocationDetail() + "\n");
                    }
                    XXLog.log_e("定位失败", sb.toString());
                }
            }
        };
    }

    /**
     * 延时两秒展示
     * @param location  位置信息
     */
    private void showMapLocation(final AMapLocation location){
        Observable.just(location).delay(2, TimeUnit.SECONDS).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AMapLocation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AMapLocation location1) {
                        if(mView != null){
                            mView.showMap(location1.getLatitude(), location1.getLongitude());
                        }
                    }
                });
    }

    /**
     * 默认的定位参数
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(10 * 1000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    @Override
    public void initMap(AMap aMap) {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //定位一次 且将视角移动到地图中心
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        aMap.getUiSettings().setLogoPosition(3);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setMyLocationEnabled(true);
//        aMap.setOnCameraChangeListener(this.mOnChangeScreenListener);
    }

    @Override
    public void startLocation() {
        if(!isLocationEnabled()){
            //如果定位服务没开启  弹出提示框
            DialogUtils.getInstance().showDialog((Activity) mView.getContext(), mView.getContext().getString(R.string.tip), mView.getContext().getString(R.string.location_service_tip), new CommonDialog.OnDialogListener() {
                @Override
                public void onConfirmListener() {
                    ((Activity) mView.getContext()).finish();
                }

                @Override
                public void onCancelListener() {

                }
            });
            return;
        }
        if(hasPermission(PermissionsConstant.COARSE_LOCATION)){
            openLocation();
        }
    }

    /**
     * 打开定位
     */
    private void openLocation(){
        // 设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void stopLocation() {
        // 停止定位
        mLocationClient.stopLocation();
    }

    @Override
    public void onPermissionGranted(List<String> permissions) {
        //权限允许
        if (permissions != null && permissions.size() > 0) {
            int size = permissions.size();
            for (int i = 0; i < size; i++) {
                switch (permissions.get(i)) {
                    // 如果定位权限允许
                    case PermissionsConstant.COARSE_LOCATION:
                        openLocation();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onPermissionDenied(List<String> permissions) {
        //权限拒绝
        if (permissions != null && permissions.size() > 0) {
            int size = permissions.size();
            for (int i = 0; i < size; i++) {
                switch (permissions.get(i)) {
                    // 如果定位权限允许
                    case PermissionsConstant.COARSE_LOCATION:
                        ToastUtil.showTextViewPrompt("定位失败!");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onBack() {
        Intent intent = new Intent();
        intent.putExtra(FrameConfig.LOCATION_INFO, mAddress);
        ((Activity)mView.getContext()).setResult(Activity.RESULT_OK, intent);
        ((Activity)mView.getContext()).finish();
    }

    /**
     * 是否有权限
     *
     * @param permissions 权限列表
     * @return 是否具有所有权限
     */
    private boolean hasPermission(String... permissions) {
        if (mView.getContext() instanceof PermissionActivity) {
            PermissionActivity activity = (PermissionActivity) mView.getContext();
            boolean bool = activity.hasPermission(permissions);
            if (!bool) {
                activity.requestPermissions(permissions);
            }
            return bool;
        } else {
            throw new IllegalArgumentException("is not allow request permission");
        }
    }

    @Override
    public void onDestroyPresenter() {
        mLocationClient = null;
        mLocationOption = null;
        mLocationListener = null;
    }
}
