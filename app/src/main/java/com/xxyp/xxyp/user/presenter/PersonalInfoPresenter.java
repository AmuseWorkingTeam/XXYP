
package com.xxyp.xxyp.user.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.RxBus;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.common.utils.compress.LuBan;
import com.xxyp.xxyp.common.utils.gallery.GalleryActivity;
import com.xxyp.xxyp.common.utils.gallery.GalleryProvider;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuManager;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuUploadCallback;
import com.xxyp.xxyp.user.contract.PersonalInfoContract;
import com.xxyp.xxyp.user.provider.UserProvider;
import com.xxyp.xxyp.user.utils.FrameConfig;

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
 * Description : 用户信息presenter
 */
public class PersonalInfoPresenter implements PersonalInfoContract.Presenter {

    private PersonalInfoContract.View mView;

    private CompositeSubscription mSubscription;

    private final int CHOOSE_IMAGE_REQUEST_CODE = 1000;

    private final int CHOOSE_LOCATION_REQUEST_CODE = 1001;

    /* 当前userInfo */
    private UserInfo mUserInfo;

    /**
     * 图片路径
     */
    private String mImagePath;

    public PersonalInfoPresenter(PersonalInfoContract.View view) {
        mView = view;
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void setHeadImage() {
        GalleryProvider.openGalley((Activity) mView.getContext(), 1, CHOOSE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onChooseLocation() {
        UserProvider.openLocation((Activity) mView.getContext(), CHOOSE_LOCATION_REQUEST_CODE);
    }

    @Override
    public void getMyUserInfo() {
        String userId = SharePreferenceUtils.getInstance().getUserId();
        mUserInfo = UserProvider.getUserInfoByDB(userId);
        mView.showUserInfo(mUserInfo);
    }

    @Override
    public void updateUserInfo(String name, String address, String introduction,  int getGender) {
        if (mUserInfo == null) {
            ToastUtil.showTextViewPrompt("用户信息获取失败");
            return;
        }
        if (!TextUtils.isEmpty(name)) {
            mUserInfo.setUserName(name);
        }
        if (!TextUtils.isEmpty(introduction)) {
            mUserInfo.setUserIntroduction(introduction);
        }
        if (!TextUtils.isEmpty(address)) {
            mUserInfo.setAddress(address);
        }
        mUserInfo.setGender(getGender);
        // todo 这事干什么的
        if (mUserInfo.getStatus() == 0) {
            mUserInfo.setStatus(1);
        }
        uploadImage();
    }

    /**
     * 上传图片
     */
    private void uploadImage() {
        mView.showSettingDialog(true);
        Subscription subscription = Observable.just(mImagePath)
                .flatMap(new Func1<String, Observable<?>>() {
                    @Override
                    public Observable<?> call(String s) {
                        if (TextUtils.isEmpty(s)) {
                            // 如果没有图片 则直接更新
                            return UserProvider.updateUserInfo(mUserInfo);
                        }
                        return compressPhotos(mImagePath);
                    }
                }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.cancelSettingDialog();
                        }
                        XXLog.log_e("PersonalInfoPresenter",
                                "updateUserInfo is failed" + e.getMessage());
                    }

                    @Override
                    public void onNext(Object o) {
                        if (mView != null) {
                            mView.cancelSettingDialog();
                            Intent intent = new Intent();
                            intent.setAction(FrameConfig.REFRESH_PERSONAL_CENTER_ACTION);
                            RxBus.getInstance().send(intent);
                            ((Activity) mView.getContext()).finish();
                        }
                    }
                });
        mSubscription.add(subscription);
    }

    /**
     * 压缩图片
     *
     * @param pic 图片列表
     * @return Observable
     */
    private Observable<Object> compressPhotos(String pic) {
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
                                        ToastUtil.showTextViewPrompt("上传图片失败");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        if (requestCode == CHOOSE_IMAGE_REQUEST_CODE) {
            //头像返回
            List<String> picPaths = data.getStringArrayListExtra(GalleryActivity.PHOTOS);
            if (picPaths != null && picPaths.size() > 0) {
                mImagePath = picPaths.get(0);
                mView.setImage(mImagePath);
            }
        } else if (requestCode == CHOOSE_LOCATION_REQUEST_CODE) {
            String address = data.getStringExtra(FrameConfig.LOCATION_INFO);
            mView.setLocation(address);
        }
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
