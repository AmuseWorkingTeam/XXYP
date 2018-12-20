
package com.xxyp.xxyp.main.service;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Description : 首页service Created by sunpengfei on 2017/7/31. Person in charge
 * : sunpengfei
 */
public interface MainService {

    String GET_HOT_WORKS = "getHotWorks";

    /**
     * 获取热门数据
     * 
     * @return Observable<ResponseBody>
     */
    @GET(GET_HOT_WORKS)
    Observable<ResponseBody> getHotWorks();
}
