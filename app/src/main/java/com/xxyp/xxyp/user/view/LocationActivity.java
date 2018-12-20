
package com.xxyp.xxyp.user.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.user.contract.LocationContract;
import com.xxyp.xxyp.user.presenter.LocationPresenter;

import java.util.List;

/**
 * Description : 位置定位
 * Created by sunpengfei on 2018/4/3.
 */
public class LocationActivity extends BaseTitleActivity implements LocationContract.View {

    /* 地图控件 */
    private MapView mMapView;

    /* 定位 */
    private AMap mAmap;

    /* 定位presenter */
    private LocationContract.Presenter mPresenter;
    
    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setTitle("位置");
        builder.setBackIcon(R.drawable.header_back_icon, new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        builder.setRightText(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPresenter != null){
                    mPresenter.onBack();
                }
            }
        });
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.activity_location, null);
        mMapView = (MapView) view.findViewById(R.id.map_location);
        mMapView.onCreate(null);
        mAmap = mMapView.getMap();
        mPresenter = new LocationPresenter(this);
        mPresenter.initMap(mAmap);
        return view;
    }

    @Override
    public void onPermissionGranted(List<String> permissions) {
        super.onPermissionGranted(permissions);
        mPresenter.onPermissionGranted(permissions);
    }

    @Override
    public void onPermissionDenied(List<String> permissions) {
        super.onPermissionDenied(permissions);
        mPresenter.onPermissionDenied(permissions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        setNull(mMapView);
        setNull(mAmap);
        mPresenter.onDestroyPresenter();
        setNull(mPresenter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setPresenter(LocationContract.Presenter presenter) {

    }

    @Override
    public void showMap(double lat, double lon) {
        LatLng marker1 = new LatLng(lat, lon);
        mMapView.getMap().moveCamera(CameraUpdateFactory.changeLatLng(marker1));
    }

}
