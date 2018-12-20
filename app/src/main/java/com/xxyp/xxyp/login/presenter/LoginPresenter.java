
package com.xxyp.xxyp.login.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.net.ErrorCodeConfig;
import com.xxyp.xxyp.common.net.RxError;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.login.contract.LoginContract;
import com.xxyp.xxyp.login.model.LoginModel;
import com.xxyp.xxyp.login.provider.RegisterProvider;
import com.xxyp.xxyp.main.view.MainActivity;

import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Description : 登陆presenter Created by sunpengfei on 2017/7/31. Person in
 * charge : sunpengfei
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.Model mModel;

    private CompositeSubscription mSubscription;

    private LoginContract.View mView;

    /* 跳转注册request */
    private final int REGISTER_REQUEST = 1000;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
        mModel = new LoginModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void checkIsLogin() {
        String userId = SharePreferenceUtils.getInstance().getUserId();
        boolean isLogin = SharePreferenceUtils.getInstance().getLoginStatus();
        //如果本地已经有用户数据 并且已经登陆过  则直接跳转首页
        if(!TextUtils.isEmpty(userId) && isLogin){
            openMainActivity();
        }
    }

    @Override
    public void login(final SHARE_MEDIA shareMedia) {
        UMShareAPI.get(mView.getContext()).getPlatformInfo((Activity) mView.getContext(), shareMedia, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if(mView != null){
                    mView.showLoginLoading(true);
                }
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if(mView != null){
                    mView.cancelLoginLoading();
                }
                login(shareMedia, map);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if(mView != null){
                    mView.cancelLoginLoading();
                    ToastUtil.showTextViewPrompt(R.string.net_error);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if(mView != null){
                    mView.cancelLoginLoading();
                }
            }
        });
    }

    /**
     * 第三方获取到数据后登陆
     * @param shareMedia   第三方类型
     * @param paramMap     数据
     */
    private void login(SHARE_MEDIA shareMedia, Map<String, String> paramMap) {
        String userId = SharePreferenceUtils.getInstance().getUserId();
        final UserInfo userInfo = buildUserInfo(shareMedia, paramMap);
        //本地无数据  则直接跳转注册
        if (TextUtils.isEmpty(userId)) {
            RegisterProvider.openRegisterActivity((Activity)mView.getContext(), userInfo, REGISTER_REQUEST);
            return;
        }
        mView.showLoginLoading(true);
        final Subscription subscription = mModel
                .login(userInfo.getUserName(), userInfo.getUserSource() + "",
                        userInfo.getUserSourceId())
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mView == null){
                            return;
                        }
                        mView.cancelLoginLoading();
                        int errorCode = 0;
                        if (e instanceof RxError) {
                            errorCode = ((RxError) e).errorCode;
                        }
                        if (errorCode == ErrorCodeConfig.USER_NOT_EXIST) {
                            // 用户不存在 直接跳到注册页面 可能场景是在切换账号或者切换第三方工具(如微信切换到微博)
                            RegisterProvider.openRegisterActivity((Activity)mView.getContext(),
                                    userInfo, REGISTER_REQUEST);
                        } else {
                            ToastUtil.showTextViewPrompt(R.string.net_error);
                        }
                    }

                    @Override
                    public void onNext(Object o) {
                        if (mView == null) {
                            return;
                        }
                        mView.cancelLoginLoading();
                        openMainActivity();
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTER_REQUEST && resultCode == Activity.RESULT_OK){
            //注册成功直接跳转首页
            openMainActivity();
        }
    }

    /**
     * 构造用户信息
     * @param shareMedia
     * @param map
     * @return UserInfo
     */
    private UserInfo buildUserInfo(SHARE_MEDIA shareMedia, Map<String, String> map) {
        if (shareMedia == null || map == null || map.size() == 0) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        if (shareMedia == SHARE_MEDIA.WEIXIN) {
            userInfo.setUserSource(1);
        } else if (shareMedia == SHARE_MEDIA.SINA) {
            userInfo.setUserSource(2);
        } else if (shareMedia == SHARE_MEDIA.QQ) {
            userInfo.setUserSource(3);
        }
        userInfo.setUserSourceId(map.get("uid"));
        userInfo.setUserName(map.get("name"));
        userInfo.setUserImage(map.get("iconurl"));
        return userInfo;
    }

    /**
     * 跳转首页
     */
    private void openMainActivity(){
        Intent intent = new Intent(mView.getContext(),
                MainActivity.class);
        mView.getContext().startActivity(intent);
        ((Activity)mView.getContext()).finish();
    }

    @Override
    public void onDestroyPresenter() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
        mView = null;
        mModel = null;
    }
}
