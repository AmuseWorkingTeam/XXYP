
package com.xxyp.xxyp.main.service;

import com.xxyp.xxyp.common.service.BaseServiceManager;
import com.xxyp.xxyp.main.bean.WorkHotListBean;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Description : 首页热门接口管理类 Created by sunpengfei on 2017/7/31. Person in charge
 * : sunpengfei
 */
public class MainServiceManager extends BaseServiceManager {

    /**
     * 获取首页热门数据
     * 
     * @return Observable<WorkHotListBean>
     */
    public static Observable<WorkHotListBean> getHotWorks() {
        return mManager.create(MainService.class).getHotWorks()
                .flatMap(new Func1<ResponseBody, Observable<WorkHotListBean>>() {
                    @Override
                    public Observable<WorkHotListBean> call(ResponseBody responseBody) {
                        return toObservable(responseBody, WorkHotListBean.class);
                    }
                });
    }
}
