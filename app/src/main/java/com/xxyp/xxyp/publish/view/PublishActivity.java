
package com.xxyp.xxyp.publish.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.utils.amap.GpsBean;
import com.xxyp.xxyp.common.utils.amap.LocationChangeListener;
import com.xxyp.xxyp.common.utils.amap.LocationUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.recyclerView.layoutManager.HeaderFooterGridLayoutManager;
import com.xxyp.xxyp.publish.adapter.PublishAdapter;
import com.xxyp.xxyp.publish.contract.PublishContract;
import com.xxyp.xxyp.publish.presenter.PublishPresenter;
import com.xxyp.xxyp.publish.utils.PublishConfig;

import java.util.List;

/**
 * Description : 发布view Created by sunpengfei on 2017/8/2. Person in charge :
 * sunpengfei
 */
public class PublishActivity extends BaseTitleActivity implements PublishContract.View {

    /* 发布标题 */
    private EditText mEtPublishTitle;

    /* 发布描述 */
    private EditText mEtPublishDes;

    private PublishContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    /* 发布约拍额外信息 */
    private PublishChooseView mPublishChoose;

    /* 发布按钮 */
    private TextView mTvPublish;

    private PublishAdapter mAdapter;

    /* 发布类型 */
    private int mPublishType;
    private TextView tvPbulishHint;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setTitle(R.string.publish_shot);
        builder.setBackIcon(R.drawable.header_back_icon, new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        return builder.build();
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        if (intent != null) {
            mPublishType = intent.getIntExtra(PublishConfig.PUBLISH_TYPE, 1);
        }
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.activity_publish, null);
        mRecyclerView = ((RecyclerView) view.findViewById(R.id.publish_recycler));
        mAdapter = new PublishAdapter(this);

        View headerView = View.inflate(this, R.layout.publish_header, null);
        mEtPublishTitle = ((EditText) headerView.findViewById(R.id.et_publish_title));
        mEtPublishDes = ((EditText) headerView.findViewById(R.id.et_publish_des));
        mAdapter.addHeaderView(headerView);

        View footerView = View.inflate(this, R.layout.publish_footer, null);
        mTvPublish = ((TextView) footerView.findViewById(R.id.tv_publish));
        tvPbulishHint = ((TextView) footerView.findViewById(R.id.tv_publish_hint));
        mPublishChoose = (PublishChooseView) footerView.findViewById(R.id.publish_choose_extra);
        mAdapter.addFooterView(footerView);

        mRecyclerView.setLayoutManager(new HeaderFooterGridLayoutManager(this, 3, true, true));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new PublishPresenter(this);
        initPublishView();
        return view;
    }

    /**
     * 初始化发布view
     */
    private void initPublishView() {
        if (mPublishType == PublishConfig.PublishType.PUBLISH_SHOT) {
            //发布约拍
            mHeader.setTitle(R.string.publish_shot);
            mTvPublish.setBackgroundResource(R.drawable.publish_red_bg);
            mEtPublishDes.setVisibility(View.GONE);
            mPublishChoose.setVisibility(View.VISIBLE);
            mEtPublishTitle.setHint(R.string.publish_shot_desc_hint);
            tvPbulishHint.setText(R.string.publish_shot_intro);
        } else {
            //上传作品
            mHeader.setTitle(R.string.upload_work);
            mTvPublish.setBackgroundResource(R.drawable.publish_green_bg);
            mEtPublishDes.setVisibility(View.VISIBLE);
            mPublishChoose.setVisibility(View.GONE);
            mEtPublishTitle.setHint(R.string.upload_work_title_hint);
            mEtPublishDes.setHint(R.string.upload_work_desc_hint);
            new LocationUtils().startLocation(this, new LocationChangeListener() {
                @Override
                public void mapLocation(GpsBean var1, int var2) {
                    tvPbulishHint.setText(var1.getCity() + "." + var1.getDistrict());
                }
            }, -1);

        }
    }

    @Override
    protected void setViewListener() {
        mEtPublishTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                requestEditTextFocus(mEtPublishTitle);
                return false;
            }
        });
        mEtPublishTitle.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        mEtPublishTitle.setCursorVisible(hasFocus);
                    }
                });
        mEtPublishDes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                requestEditTextFocus(mEtPublishDes);
                return false;
            }
        });
        mEtPublishDes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEtPublishDes.setCursorVisible(hasFocus);
            }
        });
        mAdapter.setOnPublishListener(new PublishAdapter.OnPublishPicListener() {
            public void addPicListener() {
                if (mPresenter == null || mAdapter == null) {
                    return;
                }
                mPresenter.onChoosePic(mAdapter.getPicsSize());
            }

            @Override
            public void deletePicListener(String pic) {
                mAdapter.remove(pic);
            }
        });
        mTvPublish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mPresenter == null) {
                    return;
                }
                if (mPublishType == PublishConfig.PublishType.PUBLISH_WORK) {
                    mPresenter.uploadWorks(mAdapter.getPics(),
                            mEtPublishTitle.getText().toString(),
                            mEtPublishDes.getText().toString(), "");
                } else if (mPublishType == PublishConfig.PublishType.PUBLISH_SHOT) {
                    //发布约拍
                    String purpose = mPublishChoose.getPurpose();
                    String payMethod = mPublishChoose.getPayMethod();
                    long time = mPublishChoose.getTime();
                    String address = mPublishChoose.getAddress();
                    mPresenter.uploadDatingShot(mAdapter.getPics(),
                            mEtPublishTitle.getText().toString(), purpose, payMethod, time, address);
                }
            }
        });
        //发布约拍监听
        mPublishChoose.setOnPublishChooseListener(new PublishChooseView.OnPublishChooseListener() {
            @Override
            public void onSetTimeListener() {

            }

            @Override
            public void onSetLocationListener() {
                if (mPresenter != null) {
                    mPresenter.onChooseLocation();
                }
            }
        });
    }

    private void requestEditTextFocus(EditText editText) {
        if (editText == null) {
            return;
        }
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.requestFocus();
    }

    public void showPublishDialog() {
        showLoadingDialog(true);
    }

    @Override
    public void cancelPublishDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void showChoosePic(List<String> pics) {
        mAdapter.setPics(pics);
    }

    @Override
    public void showLocation(String address) {
        mPublishChoose.setAddress(address);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroyPresenter();
            mPresenter = null;
        }
        setNull(mRecyclerView);
        setNull(mTvPublish);
        setNull(mAdapter);
        setNull(mEtPublishTitle);
        setNull(mEtPublishDes);
        super.onDestroy();
    }

    @Override
    public void setPresenter(PublishContract.Presenter presenter) {
    }
}
