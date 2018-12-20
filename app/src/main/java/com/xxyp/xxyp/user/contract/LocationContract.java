
package com.xxyp.xxyp.user.contract;

import com.amap.api.maps.AMap;
import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;

import java.util.List;

/**
 * Description : 位置contract Created by sunpengfei on 2018/4/3.
 */
public interface LocationContract {

    /**
     * 位置view
     */
    interface View extends IBaseView<Presenter>{

        /**
         * 展示定位
         * @param lat  纬度
         * @param lon  经度
         */
        void showMap(double lat, double lon);
    }

    /**
     * 位置presenter
     */
    interface Presenter extends IBasePresenter<View>{

        /**
         * 初始化定位蓝点
         * @param aMap  定位map
         */
        void initMap(AMap aMap);

        /**
         * 开启定位
         */
        void startLocation();

        /**
         * 关闭定位
         */
        void stopLocation();

        /**
         * 权限允许
         *
         * @param permissions 申请的权限
         */
        void onPermissionGranted(List<String> permissions);

        /**
         * 权限拒绝
         *
         * @param permissions 申请的权限
         */
        void onPermissionDenied(List<String> permissions);

        /**
         * 返回
         */
        void onBack();

    }

}
