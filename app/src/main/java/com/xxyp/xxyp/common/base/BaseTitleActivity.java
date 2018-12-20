
package com.xxyp.xxyp.common.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.ImmerseLinearLayout;
import com.xxyp.xxyp.common.view.LoadingDialog;

import java.util.List;

/**
 * Description : 基础带标题的activity Created by sunpengfei on 2017/7/27. Person in
 * charge : sunpengfei
 */
public abstract class BaseTitleActivity extends BaseActivity {

    private ImmerseLinearLayout mContainer;

    private View mContentView;

    protected Header mHeader;

    private LoadingDialog mLoadingDialog;

    private View mStatusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.base_activity);
        mContainer = ((ImmerseLinearLayout)findViewById(R.id.container));
        RelativeLayout headerContainer = (RelativeLayout)findViewById(R.id.container_header);
        mStatusBar = findViewById(R.id.container_status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.getStatusBarHeight());
            mStatusBar.setLayoutParams(params);
            mStatusBar.setVisibility(View.VISIBLE);
        }
        mHeader = onCreateHeader(headerContainer);
        initDataFromFront(getIntent());
        addView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            setIntent(intent);
            initDataFromFront(intent);
            if (mContainer != null) {
                mContainer.removeView(mContentView);
            }
            addView();
        }
    }

    /**
     * 设置沉浸式状态栏
     */
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 添加页面布局
     */
    private void addView() {
        mContentView = onCreateView();
        if (mContentView != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0);
            params.weight = 1;
            mContainer.addView(mContentView, params);
        }
        setViewListener();
        initDataForActivity();
    }

    /**
     * 初始化传入数据
     * @param intent  传入数据
     */
    protected void initDataFromFront(Intent intent) {
    }

    /**
     * 初始化数据
     */
    protected void initDataForActivity() {
    }

    /**
     * 设置监听
     */
    protected void setViewListener() {
    }

    protected abstract Header onCreateHeader(RelativeLayout headerContainer);

    protected abstract View onCreateView();

    /**
     * 隐藏标题
     */
    public void hideHeader() {
        hideTitleHeader();
        mStatusBar.setVisibility(View.GONE);
    }

    public void hideTitleHeader() {
        if (mHeader != null) {
            mHeader.hideHeader();
        }
    }

    /**
     * 展示加载框
     * @param cancelable  是否可取消
     */
    public void showLoadingDialog(boolean cancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }else if(mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.show();
    }

    /**
     * 取消加载框
     */
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    protected void setNull(Object object) {
        if (object != null) {
            object = null;
        }
    }

    protected void setNull(List<Object> objects) {
        if (objects != null) {
            objects.clear();
            objects = null;
        }
    }
}
