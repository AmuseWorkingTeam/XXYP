
package com.xxyp.xxyp.find.presenter;

import com.xxyp.xxyp.find.contract.PhotoDetailContract;

/**
 * Description : 作品或约拍图片详情presenter
 * Created by sunpengfei on 2017/9/6.
 * Person in charge : sunpengfei
 */
public class PhotoDetailPresenter implements PhotoDetailContract.Presenter {

    private PhotoDetailContract.View mView;

    public PhotoDetailPresenter(PhotoDetailContract.View view) {
        mView = view;
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
    }
}
