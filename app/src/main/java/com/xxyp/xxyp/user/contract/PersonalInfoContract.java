
package com.xxyp.xxyp.user.contract;

import android.content.Intent;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.common.bean.UserInfo;

/**
 * Description : 设置页contract
 * Created by sunpengfei on 2017/8/28.
 * Person in charge : sunpengfei
 */
public interface PersonalInfoContract {

    /**
     * 个人设置view
     */
    interface View extends IBaseView<Presenter> {

        /**
         * 展示数据
         *
         * @param userInfo 用户信息
         */
        void showUserInfo(UserInfo userInfo);

        /**
         * 展示加载框
         *
         * @param isCancel 是否可取消
         */
        void showSettingDialog(boolean isCancel);

        /**
         * 取消加载框
         */
        void cancelSettingDialog();

        /**
         * 设置头像u
         *
         * @param path 头像路径
         */
        void setImage(String path);

        /**
         * 设置位置
         * @param address 地址
         */
        void setLocation(String address);
    }

    /**
     * 个人设置presenter
     */
    interface Presenter extends IBasePresenter<View> {

        /**
         * 设置头像
         */
        void setHeadImage();

        /**
         * 选择位置
         */
        void onChooseLocation();

        /**
         * 获取我的用户信息
         */
        void getMyUserInfo();

        /**
         * 更新用户信息
         *
         * @param name 用户名称
         * @param address 用户地址
         * @param introduction 用户简介
         */
        void updateUserInfo(String name, String address, String introduction);


        /**
         * 页面返回
         * @param requestCode  请求码
         * @param resultCode   返回码
         * @param data         返回数据
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}

