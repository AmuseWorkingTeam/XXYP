
package com.xxyp.xxyp.find.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.main.bean.WorkBean;
import com.xxyp.xxyp.main.bean.WorkPhotoBean;
import com.xxyp.xxyp.user.bean.UserWorkListBean;

import java.util.List;

import rx.Observable;

/**
 * Description : 作品详情contract
 * Created by sunpengfei on 2017/8/9.
 * Person in charge : sunpengfei
 */
public interface ProductDetailContract {

    /**
     * 作品详情view
     */
    interface View extends IBaseView<Presenter>{

        /**
         * 展示记载框
         * @param cancelable  是否可取消
         */
        void showProductLoading(boolean cancelable);

        /**
         * 取消加载框
         */
        void cancelProductLoading();

        /**
         * 展示作品列表
         * @param workBean  作品
         */
        void showWork(WorkBean workBean);

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
         * 获取作品
         * @param userId  用户id
         * @param workId  作品id
         */
        void obtainWorks(String userId, String workId);

        /**
         * 打开frame
         * @param userId 用户id
         */
        void openFrame(String userId);

        /**
         * 打开作品图片详情
         * @param index   图片位置
         * @param userId  用户id
         * @param workPhotoBeans  作品列表
         */
        void openWorkPhotoDetail(int index, String userId, List<WorkPhotoBean> workPhotoBeans);
    }

    interface Model{

        /**
         * 获取作品
         * @param userId  用户id
         * @param workId  作品id
         */
        Observable<UserWorkListBean> obtainWorks(String userId, String workId);

    }
}
