package com.xxyp.xxyp.map.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseActivity;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.view.Header;


/**
 * Description :  承载地图显示的activity
 * Created by jingmaolin on 2017/10/11.
 * Job number：600029
 * Phone ：13342446520
 * Email：jingmaolin@syswin.com
 * Person in charge : （jingmaolin：同一组）
 * Leader：（wangyue）
 */

public class MapFragmentLoadActivity extends BaseTitleActivity {
    private MapControlFragment mMapControlFragment;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        return new Header.Builder(this, headerContainer).build();
    }

    @Override
    protected View onCreateView() {
        hideHeader();
        return View.inflate(this, R.layout.activity_store_fragment, null);
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
        load();
    }

    private void load() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        mMapControlFragment = new MapControlFragment();
        Bundle mBundle = new Bundle();
        mBundle.putInt(MapControlFragment.ENTER_TYPE, 2);
        mMapControlFragment.setArguments(mBundle);
        transaction.add(R.id.fragment_map, mMapControlFragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mMapControlFragment != null) {
            mMapControlFragment.onBackPress();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapControlFragment = null;
    }
}
