
package com.xxyp.xxyp.user.model;

import android.text.TextUtils;

import com.xxyp.xxyp.user.bean.FansFocusBean;
import com.xxyp.xxyp.user.contract.TotalFansFocusContract;
import com.xxyp.xxyp.user.service.UserServiceManager;

import java.util.List;

import rx.Observable;

/**
 * Description : 全部粉丝关注model
 * Created by sunpengfei on 2017/10/19.
 * Person in charge : sunpengfei
 */
public class TotalFansFocusModel implements TotalFansFocusContract.Model {

    @Override
    public Observable<List<FansFocusBean>> obtainTotalFans(String userId) {
        if(TextUtils.isEmpty(userId)){
            return Observable.just(null);
        }
        return UserServiceManager.getFans(null, userId);
    }

    @Override
    public Observable<List<FansFocusBean>> obtainTotalFocus(String userId) {
        if(TextUtils.isEmpty(userId)){
            return Observable.just(null);
        }
        return UserServiceManager.getFans(userId, null);
    }
}
