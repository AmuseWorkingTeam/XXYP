package com.xxyp.xxyp.user.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.user.bean.FansFocusBean;
import com.xxyp.xxyp.user.contract.TotalFansFocusContract;
import com.xxyp.xxyp.user.model.TotalFansFocusModel;
import com.xxyp.xxyp.user.provider.UserProvider;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Description : （类描述，描述当前类具体作用和功能）
 * Created by sunpengfei on 2017/10/19.
 * Job number：135182
 * Phone ：18510428121
 * Email：sunpengfei@syswin.com
 * Person in charge : sunpengfei
 * Leader：wangxiaohui
 */
public class TotalFansFocusPresenter implements TotalFansFocusContract.Presenter {

    private TotalFansFocusContract.View mView;

    private TotalFansFocusContract.Model mModel;

    private CompositeSubscription mSubscription;

    public TotalFansFocusPresenter(TotalFansFocusContract.View view){
        mView = view;
        mModel = new TotalFansFocusModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void obtainTotalFans(String userId) {
        if(TextUtils.isEmpty(userId)){
            return;
        }
        mView.showFansFocusDialog(true);
        Subscription subscription = Observable.just(userId).flatMap(new Func1<String, Observable<List<FansFocusBean>>>() {
            @Override
            public Observable<List<FansFocusBean>> call(String s) {
                return mModel.obtainTotalFans(s);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<FansFocusBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(mView != null){
                    mView.cancelFansFocusDialog();
                    ToastUtil.showTextViewPrompt(R.string.net_error);
                }
            }

            @Override
            public void onNext(List<FansFocusBean> fansFocusBeen) {
                if(mView == null){
                    return;
                }
                mView.cancelFansFocusDialog();
                mView.showFansFocus(fansFocusBeen);
            }
        });
        mSubscription.add(subscription);
    }

    @Override
    public void obtainTotalFocus(String userId) {
        if(TextUtils.isEmpty(userId)){
            return;
        }
        mView.showFansFocusDialog(true);
        Subscription subscription = Observable.just(userId).flatMap(new Func1<String, Observable<List<FansFocusBean>>>() {
            @Override
            public Observable<List<FansFocusBean>> call(String s) {
                return mModel.obtainTotalFocus(s);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<FansFocusBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(mView != null){
                    mView.cancelFansFocusDialog();
                    ToastUtil.showTextViewPrompt(R.string.net_error);
                }
            }

            @Override
            public void onNext(List<FansFocusBean> fansFocusBeen) {
                if(mView == null){
                    return;
                }
                mView.cancelFansFocusDialog();
                mView.showFansFocus(fansFocusBeen);
            }
        });
        mSubscription.add(subscription);
    }

    @Override
    public void openFrame(String userId) {
        UserProvider.openFrame((Activity) mView.getContext(), userId);
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
        mModel = null;
        if(mSubscription != null){
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }
}
