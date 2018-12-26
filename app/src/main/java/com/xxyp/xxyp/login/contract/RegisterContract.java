
package com.xxyp.xxyp.login.contract;

import android.content.Intent;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.common.bean.UserInfo;

import rx.Observable;

/**
 * Description : 注册contract Created by sunpengfei on 2017/7/31. Person in charge
 * : sunpengfei
 */
public interface RegisterContract {

    /**
     * 注册view
     */
    interface View extends IBaseView<Presenter> {

        /**
         * 获取个人信息描述
         * @return String
         */
        String getDesc();

        /**
         * 获取邮箱地址
         * @return String
         */
        String getEmail();

        /**
         * 获取昵称
         * @return String
         */
        String getNick();

        /**
         * 获取个电话
         * @return String
         */
        String getPhone();

        /**
         * 展示头像
         */
        void showAvatar(String avatar);

        /**
         * 展示名字
         */
        void showName(String name);

        /**
         * 展示加载框
         * @param cancelable  是否可取消
         */
        void showRegisterLoading(boolean cancelable);

        /**
         * 取消加载框
         */
        void cancelRegisterLoading();

        int getCheckType();
    }

    /**
     * 注册presenter
     */
    interface Presenter extends IBasePresenter<View> {

        /**
         * 创建用户
         */
        void createUser();

        /**
         * 返回数据
         * @param requestCode  请求码
         * @param resultCode   返回码
         * @param data         返回数据
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);

        /**
         * 跳转选择相册
         */
        void onGoGallery();

        /**
         * 展示用户信息
         * @param userInfo
         */
        void showUserInfo(UserInfo userInfo);
    }

    /**
     * 注册model
     */
    interface Model {

        /**
         * 创建用户
         * @param userInfo  用户信息
         * @return Observable
         */
        Observable<UserInfo> createUserInfo(UserInfo userInfo);
    }
}
