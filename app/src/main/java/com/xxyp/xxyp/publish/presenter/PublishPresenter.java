
package com.xxyp.xxyp.publish.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.common.utils.compress.LuBan;
import com.xxyp.xxyp.common.utils.gallery.GalleryActivity;
import com.xxyp.xxyp.common.utils.gallery.GalleryProvider;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuManager;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuUploadCallback;
import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.main.bean.ShotPhotoBean;
import com.xxyp.xxyp.main.bean.WorkBean;
import com.xxyp.xxyp.main.bean.WorkPhotoBean;
import com.xxyp.xxyp.map.location.beans.PluginMapLocationBean;
import com.xxyp.xxyp.map.view.MapControlFragment;
import com.xxyp.xxyp.publish.contract.PublishContract;
import com.xxyp.xxyp.publish.model.PublishModel;
import com.xxyp.xxyp.user.provider.UserProvider;
import com.xxyp.xxyp.user.view.MyDatingShotActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Description : 发布presenter Created by sunpengfei on 2017/8/2. Person in charge
 * : sunpengfei
 */
public class PublishPresenter implements PublishContract.Presenter {

    private int OPEN_GALLERY = 1000;

    private int OPEN_LOCATION = 1001;

    private PublishContract.Model mModel;

    private PublishContract.View mView;

    public PublishPresenter(PublishContract.View view) {
        mView = view;
        mModel = new PublishModel();
    }


    @Override
    public void onChoosePic(int count) {
        GalleryProvider.openGalley((Activity) mView.getContext(), 9 - count, OPEN_GALLERY);
    }

    @Override
    public void onChooseLocation() {
        UserProvider.openChooseLocation((Activity) mView.getContext(), OPEN_LOCATION);
    }

    @Override
    public void uploadWorks(List<String> paths, final String workTitle,
                            final String workDesc, final String workAddress) {
        if (paths == null || paths.size() == 0) {
            return;
        }
        if (TextUtils.isEmpty(workTitle) || TextUtils.isEmpty(workDesc)) {
            return;
        }
        mView.showPublishDialog();
        Observable.just(paths).flatMap(new Func1<List<String>, Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call(List<String> strings) {
                return compressPhotos(strings);
            }
        }).flatMap(new Func1<List<String>, Observable<SparseArray<String>>>() {
            @Override
            public Observable<SparseArray<String>> call(List<String> strings) {
                return uploadPhotos(strings);
            }
        }).map(new Func1<SparseArray<String>, List<WorkPhotoBean>>() {
            @Override
            public List<WorkPhotoBean> call(SparseArray<String> stringSparseArray) {
                List<WorkPhotoBean> workPhotoBeans = new ArrayList<>();
                if (stringSparseArray != null && stringSparseArray.size() > 0) {
                    for (int i = 0; i < stringSparseArray.size(); i++) {
                        WorkPhotoBean photoBean = new WorkPhotoBean();
                        photoBean.setWorksImageOrder(i + 1);
                        photoBean.setWorksPhoto(stringSparseArray.get(i + 1));
                        workPhotoBeans.add(photoBean);
                    }
                }
                return workPhotoBeans;
            }
        }).flatMap(new Func1<List<WorkPhotoBean>, Observable<String>>() {
            @Override
            public Observable<String> call(List<WorkPhotoBean> workPhotoBeen) {
                WorkBean workBean = new WorkBean();
                workBean.setList(workPhotoBeen);
                workBean.setWorksTitle(workTitle);
                workBean.setWorksIntroduction(workDesc);
                workBean.setWorksAddress(workAddress);
                workBean.setUserId(SharePreferenceUtils.getInstance().getUserId());
                if (mModel != null) {
                    return mModel.publishWorks(workBean);
                }
                return null;
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                XXLog.log_e("publish work is failed ", e.getMessage());
                if (mView != null) {
                    mView.cancelPublishDialog();
                    ToastUtil.showTextViewPrompt(R.string.publish_work_fail);
                }
            }

            @Override
            public void onNext(String s) {
                if (mView != null) {
                    mView.cancelPublishDialog();
                    ((Activity) mView.getContext()).finish();
                }
            }
        });
    }

    @Override
    public void uploadDatingShot(List<String> paths, final String shotDesc, final String shotPurpose,
                                 final String shotPayMethod, final long time, final String address) {
        if (paths == null || paths.size() == 0) {
            ToastUtil.showTextViewPrompt("请将图片上传");
            return;
        }
        if (TextUtils.isEmpty(shotDesc) || TextUtils.isEmpty(shotPurpose)
                || TextUtils.isEmpty(shotPayMethod) || time <= 0 || TextUtils.isEmpty(address)) {
            ToastUtil.showTextViewPrompt("请将所有信息填写完整");
            return;
        }
        mView.showPublishDialog();
        Observable.just(paths).flatMap(new Func1<List<String>, Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call(List<String> strings) {
                return compressPhotos(strings);
            }
        }).flatMap(new Func1<List<String>, Observable<SparseArray<String>>>() {
            @Override
            public Observable<SparseArray<String>> call(List<String> strings) {
                return uploadPhotos(strings);
            }
        }).map(new Func1<SparseArray<String>, List<ShotPhotoBean>>() {
            @Override
            public List<ShotPhotoBean> call(SparseArray<String> stringSparseArray) {
                List<ShotPhotoBean> shotBeans = new ArrayList<>();
                if (stringSparseArray != null && stringSparseArray.size() > 0) {
                    for (int i = 0; i < stringSparseArray.size(); i++) {
                        ShotPhotoBean photoBean = new ShotPhotoBean();
                        photoBean.setDatingShotImageOrder(i + 1);
                        photoBean.setDatingShotPhoto(stringSparseArray.get(i + 1));
                        shotBeans.add(photoBean);
                    }
                }
                return shotBeans;
            }
        }).flatMap(new Func1<List<ShotPhotoBean>, Observable<ShotBean>>() {
            @Override
            public Observable<ShotBean> call(List<ShotPhotoBean> shotPhotoBeen) {
                ShotBean shotBean = new ShotBean();
                shotBean.setDatingShotImages(shotPhotoBeen);
                shotBean.setDatingShotIntroduction(shotDesc);
                shotBean.setDatingShotAddress(shotDesc);
                shotBean.setPurpose(shotPurpose);
                shotBean.setPaymentMethod(shotPayMethod);
                shotBean.setReleaseTime(time);
                shotBean.setDatingShotAddress(address);
                shotBean.setUserId(SharePreferenceUtils.getInstance().getUserId());
                shotBean.setStatus(1);
                return mModel.publishDatingShot(shotBean);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ShotBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                XXLog.log_e("publish datingShot is failed ", e.getMessage());
                if (mView != null) {
                    mView.cancelPublishDialog();
                    ToastUtil.showTextViewPrompt(R.string.publish_shot_fail);
                }
            }

            @Override
            public void onNext(ShotBean s) {
                if (mView != null) {
                    mView.cancelPublishDialog();
                    Intent intent = new Intent();
                    intent.putExtra(MyDatingShotActivity.MY_SHOT, s);
                    ((Activity) mView.getContext()).setResult(Activity.RESULT_OK, intent);
                    ((Activity) mView.getContext()).finish();
                }
            }
        });
    }

    /**
     * 压缩图片
     *
     * @param pics 图片列表
     * @return Observable
     */
    private Observable<List<String>> compressPhotos(List<String> pics) {
        if (pics == null || pics.size() == 0) {
            return Observable.empty();
        }
        List<File> files = new ArrayList<>();
        for (String pic : pics) {
            files.add(new File(pic));
        }
        return LuBan.getInstance().compress(files).putGear(LuBan.THIRD_GEAR).asListObservable()
                .subscribeOn(Schedulers.computation()).filter(new Func1<List<File>, Boolean>() {
                    @Override
                    public Boolean call(List<File> files) {
                        return files != null && files.size() > 0;
                    }
                }).map(new Func1<List<File>, List<String>>() {
                    @Override
                    public List<String> call(List<File> files) {
                        List<String> pics = new ArrayList<>();
                        for (File file : files) {
                            pics.add(file.getAbsolutePath());
                        }
                        return pics;
                    }
                });
    }

    /**
     * 上传单个图片
     *
     * @param path  图片路径
     * @param index 图片索引位置
     * @return Observable
     */
    private Observable<Pair<Integer, String>> uploadObservable(final String path, final int index) {
        if (TextUtils.isEmpty(path)) {
            return Observable.empty();
        }
        return Observable.create(new Action1<Emitter<Pair<Integer, String>>>() {
            @Override
            public void call(final Emitter<Pair<Integer, String>> pairEmitter) {
                QiNiuManager.getInstance().uploadImage(path, new QiNiuUploadCallback() {
                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onSuccess(String file) {
                        pairEmitter.onNext(new Pair<>(index, file));
                    }

                    @Override
                    public void onError(int errorCode, String msg) {
                        pairEmitter.onError(new Throwable(msg));
                    }
                });
            }
        }, Emitter.BackpressureMode.BUFFER).observeOn(Schedulers.computation());
    }

    /**
     * 批量上传图片
     *
     * @param photos 图片地址
     * @return Observable
     */
    private Observable<SparseArray<String>> uploadPhotos(List<String> photos) {
        if (photos == null || photos.size() == 0) {
            return Observable.empty();
        }
        final SparseArray<String> photoCache = new SparseArray<>();
        final SparseArray<String> photoUrls = new SparseArray<>();
        //记录顺序
        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            photoCache.put(i + 1, photos.get(i));
            index.add(i + 1);
        }
        return Observable.from(index).flatMap(new Func1<Integer, Observable<Pair<Integer, String>>>() {
            @Override
            public Observable<Pair<Integer, String>> call(Integer integer) {
                return uploadObservable(photoCache.get(integer), integer);
            }
        }).map(new Func1<Pair<Integer, String>, SparseArray<String>>() {
            @Override
            public SparseArray<String> call(Pair<Integer, String> integerStringPair) {
                photoUrls.put(integerStringPair.first, integerStringPair.second);
                return photoUrls;
            }
        }).filter(new Func1<SparseArray<String>, Boolean>() {
            @Override
            public Boolean call(SparseArray<String> stringSparseArray) {
                return photoCache.size() == photoUrls.size();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        if (requestCode == OPEN_GALLERY) {
            List<String> picPaths = data.getStringArrayListExtra(GalleryActivity.PHOTOS);
            if (picPaths != null && picPaths.size() > 0) {
                mView.showChoosePic(picPaths);
            }
        } else if (requestCode == OPEN_LOCATION) {
            PluginMapLocationBean locationBean = (PluginMapLocationBean) data.getSerializableExtra(MapControlFragment.MAP_LOCATION_BEAN);
            if (locationBean != null) {
                mView.showLocation(locationBean.getLocation());
            }
        }
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
        mModel = null;
    }
}
