
package com.xxyp.xxyp.user.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.xxyp.xxyp.common.bean.PhotoViewBean;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.find.provider.FindProvider;
import com.xxyp.xxyp.main.bean.ShotPhotoBean;
import com.xxyp.xxyp.main.bean.WorkPhotoBean;
import com.xxyp.xxyp.message.provider.ChatProvider;
import com.xxyp.xxyp.message.utils.MessageConfig;
import com.xxyp.xxyp.user.bean.MyShotPhotoOutput;
import com.xxyp.xxyp.user.contract.FrameContract;
import com.xxyp.xxyp.user.contract.PhotoContract;
import com.xxyp.xxyp.user.model.FrameModel;
import com.xxyp.xxyp.user.provider.UserProvider;
import com.xxyp.xxyp.user.utils.FrameConfig;

import java.util.ArrayList;
import java.util.List;
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
public class PhotoPresenter implements PhotoContract.Presenter {

    private FrameContract.Model mModel;

    private CompositeSubscription mSubscription;

    private PhotoContract.View mView;

    private int pageSize = 9;

    private int pageIndex = 0;

    private boolean isLast;

    public PhotoPresenter(PhotoContract.View view) {
        mView = view;
        mModel = new FrameModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void getUserInfo(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        UserInfo userInfo = UserProvider.getUserInfoByDB(userId);
        mView.showUserInfo(userInfo);
    }


    @Override
    public void obtainMyPhoto(final String userId, final boolean isLoadData) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        if (isLast) {
            ToastUtil.showTextViewPrompt("没有更多数据了");
            mView.resetLoadMore();
            return;
        }
        mView.showFrameDialog(true);
        Subscription subscription = mModel.getShotPhoto(userId, pageSize, pageIndex)
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyShotPhotoOutput>() {
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
                    public void onNext(MyShotPhotoOutput shotPhoto) {
                        if (mView == null) {
                            return;
                        }
                        mView.cancelFrameDialog();
                        pageIndex++;
                        if (shotPhoto != null && shotPhoto.getShotPhotos() != null && !shotPhoto.getShotPhotos().isEmpty()) {
                            if (isLoadData) {
                                mView.showUserWorks(shotPhoto.getShotPhotos());
                            } else {
                                mView.addUserWorks(shotPhoto.getShotPhotos());
                            }
                            if (shotPhoto.getShotPhotos().size() < pageSize) {
                                isLast = true;
                            }
                        } else {
                            isLast = true;
                        }
                        if (!isLoadData) {
                            mView.resetLoadMore();
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
    public void openShotPhotoDetail(int position, String userId, List<WorkPhotoBean> shotPhotoBeans) {
        if (shotPhotoBeans == null || shotPhotoBeans.isEmpty()) {
            return;
        }
        List<PhotoViewBean> photos = new ArrayList<>();
        for (WorkPhotoBean shotBean : shotPhotoBeans) {
            PhotoViewBean bean = new PhotoViewBean();
            bean.setHttpUrl(shotBean.getWorksPhoto());
            photos.add(bean);
        }
        FindProvider.openPhotoDetail((Activity) mView.getContext(), position, userId, photos);
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
