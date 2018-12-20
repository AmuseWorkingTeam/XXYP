
package com.xxyp.xxyp.main.presenter;

import android.app.Activity;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.find.provider.FindProvider;
import com.xxyp.xxyp.main.bean.WorkBean;
import com.xxyp.xxyp.main.bean.WorkHotListBean;
import com.xxyp.xxyp.main.contract.MainContract;
import com.xxyp.xxyp.main.model.MainModel;
import com.xxyp.xxyp.user.provider.UserProvider;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Description : 首页presenter Created by sunpengfei on 2017/8/1. Person in charge
 * : sunpengfei
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.Model mModel;

    private CompositeSubscription mSubscription;

    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        mView = view;
        mModel = new MainModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void getHotWorks() {
        mView.showHotWorkLoading(true);
        Subscription subscription = mModel.getHotWorks().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WorkHotListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mView != null){
                            mView.cancelHotWorkLoading();
                            mView.resetRefresh();
                            ToastUtil.showTextViewPrompt(R.string.net_error);
                        }
                    }

                    @Override
                    public void onNext(WorkHotListBean workHotListBean) {
                        if(mView == null){
                            return;
                        }
                        mView.cancelHotWorkLoading();
                        mView.resetRefresh();
                        if (workHotListBean == null) {
                            return;
                        }
                        List<WorkBean> workBeanList = workHotListBean.getHotWorks();
                        if (workBeanList == null || workBeanList.size() == 0) {
                            return;
                        }
                        if (workBeanList.size() > 5) {
                            mView.showBannerList(workBeanList.subList(0, 5));
                            mView.showWorkList(workBeanList.subList(5, workBeanList.size()));
                        } else {
                            mView.showBannerList(workBeanList);
                        }
                        WorkBean workBean = workBeanList.get(0);
                        if(workBean != null){
                            //展示头像
                            mView.showBannerUserInfo(workBean);
                        }
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void openFrame(String userId) {
        UserProvider.openFrame((Activity)mView.getContext(), userId);
    }

    @Override
    public void openProduct(String userId, String workId) {
        FindProvider.openProduct((Activity)mView.getContext(), userId, workId);
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
        if(mSubscription != null){
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }
}
