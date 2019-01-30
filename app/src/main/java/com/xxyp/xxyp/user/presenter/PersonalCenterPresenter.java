
package com.xxyp.xxyp.user.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.RxBus;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.publish.db.PublishDBManager;
import com.xxyp.xxyp.user.bean.UserShotListBean;
import com.xxyp.xxyp.user.contract.PersonalCenterContract;
import com.xxyp.xxyp.user.model.FrameModel;
import com.xxyp.xxyp.user.model.PersonalModel;
import com.xxyp.xxyp.user.provider.UserProvider;
import com.xxyp.xxyp.user.utils.FrameConfig;
import com.xxyp.xxyp.user.view.MyDatingShotActivity;
import com.xxyp.xxyp.user.view.PersonalInfoActivity;
import com.xxyp.xxyp.user.view.SettingActivity;

import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Description : 个人中心presenter Created by sunpengfei on 2017/8/2. Person in
 * charge : sunpengfei
 */
public class PersonalCenterPresenter implements PersonalCenterContract.Presenter {

    private PersonalCenterContract.View mView;

    private PersonalCenterContract.Model mModel;

    private CompositeSubscription mSubscription;

    public PersonalCenterPresenter(PersonalCenterContract.View view) {
        mView = view;
        mModel = new PersonalModel();
        mSubscription = new CompositeSubscription();
        receiveRxBus();
    }

    /**
     * 接受RxBus
     */
    private void receiveRxBus() {
        Subscription refresh = RxBus.getInstance().toObservable(Intent.class)
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Intent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        XXLog.log_e("PersonalCenterPresenter",
                                "RxBus refresh is failed " + e.getMessage());
                    }

                    @Override
                    public void onNext(Intent intent) {
                        if (intent != null && TextUtils.equals(intent.getAction(),
                                FrameConfig.REFRESH_PERSONAL_CENTER_ACTION)) {
                            // 刷新
                            getMyUserInfo();
                        }
                    }
                });
        mSubscription.add(refresh);
    }

    @Override
    public void getFansCount() {
        String userId = SharePreferenceUtils.getInstance().getUserId();
        Subscription subscription = new FrameModel().getFansCount(userId)
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<String, Integer> stringIntegerMap) {
                        if (stringIntegerMap == null || mView == null) {
                            return;
                        }
                        mView.showFansCount(stringIntegerMap.get(FrameConfig.FANS_COUNT),
                                stringIntegerMap.get(FrameConfig.FOLLOW_COUNT));
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void getMyUserInfo() {
        Subscription subscription = mModel.getMyUserInfo().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        if (userInfo == null || mView == null) {
                            return;
                        }
                        mView.showUserInfo(userInfo);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void openPersonalInfo() {
        Intent intent = new Intent(mView.getContext(), PersonalInfoActivity.class);
        mView.getContext().startActivity(intent);
    }

    @Override
    public void openMyDatingShot() {
        Intent intent = new Intent(mView.getContext(), MyDatingShotActivity.class);
        mView.getContext().startActivity(intent);
    }

    @Override
    public void openMyFans() {
        UserProvider.openFans((Activity) mView.getContext(),
                SharePreferenceUtils.getInstance().getUserId());
    }

    @Override
    public void openMyFocus() {
        UserProvider.openFocus((Activity) mView.getContext(),
                SharePreferenceUtils.getInstance().getUserId());
    }

    @Override
    public void openSetting() {
        Intent intent = new Intent(mView.getContext(), SettingActivity.class);
        mView.getContext().startActivity(intent);
    }

    @Override
    public void openMyPhoto() {
        UserProvider.openMyPhoto((Activity) mView.getContext(),
                SharePreferenceUtils.getInstance().getUserId());
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }
}
