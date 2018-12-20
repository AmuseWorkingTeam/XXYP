
package com.xxyp.xxyp.user.model;

import android.text.TextUtils;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.user.contract.PersonalCenterContract;
import com.xxyp.xxyp.user.provider.UserProvider;

import rx.Observable;

/**
 * Description : 个人中心model Created by sunpengfei on 2017/8/2. Job number：135182
 * Phone ：18510428121 Email：sunpengfei@syswin.com Person in charge : sunpengfei
 * Leader：wangxiaohui
 */
public class PersonalModel implements PersonalCenterContract.Model {

    @Override
    public Observable<UserInfo> getMyUserInfo() {
        String userId = SharePreferenceUtils.getInstance().getUserId();
        if (TextUtils.isEmpty(userId)) {
            return Observable.just(null);
        }
        return UserProvider.obtainUserInfo(userId);
    }

}
