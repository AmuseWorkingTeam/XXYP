
package com.xxyp.xxyp.user.model;

import android.text.TextUtils;

import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.publish.db.PublishDBManager;
import com.xxyp.xxyp.user.bean.UserShotListBean;
import com.xxyp.xxyp.user.contract.MyDatingShotContract;
import com.xxyp.xxyp.user.service.UserServiceManager;

import java.util.List;

import rx.Observable;

/**
 * Description : 我的约拍model
 * Created by sunpengfei on 2017/8/11.
 * Person in charge : sunpengfei
 */
public class MyDatingShotModel implements MyDatingShotContract.Model {

    @Override
    public List<ShotBean> getMyShotInfo() {
        return PublishDBManager.getInstance().getShotInfo();
    }


    @Override
    public Observable<UserShotListBean> getDatingShot() {
        String userId = SharePreferenceUtils.getInstance().getUserId();
        if (TextUtils.isEmpty(userId)) {
            return Observable.just(null);
        }
        return UserServiceManager.getDatingShot(userId, null);
    }




}
