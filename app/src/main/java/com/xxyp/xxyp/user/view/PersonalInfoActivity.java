
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

/**
 * Description : 用户信息页面 Created by sunpengfei on 2017/8/3. Person in charge :
 * sunpengfei
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
        View view = View.inflate(this, R.layout.activity_personal_setting, null);
        mIvAvatar = (ShapeImageView) view.findViewById(R.id.iv_avatar);
        mIvSetHeadImage = (ImageView) view.findViewById(R.id.iv_set_head_image);
        mEtName = (EditText) view.findViewById(R.id.et_set_name);
        mEtAddress = (TextView) view.findViewById(R.id.et_set_address);
        mEtIntroduction = (EditText) view.findViewById(R.id.et_set_desc);
        mTvConfirm = (TextView) view.findViewById(R.id.tv_set_confirm);
        mPresenter = new PersonalInfoPresenter(this);
        return view;
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
                mPresenter.updateUserInfo(name, address, intro);
            }
        });
        //跳转位置
        mEtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onChooseLocation();
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
        if (mConfig == null) {
            mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(104))
                    .setLoadHeight(ScreenUtils.dp2px(104)).build();
        }
        ImageLoader.getInstance().display(mIvAvatar, userInfo.getUserImage(),
                mConfig);
        mEtName.setHint(userInfo.getUserName());
        mEtIntroduction.setHint(userInfo.getUserIntroduction());
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
