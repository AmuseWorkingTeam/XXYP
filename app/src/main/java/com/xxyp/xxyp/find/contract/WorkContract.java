
package com.xxyp.xxyp.find.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.main.bean.WorkBean;

import java.util.List;

/**
 * Description : 发现作品contract
 * Created by sunpengfei on 2017/8/21.
 * Person in charge : sunpengfei
 */
public interface WorkContract {

    /**
     * 发现作品view
     */
    interface View extends IBaseView<Presenter>{

        /**
         * 展示作品数据
         * @param workBeans  约拍数据
         */
        void showWorkList(List<WorkBean> workBeans);

        /**
         * 展示加载框
         * @param cancelable  是否可取消
         */
        void showWorkLoading(boolean cancelable);

        /**
         * 取消加载框
         */
        void cancelWorkLoading();

        /**
         * 还原下拉刷新
         */
        void resetRefresh();
    }

    /**
     * 发现作品presenter
     */
    interface Presenter extends IBasePresenter<View>{

        /**
         * 获取作品列表
         */
        void obtainWorkList();

        /**
         * 跳转作品
         * @param userId   用户id
         * @param workId   作品id
         */
        void openProduct(String userId, String workId);

        /**
         * 跳转frame
         * @param userId  用户id
         */
        void onOpenFrame(String userId);
    }
}
