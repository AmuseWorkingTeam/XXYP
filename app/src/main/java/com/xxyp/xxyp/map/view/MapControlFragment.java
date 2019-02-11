package com.xxyp.xxyp.map.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleFragment;
import com.xxyp.xxyp.common.utils.AppContextUtils;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.common.utils.dialog.DialogUtils;
import com.xxyp.xxyp.common.utils.permissions.PermissionsConstant;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.map.adapter.MapControlGDAdapter;
import com.xxyp.xxyp.map.config.MapCommonConfig;
import com.xxyp.xxyp.map.contract.MapControlContract;
import com.xxyp.xxyp.map.location.beans.PluginMapLocationBean;
import com.xxyp.xxyp.map.location.beans.TNPUserCommonPosition;
import com.xxyp.xxyp.map.location.interfaces.LocationMapCallBack;
import com.xxyp.xxyp.map.presenter.MapControlPresenter;
import com.xxyp.xxyp.map.pulltorefresh.PullToRefreshBase;
import com.xxyp.xxyp.map.pulltorefresh.PullToRefreshListView;
import com.xxyp.xxyp.map.utils.NetworkUtils;
import com.xxyp.xxyp.map.utils.SysUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 地图选择展示页面
 * Created by 135033 on 2016/11/21.
 * Job number：135033
 * Phone ：18601413765
 * Email：wangyue@syswin.com
 * Person in charge : 135033
 * Leader：135033
 */

public class MapControlFragment extends BaseTitleFragment implements MapControlContract.MapControlView, AdapterView.OnItemClickListener, LocationMapCallBack {

    public static final String ENTER_TYPE = "enterType";
    public static final String MAP_LOCATION_BEAN = "mapLocationBean";
    public static final String COMMON_LOCATION = "commonLocation";

    private String[] permissions = new String[]{
            PermissionsConstant.COARSE_LOCATION
    };

    /**
     * 访问功能类型
     * 2 返回PluginMapLocationBean类型数据（不带截图url信息）；3 TNPUserCommonPosition类型数据；4 返回PluginMapLocationBean（带截图url信息）
     */
    private int enterType;

    private AMap mAMap;

    private MapView mMapView;

    private View mMapLayout;

    private View mMapSearchView, mSearchClearBtn;

    private MapControlContract.MapControlPresenter mPresenter;

    private LinearLayout emptyView;

    private PullToRefreshListView mMapListView, mSearchListView;

    private MapControlGDAdapter mMapAdapter, mSearchAdapter;

    private EditText mSearchEditView;

    /**
     * 是否显示默认的当前位置项
     */
    private boolean isShowDefault = true;

    /**
     * 控制搜索点击item时保留的选中item
     */
    private boolean isSaveCurrentItem = false;

    /**
     * 搜索功能状态
     */
    private boolean searchState = false;

    /**
     * 地图页面请求页数；搜索页面请求页数
     */
    private int mapPageNum = 1, searchPageNum = 1;

    /**
     * 地图展示缩放比
     */
    private float zoom = 18f;

    private View mView;
    public static final String EXIT_ANIM = "exitAnim";
    public static final String ENTER_ANIM = "enterAnim";

    private int exitAnim = 0, enterAnim = 0;

    private boolean isCouldClick = false;


    @Override
    public Header onCreateHeader(RelativeLayout container) {

        requestPermissions(permissions);
        if (getArguments() != null) {
            enterType = getArguments().getInt(ENTER_TYPE, 2);
            enterAnim = getArguments().getInt(ENTER_ANIM, 0);
            exitAnim = getArguments().getInt(EXIT_ANIM, 0);
        }

        Header.Builder builder = new Header.Builder(getActivity(), container);
        builder.setBackIcon(R.drawable.header_back_icon, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SysUtils.dismissKeyBoard(getActivity());
                getActivity().finish();
                if (enterAnim != 0 || exitAnim != 0) {
                    getActivity().overridePendingTransition(enterAnim, exitAnim);
                }
            }
        });
        builder.setTitle(R.string.map_title_name);
        builder.setRightText(R.string.ok, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isCouldClick) {
                    mPresenter.backData(enterType);
                }
            }
        });
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        mPresenter = new MapControlPresenter(this, this);
        return initMapView();
    }

    @Override
    public void onPermissionGranted(List<String> permissions) {

        super.onPermissionGranted(permissions);
    }

    @Override
    public void onPermissionDenied(List<String> permissions) {
        super.onPermissionDenied(permissions);
        DialogUtils.getInstance().showDialog(getActivity(), "提示", "位置权限未开启，开启默认位置", "确认", "知道了", null);
    }

    @Override
    public View initMapView() {
        isCouldClick = false;
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ScreenUtils.widthPixels, ScreenUtils.heightPixels));
        mView = View.inflate(getContext(), R.layout.view_map_control, frameLayout);
        mMapLayout = mView.findViewById(R.id.map_layout);
        mMapView = (MapView) mMapLayout.findViewById(R.id.map_main);
        mMapView.onCreate(null);
        return frameLayout;
    }

    private void initMapParams() {

        mAMap = mMapView.getMap();

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);  // 设置定位模式
        myLocationStyle.showMyLocation(false); //设置默认小绿点是否显示
        mAMap.setMyLocationStyle(myLocationStyle);

        mAMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
        mAMap.getUiSettings().setLogoPosition(3);
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.setOnCameraChangeListener(mOnChangeScreenListener);

        mapPageNum = 1;
        mPresenter.getLocation();
    }

    private void showView() {

        // 地图
        mView.findViewById(R.id.map_search_begin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchState = true;
                // 开始搜索
                changeSearchLayout();
            }
        });

        mMapListView = (PullToRefreshListView) mView.findViewById(R.id.map_pull_refresh);
        // 搜索
        mMapSearchView = mView.findViewById(R.id.map_search_layout);
        emptyView = (LinearLayout) mMapSearchView.findViewById(R.id.empty_view);
        View cancelBtn = mMapSearchView.findViewById(R.id.map_search_cancel);
        mSearchEditView = (EditText) mMapSearchView.findViewById(R.id.map_search_editView);
        mSearchEditView.addTextChangedListener(mTextWatcher);
        mSearchEditView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mSearchClearBtn = mMapSearchView.findViewById(R.id.map_search_clear);

        mSearchListView = (PullToRefreshListView) mMapSearchView.findViewById(R.id.map_search_pull_refresh);
        mMapListView.setOnItemClickListener(MapControlFragment.this);
        mSearchListView.setOnItemClickListener(MapControlFragment.this);
        mMapListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {

                searchState = false;
                mPresenter.searchNear(false, "", mapPageNum);
            }
        });

        mMapListView.setMode(PullToRefreshBase.Mode.DISABLED);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchState = false;
                onBackPress();
            }
        });
        mSearchClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSearchEditView.setText("");
            }
        });
        mSearchListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {

                searchState = true;
                mPresenter.searchNear(true, mSearchEditView.getText().toString().trim(), searchPageNum);
            }
        });
        mSearchListView.setMode(PullToRefreshBase.Mode.DISABLED);
        initMapParams();
    }

    @Override
    public void onStart() {

        super.onStart();
        if (mMapListView == null) {
            showView();
        }
    }

    /**
     * 拖动监听
     */
    private AMap.OnCameraChangeListener mOnChangeScreenListener = new AMap.OnCameraChangeListener() {

        @Override
        public void onCameraChange(CameraPosition position) {

        }

        @Override
        public void onCameraChangeFinish(CameraPosition position) {

            if (mPresenter.calculateError(position)) {
                zoom = position.zoom;
                mapPageNum = 1;
                mMapListView.scrollTo(0, 0);
                if (mMapAdapter != null) {
                    mMapAdapter.initCurrentItem();
                    mMapAdapter.clearData();
                }
                searchState = false;
                mPresenter.searchNear(false, "", mapPageNum);
            }
        }
    };

    @Override
    public void showDialog() {
        mMapLayout.removeCallbacks(noNetHint);
        mMapLayout.postDelayed(noNetHint, 300);
    }

    private Runnable noNetHint = new Runnable() {
        @Override
        public void run() {
            ToastUtil.showTextViewPrompt(getContext().getString(R.string.map_no_internet_prompt));
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.length() > 0) {
                String searchText = s.toString().trim();
                searchPageNum = 1;
                mPresenter.searchNear(true, searchText, searchPageNum);
                mSearchClearBtn.setVisibility(View.VISIBLE);
                mSearchListView.setVisibility(View.VISIBLE);
                if (mSearchAdapter != null) {
                    mSearchAdapter.clearData();
                }
            } else {
                mSearchClearBtn.setVisibility(View.INVISIBLE);
                mSearchListView.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void showMap(double lat, double lon, boolean isSearchNear) {

        LatLng marker1 = new LatLng(lat, lon);
        // 设置中心点
        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        if (isSearchNear && NetworkUtils.isNetworkAvailable(AppContextUtils.getAppContext())) {
            mPresenter.searchNear(searchState, mSearchEditView.getText().toString().trim(), searchState ? searchPageNum : mapPageNum);
        }
    }

    @Override
    public void showPoiData(PoiResult poiResult) {

        List<PoiItem> poiItems = poiResult.getPois();
        if (poiItems != null && poiItems.size() > 0) {
            isCouldClick = true;
            int currentPage = 1;
            if (poiResult.getQuery() != null) {
                currentPage = poiResult.getQuery().getPageNum();
            }
            if (searchState) {
                isShowDefault = false;
                emptyView.setVisibility(View.GONE);
                setAdapterData(mSearchAdapter, mSearchListView, poiItems, currentPage);
                searchPageNum++;
            } else {
                isCouldClick = true;
                PoiItem item = poiItems.get(0);
                if (mMapAdapter != null && mMapAdapter.getCount() == 1) {
                    if (mPresenter != null && mPresenter.checkPOIDataEquals(item)) {
                        // 如果搜索结果和POI查询结果有重复现象去重
                        mMapAdapter.clearData();
                    }
                } else {
                    mPresenter.setLocationInfo(item);
                }
                setAdapterData(mMapAdapter, mMapListView, poiItems, currentPage);
                mapPageNum++;
            }
        } else {
            if (searchState) {
                // 搜索状态
                if (searchPageNum == 1) {
                    mSearchListView.setVisibility(View.GONE);
                    if (mSearchEditView.getText().toString().length() > 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                isCouldClick = false;
            }
        }
    }

    private void setAdapterData(MapControlGDAdapter adapter, PullToRefreshListView pullToRefreshListView, List<PoiItem> poiItems, int currentPage) {

        if (adapter == null) {
            adapter = new MapControlGDAdapter(getContext(), poiItems);
            adapter.setDefault(isShowDefault);
            adapter.setSearchState(searchState);
            pullToRefreshListView.setAdapter(adapter);
            pullToRefreshListView.setOnItemClickListener(this);
            if (poiItems != null && !poiItems.isEmpty() && !isSaveCurrentItem) {
                adapter.setCurrentItem(poiItems.get(0), 0);
            }
        } else {
            adapter.setDefault(isShowDefault);
            adapter.setSearchState(searchState);
            boolean isClear = false;
            if (currentPage == 1 && !isSaveCurrentItem) {
                isClear = true;
                isSaveCurrentItem = false;
            }
            adapter.addData(poiItems, isClear);
        }
        if (searchState) {
            mSearchAdapter = adapter;
        } else {
            mMapAdapter = adapter;
        }
    }

    @Override
    public void changeSearchLayout() {

        if (searchState) {
            // 打开搜索状态, 先清除上一次查询记录
            mSearchEditView.setText("");
            if (mSearchAdapter != null) {
                mSearchAdapter.clearData();
            }
            isSaveCurrentItem = false;
            emptyView.setVisibility(View.GONE);
            mMapSearchView.setVisibility(View.VISIBLE);
            mMapLayout.setVisibility(View.GONE);
            mHeader.getView().setVisibility(View.GONE);
            mSearchEditView.requestFocus();
            mPresenter.searchNear(true, "", searchPageNum);
            SysUtils.showKeyBoard(getActivity());
        } else {
            // 关闭搜索状态
            isBackPress();
        }
    }

    @Override
    public boolean isBackPress() {

        mPresenter.clearSearchStatus();
        emptyView.setVisibility(View.GONE);
        if (mSearchAdapter != null) {
            mSearchAdapter.clearData();
        }
        mMapLayout.setVisibility(View.VISIBLE);
        mHeader.getView().setVisibility(View.VISIBLE);
        if (mMapSearchView.getVisibility() == View.VISIBLE) {
            mMapSearchView.setVisibility(View.GONE);
            SysUtils.dismissKeyBoard(getContext());
            return true;
        }
        return false;
    }

    @Override
    public void setPresenter(MapControlContract.MapControlPresenter presenter) {

        mPresenter = presenter;
    }

    @Override
    public Context getContext() {

        return getActivity();
    }

    @Override
    public void onResume() {

        super.onResume();

        if (mMapAdapter != null && mMapAdapter.getCount() > 0) {
            isCouldClick = true;
        } else {
            isCouldClick = false;
        }
        mMapView.onResume();
    }

    public void onPause() {

        super.onPause();
        mMapView.onPause();
    }

    @Override
    public boolean onBackPress() {

        if (isBackPress()) {
            return super.onBackPress();
        }
        if (enterAnim != 0 || exitAnim != 0) {
            getActivity().finish();
            getActivity().overridePendingTransition(enterAnim, exitAnim);
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (searchState) {
            searchState = false;
            isSaveCurrentItem = true;
            if (mSearchAdapter != null) {
                PoiItem item = mSearchAdapter.getItem(position);
                if (null != item) {
                    mPresenter.onMapListItemClick(item, true, true);
                    if (mMapAdapter != null) {
                        mMapAdapter.clearData();
                        mMapAdapter.setCurrentItem(item, 0);
                        mMapAdapter.add(0, item);
                    } else {
                        List<PoiItem> poiItems = new ArrayList<>();
                        poiItems.add(item);
                        setAdapterData(mMapAdapter, mMapListView, poiItems, 0);
                    }
                }
                onBackPress();
                mapPageNum = 1;
            }
        } else {
            if (mMapAdapter != null) {
                PoiItem item = mMapAdapter.getItem(position);
                if (null != item) {
                    mMapAdapter.setCurrentItem(item, position);
                    mPresenter.onMapListItemClick(item, true, false);
                }
            }
        }
    }

    @Override
    public void onLocationChanged() {

    }

    @Override
    public void onScreenShot(PluginMapLocationBean bean) {

        Intent intent = new Intent();
        intent.putExtra(MAP_LOCATION_BEAN, bean);
        getActivity().setResult(MapCommonConfig.CREATE_CARD_FOR_RESULT_MAP, intent);
        getActivity().finish();
        if (enterAnim != 0 || exitAnim != 0) {
            getActivity().overridePendingTransition(enterAnim, exitAnim);
        }
    }

    @Override
    public void onBackLatLon(PluginMapLocationBean bean) {

        Intent intent = new Intent();
        intent.putExtra(MAP_LOCATION_BEAN, bean);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
        if (enterAnim != 0 || exitAnim != 0) {
            getActivity().overridePendingTransition(enterAnim, exitAnim);
        }
    }

    @Override
    public void onBackCommonLocation(TNPUserCommonPosition bean) {

        Intent intent = new Intent();
        intent.putExtra(COMMON_LOCATION, bean);
        getActivity().setResult(MapCommonConfig.CREATE_CARD_FOR_RESULT_MAP, intent);
        getActivity().finish();
        if (enterAnim != 0 || exitAnim != 0) {
            getActivity().overridePendingTransition(enterAnim, exitAnim);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroyPresenter();
        mMapView.onDestroy();
        mPresenter = null;
        mAMap = null;
        mMapAdapter = null;
        mMapView = null;
        mSearchAdapter = null;
        mView = null;
        mMapLayout = null;
    }
}
