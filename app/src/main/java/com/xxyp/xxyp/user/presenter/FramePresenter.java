
package com.xxyp.xxyp.user.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.find.provider.FindProvider;
import com.xxyp.xxyp.message.provider.ChatProvider;
import com.xxyp.xxyp.message.utils.MessageConfig;
import com.xxyp.xxyp.user.bean.UserWorkListBean;
import com.xxyp.xxyp.user.contract.FrameContract;
import com.xxyp.xxyp.user.model.FrameModel;
import com.xxyp.xxyp.user.provider.UserProvider;
import com.xxyp.xxyp.user.utils.FrameConfig;

import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Description : frame presenter Created by sunpengfei on 2017/8/2. Person in
 * charge : sunpengfei
 */
public class FramePresenter implements FrameContract.Presenter {

    private FrameContract.Model mModel;

    private CompositeSubscription mSubscription;

    private FrameContract.View mView;

    public FramePresenter(FrameContract.View view) {
        mView = view;
        mModel = new FrameModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void getUserInfo(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        Subscription subscription = UserProvider.obtainUserInfo(userId).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        if (mView == null) {
                            return;
                        }
                        mView.showUserInfo(userInfo);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void getFansFollowCount(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        Subscription subscription = mModel.getFansCount(userId)
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
                        mView.showFansFollowCount(stringIntegerMap.get(FrameConfig.FOLLOW_COUNT),
                                stringIntegerMap.get(FrameConfig.FANS_COUNT));
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void obtainUserWorks(final String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        mView.showFrameDialog(true);
        Subscription subscription = mModel.getWorks(userId)
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserWorkListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.cancelFrameDialog();
                        }
                    }

                    @Override
                    public void onNext(UserWorkListBean userWorkListBean) {
                        if (mView == null) {
                            return;
                        }
                        mView.cancelFrameDialog();
                        if (userWorkListBean != null) {
                            mView.showUserWorks(userWorkListBean.getWorks());
                        }
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void openProduct(String userId, String workId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        FindProvider.openProduct((Activity) mView.getContext(), userId, workId);
    }

    @Override
    public void openChat(String userId) {
        ChatProvider.openChatActivity((Activity) mView.getContext(),
                MessageConfig.MessageCatalog.CHAT_SINGLE, userId);
    }

    @Override
    public void openFans(String userId) {
        UserProvider.openFans((Activity) mView.getContext(), userId);
    }

    @Override
    public void openFocus(String userId) {
        UserProvider.openFocus((Activity) mView.getContext(), userId);
    }

    @Override
    public void focusUser(String userId) {
        mView.showFrameDialog(true);
        Subscription subscription = mModel.focusUser(userId).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.cancelFrameDialog();
                            ToastUtil.showTextViewPrompt(R.string.net_error);
                        }
                    }

                    @Override
                    public void onNext(Object o) {
                        if (mView != null) {
                            mView.cancelFrameDialog();
                            ToastUtil.showTextViewPrompt("关注成功");
                            mView.showFocus(true);
                        }
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void cancelFocus(String userId) {
        mView.showFrameDialog(true);
        Subscription subscription = mModel.cancelFocus(userId).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.cancelFrameDialog();
                            ToastUtil.showTextViewPrompt(R.string.net_error);
                        }
                    }

                    @Override
                    public void onNext(Object o) {
                        if (mView != null) {
                            mView.cancelFrameDialog();
                            ToastUtil.showTextViewPrompt("取消成功");
                            mView.showFocus(false);
                        }
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void getUserHasFansAndFollow(String userId, String otherUserId) {
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(otherUserId)) {
            return;
        }
        Subscription subscription = mModel.getUserHasFansAndFollow(userId, otherUserId)
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
                        if (stringIntegerMap != null) {
                            Integer isFollow = stringIntegerMap.get(FrameConfig.IS_FOLLOW);
                            mView.showFocus(isFollow == 1);
                        }
                    }
                });
        mSubscription.add(subscription);
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
}
