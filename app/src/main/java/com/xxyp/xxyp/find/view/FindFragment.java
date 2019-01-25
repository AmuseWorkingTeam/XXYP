
package com.xxyp.xxyp.find.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleFragment;
import com.xxyp.xxyp.common.view.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 发现页面 Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class FindFragment extends BaseTitleFragment {

    private FindPagerAdapter mAdapter;

    private View mSelectShot;

    private View mSelectWorks;

    private TextView mTvShot;

    private TextView mTvWorks;

    private ViewPager mViewPager;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        View view = View.inflate(getActivity(), R.layout.fragment_find_header, null);
        mTvShot = ((TextView) view.findViewById(R.id.tv_shot));
        mSelectShot = view.findViewById(R.id.select_shot);
        mTvWorks = ((TextView) view.findViewById(R.id.tv_works));
        mSelectWorks = view.findViewById(R.id.select_works);
        return new Header.Builder(getActivity(), headerContainer).setDefineView(view)
                .build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(getActivity(), R.layout.fragment_find, null);
        mViewPager = ((ViewPager) view.findViewById(R.id.find_view_pager));
        mAdapter = new FindPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapter);
        return view;
    }

    @Override
    protected void setViewListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTvShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFragment(0);
            }
        });
        mTvWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFragment(1);
            }
        });
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
    }

    private void selectFragment(int index) {
        mViewPager.setCurrentItem(index);
        if (index == 0) {
            mSelectShot.setVisibility(View.VISIBLE);
            mSelectWorks.setVisibility(View.GONE);
        } else {
            mSelectShot.setVisibility(View.GONE);
            mSelectWorks.setVisibility(View.VISIBLE);
        }
    }

    private class FindPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments = new ArrayList();

        public FindPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments.add(new AppointFragment());
            mFragments.add(new WorkFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
