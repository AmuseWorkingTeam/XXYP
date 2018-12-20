
package com.xxyp.xxyp.publish.contract;

import android.content.Intent;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.main.bean.WorkBean;

import java.util.List;

import rx.Observable;


/**
 * Description : 发布页面contract Created by sunpengfei on 2017/8/2. Person in
 * charge : sunpengfei
 */
public interface PublishContract {

    /**
     * 发布页面view
     */
    interface View extends IBaseView<Presenter> {

        void showPublishDialog();

        void cancelPublishDialog();

        /**
         * 展示图片
         * @param pics  图片地址
         */
        void showChoosePic(List<String> pics);

        /**
         * 展示位置
         * @param address  地址
         */
        void showLocation(String address);
    }

    /**
     * 发布页面presenter
     */
    interface Presenter extends IBasePresenter<View> {

        /**
         * 选择图片
         * @param count  个数
         */
        void onChoosePic(int count);

        /**
         * 设置位置
         */
        void onChooseLocation();

        /**
         * 上传作品
         * @param paths    图片本地路径集合
         * @param workTitle  作品标题
         * @param workDesc   作品描述
         * @param workAddress 作品地点
         */
        void uploadWorks(List<String> paths, String workTitle, String workDesc,
                String workAddress);

        /**
         * 发布约拍
         * @param paths       图片地址
         * @param shotDesc    约拍描述
         * @param shotPurpose    约拍目的
         * @param shotPayMethod     约拍付款类型
         * @param time        约拍时间
         * @param address     约拍地址
         */
        void uploadDatingShot(List<String> paths, String shotDesc, String shotPurpose,
                              String shotPayMethod, long time, String address);

        /**
         * 返回数据
         * @param requestCode   请求码
         * @param resultCode    返回码
         * @param data          返回数据
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    /**
     * 发布页面model
     */
    interface Model {

        /**
         * 发布作品
         * @param workBean 作品信息
         * @return  Observable<String>
         */
        Observable<String> publishWorks(WorkBean workBean);

        /**
         * 发布约拍
         * @param shotBean 约拍信息
         * @return  Observable<String>
         */
        Observable<String> publishDatingShot(ShotBean shotBean);
    }

}
