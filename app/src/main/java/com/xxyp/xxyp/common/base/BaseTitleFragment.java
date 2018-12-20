
package com.xxyp.xxyp.common.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.LoadingDialog;

import java.util.List;

/**
 * Description : 基础带标题的fragment Created by sunpengfei on 2017/7/27.
 */
public abstract class BaseTitleFragment extends BaseFragment {

    private LinearLayout mContainer;

    private View mContentView;

    protected Header mHeader;

    private View mStatusBar;

    private LoadingDialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_activity, container, false);
        mContainer = ((LinearLayout)view.findViewById(R.id.container));
        RelativeLayout headerContainer = (RelativeLayout)view.findViewById(R.id.container_header);
        mStatusBar = view.findViewById(R.id.container_status_bar);
        if (Build.VERSION.SDK_INT >= 21) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    ScreenUtils.getStatusBarHeight());
            mStatusBar.setLayoutParams(params);
            mStatusBar.setVisibility(View.VISIBLE);
        }
        mHeader = onCreateHeader(headerContainer);
        addView();
        return view;
    }

    /**
     * 添加页面view
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

    protected void setViewListener() {
    }

    protected void initDataForActivity() {
    }

    public void hideHeader() {
        if (mHeader != null) {
            mHeader.hideHeader();
        }
        mStatusBar.setVisibility(View.GONE);
    }

    public void hideTitleHeader() {
        if (mHeader != null) {
            mHeader.hideHeader();
        }
    }

    protected abstract Header onCreateHeader(RelativeLayout headerContainer);

    protected abstract View onCreateView();

    /**
     * 展示加载框
     * @param cancelable  是否可取消
     */
    public void showLoadingDialog(boolean cancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getActivity());
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
