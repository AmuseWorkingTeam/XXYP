
package com.xxyp.xxyp.find.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.xxyp.xxyp.common.bean.PhotoViewBean;
import com.xxyp.xxyp.find.contract.ShotDetailContract;
import com.xxyp.xxyp.find.model.ShotDetailModel;
import com.xxyp.xxyp.find.provider.FindProvider;
import com.xxyp.xxyp.main.bean.ShotPhotoBean;
import com.xxyp.xxyp.message.provider.ChatProvider;
import com.xxyp.xxyp.message.utils.MessageConfig;
import com.xxyp.xxyp.user.bean.UserShotListBean;
import com.xxyp.xxyp.user.provider.UserProvider;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description : 约拍详情presenter
 * Created by sunpengfei on 2017/8/28.
 * Person in charge : sunpengfei
 */
public class ShotDetailPresenter implements ShotDetailContract.Presenter {
    
    private ShotDetailContract.View mView;
    
    private ShotDetailContract.Model mModel;
    
    public ShotDetailPresenter(ShotDetailContract.View view){
        mView = view;
        mModel = new ShotDetailModel();
    }
    
    @Override
    public void obtainShot(String userId, String shotId) {
        if(TextUtils.isEmpty(userId)){
            return;
        }
        mView.showShotLoading(true);
        mModel.obtainShot(userId, shotId).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserShotListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mView != null){
                            mView.cancelShotLoading();
                        }
                    }

                    @Override
                    public void onNext(UserShotListBean userShotListBean) {
                        if(mView != null){
                            mView.cancelShotLoading();
                            if (userShotListBean != null && userShotListBean.getDatingShot() != null
                                    && userShotListBean.getDatingShot().size() > 0) {
                                mView.showShot(userShotListBean.getDatingShot().get(0));
                            }
                        }
                    }
                });
    }

    @Override
    public void openFrame(String userId) {
        UserProvider.openFrame((Activity)mView.getContext(), userId);
    }

    @Override
    public void openChat(String userId) {
        ChatProvider.openChatActivity((Activity)mView.getContext(),
                MessageConfig.MessageCatalog.CHAT_SINGLE, userId);
    }

    @Override
    public void openShotPhotoDetail(int index, String userId, List<ShotPhotoBean> shotPhotoBeans) {
        if (shotPhotoBeans == null || shotPhotoBeans.isEmpty()) {
            return;
        }
        List<PhotoViewBean> photos = new ArrayList<>();
        for (ShotPhotoBean shotBean : shotPhotoBeans) {
            PhotoViewBean bean = new PhotoViewBean();
            bean.setHttpUrl(shotBean.getDatingShotPhoto());
            photos.add(bean);
        }
        FindProvider.openPhotoDetail((Activity)mView.getContext(), index, userId, photos);
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
        mModel = null;
    }
}
