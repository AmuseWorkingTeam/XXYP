package com.xxyp.xxyp.user.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.XXApplication;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.db.YuePaiDB;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuManager;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuUploadCallback;
import com.xxyp.xxyp.login.view.LoginActivity;
import com.xxyp.xxyp.user.contract.MyDatingShotContract;
import com.xxyp.xxyp.user.contract.SettingContract;
import com.xxyp.xxyp.user.model.MyDatingShotModel;
import com.xxyp.xxyp.user.model.PersonalModel;
import com.xxyp.xxyp.user.provider.UserProvider;

import java.util.Random;

import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Description : 我的约拍presenter
 * Created by sunpengfei on 2017/8/11.
 * Job number：135182
 * Phone ：18510428121
 * Email：sunpengfei@syswin.com
 * Person in charge : sunpengfei
 * Leader：wangxiaohui
 */
public class SettingPresenter implements SettingContract.Presenter {

    private SettingContract.View mView;

    private SettingContract.Model mModel;

    private CompositeSubscription mSubscription;


    public SettingPresenter(SettingContract.View view) {
        mView = view;
        mModel = new PersonalModel();
        mSubscription = new CompositeSubscription();

    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
        mModel = null;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    @Override
    public void logout() {
        mView.showLoadingDialog(false);
        Subscription subscription = mModel.logout().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.dismissLoadingDialog();
                            ToastUtil.showTextViewPrompt(R.string.net_error);
                        }
                    }

                    @Override
                    public void onNext(Object result) {
                        if (mView == null) {
                            return;
                        }
                        // TODO: 2019/1/8 判断退出状态
                        mView.dismissLoadingDialog();
                        ToastUtil.showTextViewPrompt(mView.getContext(), "注销成功！");
                        SharePreferenceUtils.getInstance().putUserId("");
                        SharePreferenceUtils.getInstance().putToken("");
                        SharePreferenceUtils.getInstance().putLoginStatus(false);
                        YuePaiDB.getInstance().close();
                        Activity activity = (Activity) mView.getContext();
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void clearData() {
        mView.showLoadingDialog(true);
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    Thread.sleep(new Random().nextInt(3) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(new Object());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                mView.dismissLoadingDialog();
                ToastUtil.showTextViewPrompt(mView.getContext(), "数据清除成功！");
            }
        });
    }
}
