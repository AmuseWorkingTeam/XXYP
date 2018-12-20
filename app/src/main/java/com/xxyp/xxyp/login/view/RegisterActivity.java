
package com.xxyp.xxyp.login.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.login.contract.RegisterContract;
import com.xxyp.xxyp.login.presenter.RegisterPresenter;
import com.xxyp.xxyp.login.utils.UserConfig;

/**
 * Description : 注册页面 Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class RegisterActivity extends BaseTitleActivity implements RegisterContract.View {

    private ImageRequestConfig mConfig;

    private EditText mDescEt;

    private EditText mEmailEt;

    private SimpleDraweeView mIvAvatar;

    private ImageView mIvChooseAvatar;

    private EditText mNameEt;

    private EditText mPhoneEt;

    private RegisterContract.Presenter mPresenter;

    private RelativeLayout mRlAvatar;

    private UserInfo mUserInfo;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setBackIcon(R.drawable.header_back_icon, new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        builder.setRightIcon(R.drawable.header_confirm_icon, new View.OnClickListener() {
            public void onClick(View view) {
                mPresenter.createUser();
            }
        });
        builder.setTitle(R.string.register);
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.activity_register, null);
        mRlAvatar = ((RelativeLayout)view.findViewById(R.id.rl_avatar));
        mIvChooseAvatar = ((ImageView)view.findViewById(R.id.iv_choose_avatar));
        mIvAvatar = ((SimpleDraweeView)view.findViewById(R.id.iv_avatar));
        mNameEt = ((EditText)view.findViewById(R.id.et_input_name));
        mDescEt = ((EditText)view.findViewById(R.id.et_input_desc));
        mPhoneEt = ((EditText)view.findViewById(R.id.et_input_phone));
        mEmailEt = ((EditText)view.findViewById(R.id.et_input_email));
        mConfig = new ImageRequestConfig.Builder().build();
        mPresenter = new RegisterPresenter(this);
        return view;
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        if (intent == null) {
            return;
        }
        mUserInfo = ((UserInfo)intent.getSerializableExtra(UserConfig.USER_INFO));
    }

    @Override
    protected void setViewListener() {
        mRlAvatar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mPresenter.onGoGallery();
            }
        });
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.showUserInfo(mUserInfo);
    }

    @Override
    public String getDesc() {
        if (mDescEt != null) {
            return mDescEt.getText().toString();
        }
        return null;
    }

    @Override
    public String getEmail() {
        if (mEmailEt != null) {
            return mEmailEt.getText().toString();
        }
        return null;
    }

    @Override
    public String getNick() {
        if (mNameEt != null) {
            return mNameEt.getText().toString();
        }
        return null;
    }

    @Override
    public String getPhone() {
        if (mPhoneEt != null) {
            return mPhoneEt.getText().toString();
        }
        return null;
    }

    @Override
    public void showAvatar(String avatar) {
        mIvChooseAvatar.setVisibility(View.GONE);
        mIvAvatar.setVisibility(View.VISIBLE);
        ImageLoader.getInstance().display(mIvAvatar, avatar, mConfig);
    }

    @Override
    public void showName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mNameEt.setText(name);
        }
    }

    @Override
    public void showRegisterLoading(boolean cancelable) {
        showLoadingDialog(cancelable);
    }

    @Override
    public void cancelRegisterLoading() {
        dismissLoadingDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroyPresenter();
        setNull(mPresenter);
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
    }

    @Override
    public Context getContext() {
        return this;
    }
}
