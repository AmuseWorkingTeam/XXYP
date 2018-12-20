
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
import com.xxyp.xxyp.common.utils.TimeUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.recyclerView.layoutManager.HeaderFooterGridLayoutManager;
import com.xxyp.xxyp.find.adapter.WorkDetailAdapter;
import com.xxyp.xxyp.find.contract.ProductDetailContract;
import com.xxyp.xxyp.find.presenter.ProductDetailPresenter;
import com.xxyp.xxyp.find.utils.FindConfig;
import com.xxyp.xxyp.main.bean.WorkBean;

/**
 * Description : 作品详情页面 Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class ProductDetailActivity extends BaseTitleActivity implements ProductDetailContract.View {

    protected RecyclerView mRecyclerView;

    private WorkDetailAdapter mAdapter;

    private String mUserId;

    private String mWorkId;
    
    /* 作品标题  描述 */
    private TextView mWorkTitle, mWorkDesc;

    private RelativeLayout mUserContainer;
    
    /* 用户信息 */
    private TextView mTvUserName, mTvUserAddress, mTvUserTime;
    
    private SimpleDraweeView mUserAvatar;
    
    private ImageRequestConfig mConfig;

    /* 作品详情presenter */
    private ProductDetailContract.Presenter mPresenter;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setTitle("");
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
        view.findViewById(R.id.frame_communication).setVisibility(View.GONE);

        mRecyclerView.setLayoutManager(new HeaderFooterGridLayoutManager(this, 3, true, true));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new WorkDetailAdapter(this);

        View headerView = View.inflate(this, R.layout.item_user_view, null);
        mUserContainer = (RelativeLayout) headerView.findViewById(R.id.user_container);
        mTvUserName = (TextView) headerView.findViewById(R.id.user_name);
        mTvUserAddress = (TextView) headerView.findViewById(R.id.user_location);
        mTvUserTime = (TextView) headerView.findViewById(R.id.user_time);
        mUserAvatar = (SimpleDraweeView)headerView.findViewById(R.id.user_avatar);
        mAdapter.addHeaderView(headerView);

        View footerView = View.inflate(this, R.layout.product_detail_footer_view, null);
        mWorkTitle = (TextView) footerView.findViewById(R.id.tv_product_title);
        mWorkDesc = (TextView) footerView.findViewById(R.id.tv_product_desc);
        mAdapter.addFooterView(footerView);

        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new ProductDetailPresenter(this);
        return view;
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        if(intent == null){
            return;
        }
        mUserId = intent.getStringExtra(FindConfig.USER_ID);
        mWorkId = intent.getStringExtra(FindConfig.WORK_ID);
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.obtainWorks(mUserId, mWorkId);
    }

    @Override
    protected void setViewListener() {
        mUserContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openFrame(mUserId);
            }
        });
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.openWorkPhotoDetail(position, mUserId, mAdapter.getData());
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setPresenter(ProductDetailContract.Presenter presenter) {

    }


    @Override
    public void showWork(WorkBean workBean) {
        if(workBean == null){
            return;
        }
        mAdapter.setData(workBean.getList());
        //设置标题
        String title = workBean.getWorksTitle();
        mHeader.setTitle(!TextUtils.isEmpty(title) ? title : "");
        mWorkTitle.setText(!TextUtils.isEmpty(title) ? title : "");
        mWorkDesc.setText(!TextUtils.isEmpty(workBean.getWorksIntroduction())
                ? workBean.getWorksIntroduction() : "");
        showUserInfo(workBean.getUserName(), workBean.getUserImage(), workBean.getWorksAddress(),
                workBean.getReleaseTime());
    }

    /**
     * 展示用户信息
     * @param userName   名称
     * @param userAvatar 头像
     * @param userAddress 作品位置
     * @param userTime    作品时间
     */
    @Override
    public void showUserInfo(String userName, String userAvatar, String userAddress,
            long userTime) {
        mTvUserName.setText(!TextUtils.isEmpty(userName) ? userName : "");
        mTvUserAddress.setText(!TextUtils.isEmpty(userAddress) ? userAddress : "");
        mTvUserTime.setText(TimeUtils.millis2String(userTime));
        if(mConfig == null){
            mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(44))
                    .setLoadHeight(ScreenUtils.dp2px(44)).setRender(true).build();
        }
        ImageLoader.getInstance().display(mUserAvatar, userAvatar, mConfig);
    }

    @Override
    public void showProductLoading(boolean cancelable) {
        showLoadingDialog(cancelable);
    }

    @Override
    public void cancelProductLoading() {
        dismissLoadingDialog();
    }
}
