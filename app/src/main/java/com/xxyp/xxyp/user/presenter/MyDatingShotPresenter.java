package com.xxyp.xxyp.user.presenter;

import android.app.Activity;

import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.publish.db.PublishDBManager;
import com.xxyp.xxyp.user.bean.UserShotListBean;
import com.xxyp.xxyp.user.contract.MyDatingShotContract;
import com.xxyp.xxyp.user.model.MyDatingShotModel;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Description : 我的约拍presenter
 * Created by sunpengfei on 2017/8/11.
 */
public class MyDatingShotPresenter implements MyDatingShotContract.Presenter {

    private MyDatingShotContract.View mView;

    private MyDatingShotContract.Model mModel;

    private CompositeSubscription mSubscription;


    public MyDatingShotPresenter(MyDatingShotContract.View view) {
        mView = view;
        mModel = new MyDatingShotModel();
        mSubscription = new CompositeSubscription();
    }


    public void getMyDatingShot() {
        List<ShotBean> myShotInfo = mModel.getMyShotInfo();
        if (myShotInfo == null || myShotInfo.isEmpty()) {
            Subscription subscription = mModel.getDatingShot().subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.computation()).subscribe(new Subscriber<UserShotListBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(final UserShotListBean userInfo) {
                            if (userInfo == null) {
                                return;
                            }
                            ((Activity) mView.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mView.showMyShot(userInfo.getDatingShot());
                                }
                            });
                            PublishDBManager.getInstance().addShotInfos(null, userInfo.getDatingShot());
                        }
                    });
            mSubscription.add(subscription);
        } else {
            mView.showMyShot(myShotInfo);
        }
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
        mModel = null;
    }
}
