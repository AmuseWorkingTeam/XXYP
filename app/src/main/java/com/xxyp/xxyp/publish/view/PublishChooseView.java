
package com.xxyp.xxyp.publish.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.publish.utils.PublishConfig;
import com.xxyp.xxyp.user.utils.DateCheckListener;
import com.xxyp.xxyp.user.utils.IWheelDataChangeCallback;
import com.xxyp.xxyp.user.utils.TimeCheckListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Description : 发布选择额外信息 Created by sunpengfei on 2017/8/9. Person in charge :
 * sunpengfei
 */
public class PublishChooseView extends LinearLayout {

    /* 约摄影师 约模特 */
    private TextView mFindCamera, mFindModel;

    /* 约摄影师 约模特 */
    private View mSelectCamera, mSelectModel;

    /* 付款方式 */
    private TextView mGather, mFree, mPay;

    /* 付款方式 */
    private View mSelectGather, mSelectFree, mSelectPay;

    private TextView mTvTime, mTvAddress;

    private String mPurpose = PublishConfig.ShotPurpose.FIND_CAMERA;

    private String mPayMethod = PublishConfig.ShotPayMethod.FREE;

    private long mTime = 0;

    private String mAddress;

    private OnPublishChooseListener mListener;

    private Context context;
    private TextView tvDate;

    private String date;

    private String time;

    public PublishChooseView(Context context) {
        this(context, null);
    }

    public PublishChooseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PublishChooseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        View view = inflate(context, R.layout.publish_camera_view, this);
        mFindCamera = (TextView) view.findViewById(R.id.find_camera);
        mSelectCamera = view.findViewById(R.id.choose_camera);
        mFindModel = (TextView) view.findViewById(R.id.find_model);
        mSelectModel = view.findViewById(R.id.choose_model);
        mGather = (TextView) view.findViewById(R.id.tv_gather);
        mSelectGather = view.findViewById(R.id.choose_gather);
        mFree = (TextView) view.findViewById(R.id.tv_free);
        mSelectFree = view.findViewById(R.id.choose_free);
        mPay = (TextView) view.findViewById(R.id.tv_pay);
        mSelectPay = view.findViewById(R.id.choose_pay);

        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        mTvAddress = (TextView) view.findViewById(R.id.tv_address);
        setViewListener();
    }

    private void setViewListener() {
        mFindCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFind(PublishConfig.ShotPurpose.FIND_CAMERA);
            }
        });
        mFindModel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFind(PublishConfig.ShotPurpose.FIND_MODEL);
            }
        });
        mGather.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePay(PublishConfig.ShotPayMethod.GATHER);
            }
        });
        mFree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePay(PublishConfig.ShotPayMethod.FREE);
            }
        });
        mPay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePay(PublishConfig.ShotPayMethod.PAY);
            }
        });
        //日期
        tvDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateCheckListener(context).handleDateCheck(PublishChooseView.this, tvDate,
                        tvDate.getText().toString(), new IWheelDataChangeCallback() {
                            @Override
                            public void wheelDataChangeCallback(String wheelData) {
                                date = wheelData;
                                convertDateTime();
                            }
                        });
            }
        });
        //时间
        mTvTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimeCheckListener(context).handleTimeCheck(PublishChooseView.this, mTvTime,
                        mTvTime.getText().toString(), new IWheelDataChangeCallback() {
                            @Override
                            public void wheelDataChangeCallback(String wheelData) {
                                time = wheelData;
                                convertDateTime();
                            }
                        });
            }
        });
        //跳转位置
        mTvAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSetLocationListener();
                }
            }
        });
    }

    private void convertDateTime() {
        if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(time)) {
            try {
                mTime = new SimpleDateFormat("yyyy-MM-ddHH:mm").parse(date + time).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置监听
     *
     * @param listener 监听
     */
    public void setOnPublishChooseListener(OnPublishChooseListener listener) {
        mListener = listener;
    }

    /**
     * 选择约拍目的
     *
     * @param purpose 目的
     */
    private void chooseFind(String purpose) {
        mPurpose = purpose;
        switch (purpose) {
            case PublishConfig.ShotPurpose.FIND_CAMERA:
                // 选择摄影师
                mSelectCamera.setVisibility(VISIBLE);
                mSelectModel.setVisibility(INVISIBLE);
                break;
            case PublishConfig.ShotPurpose.FIND_MODEL:
                // 选择约拍
                mSelectCamera.setVisibility(INVISIBLE);
                mSelectModel.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 选择付款类型
     *
     * @param payMethod 类型
     */
    private void choosePay(String payMethod) {
        mPayMethod = payMethod;
        switch (payMethod) {
            case PublishConfig.ShotPayMethod.GATHER:
                // 收款
                mSelectGather.setVisibility(VISIBLE);
                mSelectFree.setVisibility(INVISIBLE);
                mSelectPay.setVisibility(INVISIBLE);
                break;
            case PublishConfig.ShotPayMethod.FREE:
                // 免费
                mSelectGather.setVisibility(INVISIBLE);
                mSelectFree.setVisibility(VISIBLE);
                mSelectPay.setVisibility(INVISIBLE);
                break;
            case PublishConfig.ShotPayMethod.PAY:
                // 付款
                mSelectGather.setVisibility(INVISIBLE);
                mSelectFree.setVisibility(INVISIBLE);
                mSelectPay.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        mAddress = address;
        mTvAddress.setText(!TextUtils.isEmpty(address) ? address : "");
    }

    /**
     * 获取约拍目的
     *
     * @return String
     */
    public String getPurpose() {
        return mPurpose;
    }

    /**
     * 获取约拍付款类型
     *
     * @return String
     */
    public String getPayMethod() {
        return mPayMethod;
    }

    public long getTime() {
        return mTime;
    }

    public String getAddress() {
        return mAddress;
    }

    /**
     * 发布监听
     */
    public interface OnPublishChooseListener {

        /**
         * 设置位置
         */
        void onSetLocationListener();
    }
}
