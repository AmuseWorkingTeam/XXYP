
package com.xxyp.xxyp.find.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;

/**
 * Description : 作品或约拍的图片详情
 * Created by sunpengfei on 2017/9/6.
 * Person in charge : sunpengfei
 */
public interface PhotoDetailContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 展示图片加载
         * @param cancelable 是否可取消
         */
        void showPhotoDialog(boolean cancelable);

        /**
         * 取消图片加载
         */
        void cancelPhotoDialog();

    }

    interface Presenter extends IBasePresenter<View> {

    }
}
