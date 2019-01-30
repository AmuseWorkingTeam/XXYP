
package com.xxyp.xxyp.user.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.user.bean.UserShotListBean;

import rx.Observable;

/**
 * Description : 个人中心contract Created by sunpengfei on 2017/8/2. Person in
 * charge : sunpengfei
 */
public interface PersonalCenterContract {

    /**
     * 个人中心view
     */
    interface View extends IBaseView<Presenter> {

        void showUserInfo(UserInfo userInfo);

        void showFansCount(int fansCount, int followCount);
    }

    /**
     * 个人中心presenter
     */
    interface Presenter extends IBasePresenter<View> {

        /**
         * 获取我的粉丝关注数
         */
        void getFansCount();

        /**
         * 获取我的信息
         */
        void getMyUserInfo();

        /**
         * 打开用户信息
         */
        void openPersonalInfo();

        /**
         * 打开我的约拍
         */
        void openMyDatingShot();

        /**
         * 打开我的全部粉丝
         */
        void openMyFans();

        /**
         * 打开我的全部关注
         */
        void openMyFocus();

        void openSetting();

        void openMyPhoto();
    }

    /**
     * 个人中心model
     */
    interface Model {

        /**
         * 获取我的信息
         *
         * @return Observable
         */
        Observable<UserInfo> getMyUserInfo();

    }

}
