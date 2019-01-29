
package com.xxyp.xxyp.user.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.recyclerView.layoutManager.HeaderFooterGridLayoutManager;
import com.xxyp.xxyp.login.utils.UserConfig;
import com.xxyp.xxyp.main.bean.WorkBean;
import com.xxyp.xxyp.user.adapter.MyPhotoAdapter;
import com.xxyp.xxyp.user.contract.PhotoContract;
import com.xxyp.xxyp.user.presenter.PhotoPresenter;
import com.xxyp.xxyp.user.utils.FrameConfig;

import java.util.List;

/**
 * <Li> Description : 我的相册
 */
public class MyPhotoActivity extends BaseTitleActivity implements PhotoContract.View {

    private PhotoContract.Presenter mPresenter;


    private MyPhotoAdapter mAdapter;

    private SimpleDraweeView mAvatar;

    private ImageRequestConfig mConfig;

    private TextView mIntro;

    private TextView mName;

    /* 关注粉丝 */
    private RecyclerView mRecyclerView;

    private String userId;

    private ImageView mUserIdentity;

    /* 返回icon */
    private ImageView mIvFrameBack;

    private View rootView;
    private TextView tvHeartCount;
    private SwipeToLoadLayout stllRoot;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        return new Header.Builder(this, headerContainer).build();
    }

    @Override
    protected View onCreateView() {
        hideTitleHeader();
        View view = View.inflate(this, R.layout.activity_my_photo, null);
        stllRoot = (SwipeToLoadLayout) view.findViewById(R.id.container);
        mRecyclerView = ((RecyclerView) view.findViewById(R.id.photo_recycler));
        mRecyclerView
                .setLayoutManager(new HeaderFooterGridLayoutManager(this, 3, true, false));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyPhotoAdapter(this);

        View headerView = View.inflate(this, R.layout.photo_header, null);
        mIvFrameBack = (ImageView) headerView.findViewById(R.id.frame_back_icon);
        mAvatar = ((SimpleDraweeView) headerView.findViewById(R.id.frame_user_avatar));
        mName = ((TextView) headerView.findViewById(R.id.frame_user_name));
        mIntro = ((TextView) headerView.findViewById(R.id.frame_user_desc));
        mUserIdentity = ((ImageView) headerView.findViewById(R.id.frame_user_identity));
        LinearLayout llHeart = ((LinearLayout) headerView.findViewById(R.id.frame_user_heart));
        llHeart.setVisibility(View.VISIBLE);
        tvHeartCount = ((TextView) headerView.findViewById(R.id.frame_user_heart_count));
        mAdapter.addHeaderView(headerView);

        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new PhotoPresenter(this);
        return view;
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        if (intent == null) {
            return;
        }
        userId = intent.getStringExtra(FrameConfig.USER_ID);
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.getUserInfo(userId);
        mPresenter.getFansFollowCount(userId);
        mPresenter.obtainUserWorks(userId);
    }

    @Override
    protected void setViewListener() {
        //返回
        mIvFrameBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdapter.setWorkListener(new MyPhotoAdapter.OnWorkListener() {
            @Override
            public void openProduct(String userId, String workId) {
                mPresenter.openProduct(userId, workId);
            }
        });

        stllRoot.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //上拉刷新
                stllRoot.setLoadingMore(false);
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setPresenter(PhotoContract.Presenter presenter) {

    }

    @Override
    public void showUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        mName.setText(userInfo.getUserName());
        mIntro.setText(userInfo.getUserIntroduction());
        if (mConfig == null) {
            mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(104))
                    .setLoadHeight(ScreenUtils.dp2px(104)).setRender(true).build();
        }
        ImageLoader.getInstance().display(mAvatar, userInfo.getUserImage(), mConfig);
        if (userInfo.getUserIdentity() == UserConfig.UserType.CAMERAMER) {
            mUserIdentity.setBackgroundResource(R.drawable.cameraman_icon);
        } else {
            mUserIdentity.setBackgroundResource(R.drawable.customer_icon);
        }
    }

    @Override
    public void showUserWorks(List<WorkBean> workBeans) {
        if (workBeans == null || workBeans.size() == 0) {
            return;
        }
        mAdapter.setData(workBeans);
    }

    @Override
    public void showFansFollowCount(int followCount, int fansCount) {
        tvHeartCount.setText(fansCount);
    }

    @Override
    public void resetRefresh() {
        stllRoot.setRefreshing(false);
    }

    @Override
    public void resetLoadMore() {
        stllRoot.setLoadingMore(false);
    }

    @Override
    public void showFrameDialog(boolean cancelable) {
        showLoadingDialog(cancelable);
    }

    @Override
    public void cancelFrameDialog() {
        dismissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroyPresenter();
            setNull(mPresenter);
        }
        super.onDestroy();
    }
}
