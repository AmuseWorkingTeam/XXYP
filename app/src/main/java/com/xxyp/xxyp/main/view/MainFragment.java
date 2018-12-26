
package com.xxyp.xxyp.main.view;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleFragment;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ImageUtils;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.utils.TimeUtils;
import com.xxyp.xxyp.common.view.AutoViewPager;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.recyclerView.DividerItemDecoration;
import com.xxyp.xxyp.common.view.viewpagerindicator.CirclePageIndicator;
import com.xxyp.xxyp.main.adapter.HotWorkAdapter;
import com.xxyp.xxyp.main.adapter.HotWorkBannerAdapter;
import com.xxyp.xxyp.main.bean.WorkBean;
import com.xxyp.xxyp.main.contract.MainContract;
import com.xxyp.xxyp.main.presenter.MainPresenter;

import java.util.List;

/**
 * Description :热门
 * Created by sunpengfei on 2017/8/1.
 * Person in charge : sunpengfei
 */
public class MainFragment extends BaseTitleFragment implements MainContract.View {

    /* 上下拉 */
    private SwipeToLoadLayout mSwipeToLoadLayout;

    private HotWorkBannerAdapter mBannerAdapter;

    private CirclePageIndicator mCirclePageIndicator;

    private ImageRequestConfig mConfig;

    private HotWorkAdapter mHotWorkAdapter;

    private MainContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private RelativeLayout mRlHeaderView;

    private SimpleDraweeView mUserAvatar;

    private TextView mUserLocation;

    private TextView mUserName;

    private TextView mUserTime;

    private AutoViewPager mViewPager;

    /* 当前banner展示的用户的userId */
    private String mCurrentUserId;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(getActivity(), headerContainer);
        builder.setTitle(R.string.hot_works);
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(getActivity(), R.layout.fragment_main, null);
        mSwipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.container);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mHotWorkAdapter = new HotWorkAdapter(getActivity());

        View headerView = View.inflate(getActivity(), R.layout.fragment_main_header, null);
        mRlHeaderView = (RelativeLayout) headerView.findViewById(R.id.user_container);
        mViewPager = (AutoViewPager) headerView.findViewById(R.id.main_view_pager);
        mCirclePageIndicator = (CirclePageIndicator) headerView.findViewById(R.id.circle_pager_indicator);
        mBannerAdapter = new HotWorkBannerAdapter(getActivity());
        mViewPager.setAdapter(mBannerAdapter);
        mHotWorkAdapter.addHeaderView(headerView);

        mCirclePageIndicator.setViewPager(mViewPager);
        mUserAvatar = (SimpleDraweeView) headerView.findViewById(R.id.user_avatar);
        mUserName = (TextView) headerView.findViewById(R.id.user_name);
        mUserLocation = (TextView) headerView.findViewById(R.id.user_location);
        mUserTime = (TextView) headerView.findViewById(R.id.user_time);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.color.guide_divider));
        mRecyclerView.setAdapter(mHotWorkAdapter);
        mPresenter = new MainPresenter(this);
        return view;
    }

    @Override
    protected void setViewListener() {
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新 直接获取最新的数据
                mPresenter.getHotWorks();
            }
        });

        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //上拉刷新
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        });

        mHotWorkAdapter.setWorkListener(new HotWorkAdapter.OnWorkListener() {

            @Override
            public void onWorkListener(String userId, String workId) {
                mPresenter.openProduct(userId, workId);
            }

            @Override
            public void onOpenFrame(String userId) {
                mPresenter.openFrame(userId);
            }
        });

        mCirclePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showBannerUserInfo(mBannerAdapter.getItem(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mBannerAdapter.setWorkListener(new HotWorkBannerAdapter.OnWorkListener() {
            @Override
            public void onWorkListener(WorkBean workBean) {
                if (workBean == null) {
                    return;
                }
                mPresenter.openProduct(workBean.getUserId(), workBean.getWorksId());
            }
        });

        mRlHeaderView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mPresenter.openFrame(mCurrentUserId);
            }
        });
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.getHotWorks();
    }

    @Override
    public void showBannerUserInfo(WorkBean workBean) {
        mCurrentUserId = workBean.getUserId();
        if (mConfig == null) {
            mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(44))
                    .setLoadHeight(ScreenUtils.dp2px(44)).setRender(true).build();
        }
        String avatar = !TextUtils.isEmpty(workBean.getUserImage()) ? workBean.getUserImage() : "";
        String name = !TextUtils.isEmpty(workBean.getUserName()) ? workBean.getUserName() : "";
        String address = !TextUtils.isEmpty(workBean.getWorksAddress()) ? workBean.getWorksAddress()
                : "";
        String time = workBean.getReleaseTime() > 0
                ? TimeUtils.millis2String(workBean.getReleaseTime())
                : "";
        ImageLoader.getInstance().display(mUserAvatar, ImageUtils.getAvatarUrl(avatar), mConfig);
        mUserName.setText(name);
        mUserLocation.setText(address);
        mUserTime.setText(time);
    }

    @Override
    public void showWorkList(List<WorkBean> workBeanList) {
        mHotWorkAdapter.setList(workBeanList);
    }

    @Override
    public void showBannerList(List<WorkBean> workBeanList) {
        mBannerAdapter.setList(workBeanList);
    }

    @Override
    public void showHotWorkLoading(boolean cancelable) {
        showLoadingDialog(cancelable);
    }

    @Override
    public void cancelHotWorkLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void resetRefresh() {
        mSwipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void resetLoadMore() {
        mSwipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroyPresenter();
        setNull(mPresenter);
    }

    public void setPresenter(MainContract.Presenter paramPresenter) {
    }
}
