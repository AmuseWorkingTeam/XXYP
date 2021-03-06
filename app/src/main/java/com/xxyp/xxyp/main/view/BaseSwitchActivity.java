
package com.xxyp.xxyp.main.view;

import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseFragment;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.utils.AppContextUtils;
import com.xxyp.xxyp.common.view.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 可切换页面activity Created by sunpengfei on 2017/8/1. Person in
 * charge : sunpengfei
 */
public abstract class BaseSwitchActivity extends BaseTitleActivity {

    private BaseFragment mCurrentFragment;

    private int mCurrentIndex = -1;

    private SparseArray<BaseFragment> mFragmentCache;

    protected RelativeLayout mContainer;

    protected LinearLayout mTabLayout;

    private List<TabSwitchView> mTabSwitch;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        return new Header.Builder(this, headerContainer).build();
    }

    @Override
    protected View onCreateView() {
        hideHeader();
        View view = View.inflate(this, R.layout.activity_main, null);
        mContainer = (RelativeLayout) view.findViewById(R.id.container);
        mTabLayout = ((LinearLayout)view.findViewById(R.id.ll_switch_tab));
        mFragmentCache = new SparseArray();
        mTabSwitch = new ArrayList<>();
        initFragment(mTabSwitch);
        initTabView();
        return view;
    }

    protected abstract void initFragment(List<TabSwitchView> mTabSwitch);

    protected void onTabClick() {

    }

    /**
     * 初始化底部切换view
     */
    protected void initTabView() {
        for (int i = 0; i < mTabSwitch.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            mTabLayout.addView((mTabSwitch.get(i)).mSwitchView,
                    params);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        switchFragment(mCurrentIndex);
    }

    /**
     * 切换fragment
     * @param index  索引
     */
    protected void switchFragment(int index) {
        if (mCurrentIndex == index && mCurrentFragment != null) {
            mCurrentFragment.onTabClickAgain();
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        if (index < 0 || index > 4) {
            index = 0;
        }
        mCurrentIndex = index;
        mCurrentFragment = mFragmentCache.get(mCurrentIndex);
        if (mCurrentFragment == null) {
            return;
        }
        for(TabSwitchView tabSwitchView : mTabSwitch){
            tabSwitchView.getTabImageView().setSelected(tabSwitchView.mIndex == index);
        }
        if (!mCurrentFragment.isAdded()) {
            transaction.add(R.id.fl_main, mCurrentFragment,
                    mCurrentFragment.getClass().getName());
        }else{
            transaction.show(mCurrentFragment);
        }
        transaction.commit();
    }

    protected BaseFragment getCurrentFrament() {
        return mCurrentFragment;
    }

    protected final class TabSwitchView implements View.OnClickListener {

        private int mDrawable;

        private int mIndex = -1;

        private ImageView mIvTab;

        private View mSwitchView;

        public TabSwitchView(@DrawableRes int drawable, BaseFragment baseFragment,
                             int index) {
            if (baseFragment != null && index > -1) {
                mFragmentCache.put(index, baseFragment);
            }
            mDrawable = drawable;
            mIndex = index;
            mSwitchView = View.inflate(AppContextUtils.getAppContext(), R.layout.switch_tab, null);
            mIvTab = ((ImageView)mSwitchView.findViewById(R.id.iv_switch_tab));
            mIvTab.setImageResource(mDrawable);
            mSwitchView.setOnClickListener(this);
        }

        public View getSwitchView() {
            return mSwitchView;
        }

        public ImageView getTabImageView() {
            return mIvTab;
        }

        public void onClick(View paramView) {
            onTabClick();
            switchFragment(mIndex);
        }
    }
}
