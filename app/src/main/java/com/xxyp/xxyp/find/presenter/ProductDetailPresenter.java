
package com.xxyp.xxyp.find.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.xxyp.xxyp.common.bean.PhotoViewBean;
import com.xxyp.xxyp.find.contract.ProductDetailContract;
import com.xxyp.xxyp.find.model.ProductDetailModel;
import com.xxyp.xxyp.find.provider.FindProvider;
import com.xxyp.xxyp.main.bean.WorkPhotoBean;
import com.xxyp.xxyp.user.bean.UserWorkListBean;
import com.xxyp.xxyp.user.provider.UserProvider;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Description : 作品详情presenter
 * Created by sunpengfei on 2017/8/9.
 * Person in charge : sunpengfei
 */
public class ProductDetailPresenter implements ProductDetailContract.Presenter {

    /* 作品详情view */
    private ProductDetailContract.View mView;

    /* 作品详情model */
    private ProductDetailContract.Model mModel;

    private CompositeSubscription mSubscription;

    public ProductDetailPresenter(ProductDetailContract.View view) {
        mView = view;
        mModel = new ProductDetailModel();
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void obtainWorks(String userId, String workId) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        mView.showProductLoading(true);
        Subscription subscription = mModel.obtainWorks(userId, workId).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserWorkListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.cancelProductLoading();
                        }
                    }

                    @Override
                    public void onNext(UserWorkListBean userWorkListBean) {
                        if (mView != null) {
                            mView.cancelProductLoading();
                            if (userWorkListBean != null && userWorkListBean.getWorks() != null
                                    && userWorkListBean.getWorks().size() > 0) {
                                mView.showWork(userWorkListBean.getWorks().get(0));
                            }
                        }
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void openFrame(String userId) {
        UserProvider.openFrame((Activity) mView.getContext(), userId);
    }

    @Override
    public void openWorkPhotoDetail(int index, String userId, List<WorkPhotoBean> workPhotoBeans) {
        if (workPhotoBeans == null || workPhotoBeans.isEmpty()) {
            return;
        }
        List<PhotoViewBean> photos = new ArrayList<>();
        for (WorkPhotoBean workBean : workPhotoBeans) {
            PhotoViewBean bean = new PhotoViewBean();
            bean.setHttpUrl(workBean.getWorksPhoto());
            photos.add(bean);
        }
        FindProvider.openPhotoDetail((Activity) mView.getContext(), index, userId, photos);
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
