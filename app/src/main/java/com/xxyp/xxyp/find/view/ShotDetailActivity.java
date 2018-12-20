
package com.xxyp.xxyp.find.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.common.utils.TimeUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.recyclerView.layoutManager.HeaderFooterGridLayoutManager;
import com.xxyp.xxyp.find.adapter.ShotDetailAdapter;
import com.xxyp.xxyp.find.contract.ShotDetailContract;
import com.xxyp.xxyp.find.presenter.ShotDetailPresenter;
import com.xxyp.xxyp.find.utils.FindConfig;
import com.xxyp.xxyp.main.bean.ShotBean;

/**
 * Description : 约拍详情view Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class ShotDetailActivity extends BaseTitleActivity implements ShotDetailContract.View {

    private ShotDetailAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private String mUserId;

    private String mShotId;

    /* 约拍标题  描述 时间 地点 */
    private TextView mShotTitle, mShotDesc, mShotTime, mShotAddress;

    private RelativeLayout mUserContainer;

    /* 用户信息 */
    private TextView mTvUserName, mTvUserAddress, mTvUserTime;

    /* 联系 */
    private TextView mTvCommunication;

    private SimpleDraweeView mUserAvatar;

    private ImageRequestConfig mConfig;

    private ShotDetailContract.Presenter mPresenter;

    @Override
    protected Header onCreateHeader(RelativeLayout container) {
        Header.Builder builder = new Header.Builder(this, container);
        builder.setTitle(R.string.shot);
        builder.setBackIcon(R.drawable.header_back_icon, new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.activity_frame, null);
        mRecyclerView = ((RecyclerView)view.findViewById(R.id.frame_recycler));
        mTvCommunication = (TextView) view.findViewById(R.id.frame_communication);
        mTvCommunication.setText("约TA");
        mRecyclerView.setLayoutManager(new HeaderFooterGridLayoutManager(this, 3, true, true));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ShotDetailAdapter(this);

        View headerView = View.inflate(this, R.layout.item_user_view, null);
        mUserContainer = (RelativeLayout) headerView.findViewById(R.id.user_container);
        mTvUserName = (TextView) headerView.findViewById(R.id.user_name);
        mTvUserAddress = (TextView) headerView.findViewById(R.id.user_location);
        mTvUserTime = (TextView) headerView.findViewById(R.id.user_time);
        mUserAvatar = (SimpleDraweeView)headerView.findViewById(R.id.user_avatar);
        mAdapter.addHeaderView(headerView);

        View footerView = View.inflate(this, R.layout.appoint_detail_footer_view, null);
        mShotTitle = (TextView) footerView.findViewById(R.id.tv_shot_title);
        mShotDesc = (TextView) footerView.findViewById(R.id.tv_shot_desc);
        mShotTime = (TextView) footerView.findViewById(R.id.tv_shot_time);
        mShotAddress = (TextView) footerView.findViewById(R.id.tv_shot_address);
        mAdapter.addFooterView(footerView);

        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new ShotDetailPresenter(this);
        return view;
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        if(intent == null){
            return;
        }
        mUserId = intent.getStringExtra(FindConfig.USER_ID);
        mShotId = intent.getStringExtra(FindConfig.SHOT_ID);
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.obtainShot(mUserId, mShotId);
    }

    @Override
    protected void setViewListener() {
        mUserContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openFrame(mUserId);
            }
        });

        mTvCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openChat(mUserId);
            }
        });

        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.openShotPhotoDetail(position, mUserId, mAdapter.getData());
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setPresenter(ShotDetailContract.Presenter presenter) {

    }

    @Override
    public void showShotLoading(boolean cancelable) {
        showLoadingDialog(cancelable);
    }

    @Override
    public void cancelShotLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showShot(ShotBean shotBean) {
        if(shotBean == null){
            return;
        }
        mAdapter.setData(shotBean.getDatingShotImages());
        //设置标题
        String title = shotBean.getDatingShotIntroduction();
        mShotTitle.setText(!TextUtils.isEmpty(title) ? title : "");
        mShotDesc.setText(!TextUtils.isEmpty(shotBean.getDescription())
                ? shotBean.getDescription() : "");
        String time = TimeUtils.millis2String(shotBean.getReleaseTime());
        mShotTime.setText("时间:" + time);
        mShotAddress.setText("地点:" + shotBean.getDatingShotAddress());
        showUserInfo(shotBean.getUserName(), shotBean.getUserImage(), shotBean.getDatingShotAddress(),
                shotBean.getReleaseTime());
    }

    @Override
    public void showUserInfo(String userName, String userAvatar, String userAddress, long userTime) {
        mTvUserName.setText(!TextUtils.isEmpty(userName) ? userName : "");
        mTvUserAddress.setText(!TextUtils.isEmpty(userAddress) ? userAddress : "");
        mTvUserTime.setText(TimeUtils.millis2String(userTime));
        if(mConfig == null){
            mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(44))
                    .setLoadHeight(ScreenUtils.dp2px(44)).setRender(true).build();
        }
        ImageLoader.getInstance().display(mUserAvatar, userAvatar, mConfig);
        // TODO
        mTvCommunication.setVisibility(
                TextUtils.equals(mUserId, SharePreferenceUtils.getInstance().getUserId())
                        ? View.GONE
                        : View.VISIBLE);
    }
}
