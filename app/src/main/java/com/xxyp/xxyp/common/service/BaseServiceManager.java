
package com.xxyp.xxyp.common.service;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xxyp.xxyp.common.bean.MetaBean;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.net.RetrofitServiceManager;
import com.xxyp.xxyp.common.net.RxError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Description : 网络请求基础调用管理类 Created by sunpengfei on 2017/7/27. Person in
 * charge : sunpengfei
 */
public class BaseServiceManager {

    protected static RetrofitServiceManager mManager = RetrofitServiceManager.getInstance();

    protected static <T> Observable<T> toObservable(ResponseBody responseBody, Class<T> clazz) {
        if (responseBody == null) {
            return Observable.error(RxError.create(RxError.ERROR_UNKNOWN, RxError.ERROR_UNKNOWN));
        }
        try {
            String body = responseBody.string();
            if (TextUtils.isEmpty(body)) {
                return Observable
                        .error(RxError.create(RxError.ERROR_UNKNOWN, RxError.ERROR_UNKNOWN));
            }
            XXLog.log_d("BaseServiceManager", body);

            JSONObject jsonObject = new JSONObject(body);
            MetaBean metaBean = null;
            if (jsonObject.has("meta")) {
                metaBean = new Gson().fromJson(jsonObject.getString("meta"), MetaBean.class);
            }
            if (metaBean == null) {
                return Observable
                        .error(RxError.create(RxError.ERROR_UNKNOWN, RxError.ERROR_UNKNOWN));
            }
            if (metaBean.getCode() == 0 && jsonObject.has("data")) {
                return Observable.just(new Gson().fromJson(jsonObject.getString("data"), clazz));
            }
            return Observable.error(RxError.create(RxError.ERROR_TYPE_DATA, metaBean.getCode(),
                    metaBean.getMessage()));

        } catch (IOException e) {
            XXLog.log_e("BaseServiceManager", e, "");
        } catch (JsonSyntaxException e) {
            XXLog.log_e("BaseServiceManager", e, "");
        } catch (JSONException e) {
            XXLog.log_e("BaseServiceManager", e, "");
        }
        return Observable.empty();
    }

}
