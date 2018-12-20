
package com.xxyp.xxyp.find.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.find.contract.AppointContract;
import com.xxyp.xxyp.find.provider.FindProvider;
import com.xxyp.xxyp.user.bean.UserShotListBean;
import com.xxyp.xxyp.user.provider.UserProvider;
import com.xxyp.xxyp.user.service.UserServiceManager;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description : 发现约拍presenter
 * Created by sunpengfei on 2017/8/21.
 * Person in charge : sunpengfei
 */
public class AppointPresenter implements AppointContract.Presenter {

    private AppointContract.View mView;

    public AppointPresenter(AppointContract.View view){
        mView = view;
    }

    @Override
    public void obtainShotList() {
        mView.showShotLoading(true);
        UserServiceManager.getDatingShot(null, null).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UserShotListBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(mView != null){
                    mView.cancelShotLoading();
                    mView.resetRefresh();
                    ToastUtil.showTextViewPrompt(R.string.net_error);
                }
            }

            @Override
            public void onNext(UserShotListBean userShotListBean) {
                if(mView == null){
                    return;
                }
                mView.cancelShotLoading();
                mView.resetRefresh();
                if(userShotListBean != null){
                    mView.showShotList(userShotListBean.getDatingShot());
                }
            }
        });
    }

    @Override
    public void openFrame(String userId) {
        UserProvider.openFrame((Activity) mView.getContext(), userId);
    }

    @Override
    public void openShotDetail(String userId, String shotId) {
        if(TextUtils.isEmpty(userId)){
            return;
        }
        FindProvider.openShot((Activity)mView.getContext(), userId, shotId);
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
    }
}
