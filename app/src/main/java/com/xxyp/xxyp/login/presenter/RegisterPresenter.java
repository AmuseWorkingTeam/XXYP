
package com.xxyp.xxyp.login.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.common.utils.compress.LuBan;
import com.xxyp.xxyp.common.utils.gallery.GalleryActivity;
import com.xxyp.xxyp.common.utils.gallery.GalleryProvider;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuManager;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuUploadCallback;
import com.xxyp.xxyp.login.contract.RegisterContract;
import com.xxyp.xxyp.login.model.RegisterModel;
import com.xxyp.xxyp.user.provider.UserProvider;

import java.io.File;
import java.util.List;

import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Description : 注册presenter Created by sunpengfei on 2017/7/31. Person in
 * charge : sunpengfei
 */
public class RegisterPresenter implements RegisterContract.Presenter {

    private int OPEN_GALLERY = 1000;

    private RegisterContract.Model mModel;

    private String mPhoto;

    private CompositeSubscription mSubscription;

    private UserInfo mUserInfo;

    private RegisterContract.View mView;

    public RegisterPresenter(RegisterContract.View view) {
        mView = view;
        mModel = new RegisterModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void createUser() {
        String nick = mView.getNick();
        String desc = mView.getDesc();
        String phone = mView.getPhone();
        String email = mView.getEmail();
        int checkType = mView.getCheckType();
        if (TextUtils.isEmpty(nick)) {
            return;
        }
        mUserInfo.setUserName(nick);
        mUserInfo.setUserIntroduction(desc);
        mUserInfo.setMobile(phone);
        mUserInfo.setEmail(email);
        mUserInfo.setUserIdentity(checkType);
        mView.showRegisterLoading(true);
        Subscription subscription = mModel.createUserInfo(mUserInfo)
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.cancelRegisterLoading();
                            ToastUtil.showTextViewPrompt(R.string.net_error);
                        }
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        if (mView != null) {
                            mView.cancelRegisterLoading();
                            Intent intent = new Intent();
                            ((Activity) mView.getContext()).setResult(Activity.RESULT_OK, intent);
                            ((Activity) mView.getContext()).finish();
                        }
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode != OPEN_GALLERY) || (resultCode != Activity.RESULT_OK)
                || (data == null)) {
            return;
        }
        List<String> paths = data.getStringArrayListExtra(GalleryActivity.PHOTOS);
        if (paths != null && paths.size() > 0) {
            mPhoto = paths.get(0);
            mView.showAvatar("file://" + mPhoto);
            mView.showRegisterLoading(true);
            Subscription subscription = Observable.just(mPhoto)
                    .flatMap(new Func1<String, Observable<?>>() {
                        @Override
                        public Observable<?> call(String s) {
                            if (TextUtils.isEmpty(s)) {
                                // 如果没有图片 则直接更新
                                return Observable.just(null);
                            }
                            return uploadAvatar(mPhoto);
                        }
                    }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Object>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mView != null) {
                                mView.cancelRegisterLoading();
                            }
                            XXLog.log_e("RegisterPresenter",
                                    "updateUserInfo is failed" + e.getMessage());
                        }

                        @Override
                        public void onNext(Object o) {
                            if (mView != null) {
                                mView.cancelRegisterLoading();
                            }
                        }
                    });
            mSubscription.add(subscription);
        }
    }

    /**
     * 上传头像
     *
     * @return Observable
     */
    private Observable<Object> uploadAvatar(String pic) {
        if (TextUtils.isEmpty(pic)) {
            return Observable.just(null);
        }
        File file = new File(pic);
        return LuBan.getInstance().compress(file).putGear(LuBan.THIRD_GEAR).asObservable()
                .subscribeOn(Schedulers.computation()).map(new Func1<File, String>() {
                    @Override
                    public String call(File file) {
                        return file.getAbsolutePath();
                    }
                }).flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String s) {
                        return Observable.create(new Action1<Emitter<String>>() {
                            @Override
                            public void call(final Emitter<String> pairEmitter) {
                                QiNiuManager.getInstance().uploadImage(s, new QiNiuUploadCallback() {
                                    @Override
                                    public void onProgress(int progress) {

                                    }

                                    @Override
                                    public void onSuccess(String file) {
                                        pairEmitter.onNext(file);
                                    }

                                    @Override
                                    public void onError(int errorCode, String msg) {
                                        ToastUtil.showTextViewPrompt("上传头像失败");
                                        XXLog.log_e("PersonalInfoPresenter", "uploadImage is failed" + msg);
                                        pairEmitter.onError(new Throwable(msg));
                                    }
                                });
                            }
                        }, Emitter.BackpressureMode.BUFFER).observeOn(Schedulers.computation());
                    }
                }).flatMap(new Func1<String, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(String s) {
                        mUserInfo.setUserImage(s);
                        return UserProvider.updateUserInfo(mUserInfo);
                    }
                });
    }

    @Override
    public void onGoGallery() {
        GalleryProvider.openGalley((Activity) mView.getContext(), 1, OPEN_GALLERY);
    }

    @Override
    public void showUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        if (mUserInfo == null) {
            mUserInfo = new UserInfo();
        }
        mView.showAvatar(mUserInfo.getUserImage());
        mView.showName(mUserInfo.getUserName());
    }

    @Override
    public void onDestroyPresenter() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
        mView = null;
        mModel = null;
        mPhoto = null;
    }

}
