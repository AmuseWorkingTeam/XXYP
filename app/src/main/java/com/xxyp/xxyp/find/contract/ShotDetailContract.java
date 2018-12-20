
package com.xxyp.xxyp.find.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.main.bean.ShotPhotoBean;
import com.xxyp.xxyp.user.bean.UserShotListBean;

import java.util.List;

import rx.Observable;

/**
 * Description : 约拍详情contract
 * Created by sunpengfei on 2017/8/9.
 * Person in charge : sunpengfei
 */
public interface ShotDetailContract {

    /**
     * 作品详情view
     */
    interface View extends IBaseView<Presenter>{

        /**
         * 展示记载框
         * @param cancelable  是否可取消
         */
        void showShotLoading(boolean cancelable);

        /**
         * 取消加载框
         */
        void cancelShotLoading();

        /**
         * 展示约拍
         * @param shotBean  约拍
         */
        void showShot(ShotBean shotBean);

        /**
         * 展示用户信息
         * @param userName   名称
         * @param userAvatar 头像
         * @param userAddress 作品位置
         * @param userTime    作品时间
         */
        void showUserInfo(String userName, String userAvatar, String userAddress,
                          long userTime);
    }

    interface Presenter extends IBasePresenter<View>{

        /**
         * 获取约拍
         * @param userId  用户id
         * @param shotId  约拍id
         */
        void obtainShot(String userId, String shotId);

        /**
         * 打开frame
         * @param userId 用户id
         */
        void openFrame(String userId);

        /**
         * 打开聊天
         * @param userId 用户id
         */
        void openChat(String userId);

        /**
         * 打开约拍图片详情
         * @param index   图片位置
         * @param userId  用户id
         * @param shotPhotoBeans  约拍图片列表
         */
        void openShotPhotoDetail(int index, String userId, List<ShotPhotoBean> shotPhotoBeans);

    }

    interface Model{

        /**
         * 获取作品
         * @param userId  用户id
         * @param shotId  约拍id
         */
        Observable<UserShotListBean> obtainShot(String userId, String shotId);

    }
}
