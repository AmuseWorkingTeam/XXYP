
package com.xxyp.xxyp.user.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.ShapeImageView;
import com.xxyp.xxyp.user.contract.PersonalInfoContract;
import com.xxyp.xxyp.user.presenter.PersonalInfoPresenter;
import com.xxyp.xxyp.user.utils.CheckListener;
import com.xxyp.xxyp.user.utils.IWheelDataChangeCallback;
import com.xxyp.xxyp.user.utils.SingleCheckListener;

/**
 * Description : 用户信息页面
 */
public class PersonalInfoActivity extends BaseTitleActivity implements PersonalInfoContract.View {

    private EditText mEtName, mEtIntroduction;

    private TextView mEtAddress;

    private ShapeImageView mIvAvatar;

    /* 设置头像 */
    private ImageView mIvSetHeadImage;

    private TextView mTvConfirm;

    private PersonalInfoContract.Presenter mPresenter;

    private ImageRequestConfig mConfig;
    private TextView tvGender;
    private int gender;
    private View rootView;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setTitle(R.string.personal_info);
        builder.setBackIcon(R.drawable.header_back_icon, new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        rootView = View.inflate(this, R.layout.activity_personal_setting, null);
        mIvAvatar = (ShapeImageView) rootView.findViewById(R.id.iv_avatar);
        mIvSetHeadImage = (ImageView) rootView.findViewById(R.id.iv_set_head_image);
        mEtName = (EditText) rootView.findViewById(R.id.et_set_name);
        tvGender = (TextView) rootView.findViewById(R.id.tv_set_gender);
        mEtAddress = (TextView) rootView.findViewById(R.id.et_set_address);
        mEtIntroduction = (EditText) rootView.findViewById(R.id.et_set_desc);
        mTvConfirm = (TextView) rootView.findViewById(R.id.tv_set_confirm);
        mPresenter = new PersonalInfoPresenter(this);
        return rootView;
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        super.initDataFromFront(intent);
        mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(104))
                .setLoadHeight(ScreenUtils.dp2px(104)).build();
    }

    @Override
    protected void setViewListener() {
        //设置头像
        mIvSetHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.setHeadImage();
            }
        });
        //更新userInfo
        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getEditableText().toString().trim();
                String address = mEtAddress.getText().toString();
                String intro = mEtIntroduction.getEditableText().toString().trim();
                mPresenter.updateUserInfo(name, address, intro, gender);
            }
        });
        //跳转位置
        mEtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onChooseLocation();
            }
        });
        tvGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckListener checkListener = new SingleCheckListener(PersonalInfoActivity.this);
                checkListener.handleCheck(rootView, tvGender, new IWheelDataChangeCallback() {
                    @Override
                    public void wheelDataChangeCallback(String wheelData) {
                        if (TextUtils.equals(wheelData, "男")) {
                            gender = 0;
                            tvGender.setText(wheelData);
                        } else if (TextUtils.equals(wheelData, "女")) {
                            gender = 1;
                            tvGender.setText(wheelData);
                        }
                    }
                }, "男", "女");
            }
        });
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.getMyUserInfo();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setPresenter(PersonalInfoContract.Presenter presenter) {

    }

    @Override
    public void showUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        ImageLoader.getInstance().display(mIvAvatar, userInfo.getUserImage(),
                mConfig);
        mEtName.setText(userInfo.getUserName());
        mEtIntroduction.setText(userInfo.getUserIntroduction());
        mEtAddress.setText(userInfo.getAddress());
        tvGender.setText(userInfo.getGender() == 0 ? "男" : "女");
    }

    @Override
    public void showSettingDialog(boolean isCancel) {
        showLoadingDialog(isCancel);
    }

    @Override
    public void cancelSettingDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void setImage(String path) {
        ImageLoader.getInstance().display(mIvAvatar, "file://" + path,
                mConfig);
    }

    @Override
    public void setLocation(String address) {
        mEtAddress.setText(TextUtils.isEmpty(address) ? "" : address);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroyPresenter();
            setNull(mPresenter);
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }
}
