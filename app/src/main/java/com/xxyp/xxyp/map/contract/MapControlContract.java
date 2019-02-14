package com.xxyp.xxyp.map.contract;

import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;

/**
 * Description : 地图控制器
 */

public interface MapControlContract {

    interface MapControlView extends IBaseView<MapControlPresenter> {

        /**
         * 初始化地图展示view
         */
        View initMapView();

        /**
         * 无网提示
         */
        void showDialog();

        /**
         * 显示地图信息
         */
        void showMap(double lat, double lon, boolean isSearchNear);

        /**
         * 显示周边热点
         */
        void showPoiData(PoiResult poiResult);

        /**
         * 改变界面显示，显示搜索功能
         */
        void changeSearchLayout();


        /**
         * 返回是否执行backPress方法
         */
        boolean isBackPress();

        /**
         * 展示加载框
         *
         * @param cancelable 是否可取消
         */
        void showLoadingDialog(boolean cancelable);

        /**
         * 取消加载框
         */
        void dismissLoadingDialog();

    }

    interface MapControlPresenter extends IBasePresenter<MapControlView> {

        /**
         * 初始化城市相关map数据
         */
        void initCityMap();

        void getLocation();

        /**
         * 计算经纬度误差 true 表示拖动，false 表示未拖动
         */
        boolean calculateError(CameraPosition position);

        /**
         * 搜索周边信息
         */
        void searchNear(boolean searchState, String searchText, int page);

        /**
         * 地址信息列表展示页
         */
        void onMapListItemClick(PoiItem item, boolean isShowMap, boolean isSearchNear);

        /**
         * 设置全局参数
         */
        void setLocationInfo(PoiItem item);

        /**
         * 清除搜索状态
         */
        void clearSearchStatus();

        void backData(int type);

        /**
         * 返回当前选择的经纬度信息和截图网络地址
         */
        void backLatLonAndImageUrl(String filePath);

        /**
         * 检查poi对象是否是之前查询的对象一致
         */
        boolean checkPOIDataEquals(PoiItem item);
    }
}
