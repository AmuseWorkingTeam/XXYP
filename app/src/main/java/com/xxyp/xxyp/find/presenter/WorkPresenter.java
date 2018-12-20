
package com.xxyp.xxyp.find.presenter;

import android.app.Activity;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.find.contract.WorkContract;
import com.xxyp.xxyp.find.provider.FindProvider;
import com.xxyp.xxyp.user.bean.UserWorkListBean;
import com.xxyp.xxyp.user.provider.UserProvider;
import com.xxyp.xxyp.user.service.UserServiceManager;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description : 发现作品presenter
 * Created by sunpengfei on 2017/8/21.
 * Person in charge : sunpengfei
 */
public class WorkPresenter implements WorkContract.Presenter {

    private WorkContract.View mView;

    public WorkPresenter(WorkContract.View view){
        mView = view;
    }

    @Override
    public void obtainWorkList() {
        mView.showWorkLoading(true);
        UserServiceManager.getWorks(null, null).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserWorkListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mView != null){
                            mView.cancelWorkLoading();
                            mView.resetRefresh();
                            ToastUtil.showTextViewPrompt(R.string.net_error);
                        }
                    }

                    @Override
                    public void onNext(UserWorkListBean userWorkListBean) {
                        if(mView == null){
                            return;
                        }
                        mView.cancelWorkLoading();
                        mView.resetRefresh();
                        if(userWorkListBean != null){
                            mView.showWorkList(userWorkListBean.getWorks());
                        }
                    }
                });
    }

    @Override
    public void openProduct(String userId, String workId) {
        FindProvider.openProduct((Activity)mView.getContext(), userId, workId);
    }

    @Override
    public void onOpenFrame(String userId) {
        UserProvider.openFrame((Activity)mView.getContext(), userId);
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
    }
}
