
package com.xxyp.xxyp.main.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.main.bean.WorkBean;
import com.xxyp.xxyp.main.bean.WorkHotListBean;

import java.util.List;

import rx.Observable;


/**
 * Description : 首页contract Created by sunpengfei on 2017/7/31. Person in charge
 * : sunpengfei
 */
public interface MainContract {

    /**
     * 首页热门view
     */
    interface View extends IBaseView<Presenter> {

        /**
         * 展示作品轮播
         * @param workBeanList  作品列表
         */
        void showBannerList(List<WorkBean> workBeanList);

        /**
         * 展示作品的用户信息
         * @param workBean  作品信息
         */
        void showBannerUserInfo(WorkBean workBean);

        /**
         * 展示作品列表
         * @param workBeanList 作品列表
         */
        void showWorkList(List<WorkBean> workBeanList);

        /**
         * 展示加载框
         * @param cancelable  是否可取消
         */
        void showHotWorkLoading(boolean cancelable);

        /**
         * 取消展示加载框
         */
        void cancelHotWorkLoading();

        /**
         * 还原下拉刷新
         */
        void resetRefresh();

        /**
         * 还原上拉加载
         */
        void resetLoadMore();
    }

    /**
     * 首页presenter
     */
    interface Presenter extends IBasePresenter<View> {

        /**
         * 获取热门作品
         */
        void getHotWorks();

        /**
         * 打开用户详情
         * @param userId 用户id
         */
        void openFrame(String userId);

        /**
         * 打开作品详情
         * @param userId 用户的id
         * @param workId 作品id
         */
        void openProduct(String userId, String workId);
    }

    /**
     * 首页model
     */
    interface Model {

        /**
         * 获取热门作品
         * @return Observable<WorkHotListBean>
         */
        Observable<WorkHotListBean> getHotWorks();
    }
}
