
package com.xxyp.xxyp.login.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.login.contract.LoginContract;
import com.xxyp.xxyp.login.presenter.LoginPresenter;
import com.xxyp.xxyp.main.view.MainActivity;

/**
 * Description : 登陆activity Created by sunpengfei on 2017/7/31. Person in charge
 * : sunpengfei
 */
public class LoginActivity extends BaseTitleActivity implements LoginContract.View {

    private ImageView mIvQQ;

    private ImageView mIvWeChat;

    private ImageView mIvWeiBo;

    private LoginContract.Presenter mPresenter;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder localBuilder = new Header.Builder(this, headerContainer);
        localBuilder.setTitle(R.string.login);
        return localBuilder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.activity_login, null);
        mIvWeChat = ((ImageView)view.findViewById(R.id.iv_weChat));
        mIvQQ = ((ImageView)view.findViewById(R.id.iv_qq));
        mIvWeiBo = ((ImageView)view.findViewById(R.id.iv_weiBo));
        mPresenter = new LoginPresenter(this);
        return view;
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.checkIsLogin();
    }

    @Override
    protected void setViewListener() {
        mIvWeChat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                mPresenter.login(SHARE_MEDIA.WEIXIN);
                Intent intent = new Intent(getContext(),
                        MainActivity.class);
                getContext().startActivity(intent);
                finish();
            }
        });
        mIvQQ.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mPresenter.login(SHARE_MEDIA.QQ);
            }
        });
        mIvWeiBo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mPresenter.login(SHARE_MEDIA.SINA);
            }
        });
    }

    @Override
    public void showLoginLoading(boolean cancelable) {
        showLoadingDialog(cancelable);
    }

    @Override
    public void cancelLoginLoading() {
        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    public void setPresenter(LoginContract.Presenter presenter) {
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroyPresenter();
            mPresenter = null;
        }
        setNull(mIvWeChat);
        setNull(mIvQQ);
        setNull(mIvWeiBo);
    }
}
