
package com.xxyp.xxyp.user.model;

import android.text.TextUtils;

import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.user.bean.CreateFansInput;
import com.xxyp.xxyp.user.bean.UserWorkListBean;
import com.xxyp.xxyp.user.contract.FrameContract;
import com.xxyp.xxyp.user.service.UserServiceManager;
import com.xxyp.xxyp.user.utils.FrameConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Description : frame model Created by sunpengfei on 2017/8/2. Person in charge
 * : sunpengfei
 */
public class FrameModel implements FrameContract.Model {
    @Override
    public Observable<UserWorkListBean> getWorks(String userId) {
        return UserServiceManager.getWorks(userId, null);

    }

    @Override
    public Observable<Object> focusUser(String userId) {
        CreateFansInput input = new CreateFansInput();
        input.setFromUserId(SharePreferenceUtils.getInstance().getUserId());
        input.setToUserId(userId);
        return UserServiceManager.createFans(input);
    }

    @Override
    public Observable<Map<String, Integer>> getFansCount(String useId) {
        if (TextUtils.isEmpty(useId)) {
            return Observable.just(null);
        }
        return UserServiceManager.getFansCount(useId)
                .map(new Func1<Object, Map<String, Integer>>() {
                    @Override
                    public Map<String, Integer> call(Object o) {
                        if (o == null) {
                            return null;
                        }
                        try {
                            Map<String, Integer> map = new HashMap<>();
                            JSONObject jsonObject = new JSONObject(o.toString());
                            if (jsonObject.has(FrameConfig.FOLLOW_COUNT)) {
                                map.put(FrameConfig.FOLLOW_COUNT,
                                        jsonObject.getInt(FrameConfig.FOLLOW_COUNT));
                            }
                            if (jsonObject.has(FrameConfig.FANS_COUNT)) {
                                map.put(FrameConfig.FANS_COUNT,
                                        jsonObject.getInt(FrameConfig.FANS_COUNT));
                            }
                            return map;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
    }
}
