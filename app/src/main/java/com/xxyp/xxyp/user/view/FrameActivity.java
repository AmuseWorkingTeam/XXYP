
package com.xxyp.xxyp.user.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.recyclerView.layoutManager.HeaderFooterGridLayoutManager;
import com.xxyp.xxyp.login.utils.UserConfig;
import com.xxyp.xxyp.main.bean.WorkBean;
import com.xxyp.xxyp.user.adapter.FrameAdapter;
import com.xxyp.xxyp.user.contract.FrameContract;
import com.xxyp.xxyp.user.presenter.FramePresenter;
import com.xxyp.xxyp.user.utils.FrameConfig;

import java.util.List;

/**
 * Description : frame页面 Created by sunpengfei on 2017/8/3. Person in charge :
 * sunpengfei
 */
public class FrameActivity extends BaseTitleActivity implements FrameContract.View {

    private FrameAdapter mAdapter;

    private SimpleDraweeView mAvatar;

    private ImageRequestConfig mConfig;

    private TextView mIntro;

    private TextView mName;
    
    /* 关注粉丝 */
    private TextView mTvFollowCount, mTvFansCount;

    private FrameContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private TextView mTvConnect;

    private String mUserId;

    private ImageView mUserIdentity;

    /* 返回icon */
    private ImageView mIvFrameBack;

    /* 关注 */
    private ImageView mIvFocus;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        return new Header.Builder(this, headerContainer).build();
    }

    @Override
    protected View onCreateView() {
        hideTitleHeader();
        View view = View.inflate(this, R.layout.activity_frame, null);
        mTvConnect = (TextView) view.findViewById(R.id.frame_communication);
        mRecyclerView = ((RecyclerView)view.findViewById(R.id.frame_recycler));
        mRecyclerView
                .setLayoutManager(new HeaderFooterGridLayoutManager(this, 3, true, false));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new FrameAdapter(this);

        View headerView = View.inflate(this, R.layout.frame_header, null);
        mIvFrameBack = (ImageView) headerView.findViewById(R.id.frame_back_icon);
        mIvFocus = (ImageView) headerView.findViewById(R.id.frame_focus_icon);
        mAvatar = ((SimpleDraweeView)headerView.findViewById(R.id.frame_user_avatar));
        mName = ((TextView)headerView.findViewById(R.id.frame_user_name));
        mIntro = ((TextView)headerView.findViewById(R.id.frame_user_desc));
        mTvFollowCount = ((TextView)headerView.findViewById(R.id.tv_follow_count));
        mTvFansCount = ((TextView)headerView.findViewById(R.id.tv_fans_count));
        mUserIdentity = ((ImageView)headerView.findViewById(R.id.frame_user_identity));
        mAdapter.addHeaderView(headerView);

        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new FramePresenter(this);
        return view;
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        if (intent == null) {
            return;
        }
        mUserId = intent.getStringExtra(FrameConfig.USER_ID);
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.getUserInfo(mUserId);
        mPresenter.obtainUserWorks(mUserId);
        mPresenter.getFansFollowCount(mUserId);
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

        //关注
        mIvFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.focusUser(mUserId);
            }
        });
        //跳转关注列表
        mTvFollowCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openFocus(mUserId);
            }
        });

        //跳转粉丝列表
        mTvFansCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openFans(mUserId);
            }
        });

        mAdapter.setWorkListener(new FrameAdapter.OnWorkListener() {
            @Override
            public void openProduct(String userId, String workId) {
                mPresenter.openProduct(userId, workId);
            }
        });

        mTvConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openChat(mUserId);
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void setPresenter(FrameContract.Presenter presenter) {
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
        }else{
            mUserIdentity.setBackgroundResource(R.drawable.customer_icon);
        }
        //TODO
        mTvConnect.setVisibility(TextUtils.equals(userInfo.getUserId(),
                SharePreferenceUtils.getInstance().getUserId()) ? View.GONE : View.VISIBLE);
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
        mTvFollowCount
                .setText(String.format(getResources().getString(R.string.focus), followCount+""));
        mTvFansCount
                .setText(String.format(getResources().getString(R.string.fans), fansCount+""));
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
