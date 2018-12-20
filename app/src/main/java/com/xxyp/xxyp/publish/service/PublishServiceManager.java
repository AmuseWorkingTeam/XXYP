
package com.xxyp.xxyp.publish.service;

import com.xxyp.xxyp.common.service.BaseServiceManager;
import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.main.bean.WorkBean;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Description : 发布接口管理类 Created by sunpengfei on 2017/8/2. Person in charge :
 * sunpengfei
 */
public class PublishServiceManager extends BaseServiceManager {

    /**
     * 发布作品
     * @param workBean 作品信息
     * @return Observable<Object>
     */
    public static Observable<String> createWork(WorkBean workBean) {
        if (workBean == null || workBean.getList() == null) {
            return Observable.empty();
        }
        return mManager.create(PublishService.class).createWorks(workBean)
                .flatMap(new Func1<ResponseBody, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(ResponseBody responseBody) {
                        return toObservable(responseBody, Object.class);
                    }
                }).map(new Func1<Object, String>() {
                    @Override
                    public String call(Object o) {
                        if (o == null) {
                            return null;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(o.toString());
                            if (jsonObject.has("worksId")) {
                                return jsonObject.getInt("worksId") + "";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
    }

    /**
     * 发布约拍
     * @param shotBean 约拍信息
     * @return Observable<String>
     */
    public static Observable<String> createDatingShot(ShotBean shotBean) {
        if (shotBean == null || shotBean.getDatingShotImages() == null) {
            return Observable.empty();
        }
        return mManager.create(PublishService.class).createDatingShot(shotBean)
                .flatMap(new Func1<ResponseBody, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(ResponseBody responseBody) {
                        return toObservable(responseBody, Object.class);
                    }
                }).map(new Func1<Object, String>() {
                    @Override
                    public String call(Object o) {
                        if (o == null) {
                            return null;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(o.toString());
                            if (jsonObject.has("datingShotId")) {
                                return jsonObject.getInt("datingShotId") + "";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
    }
}
