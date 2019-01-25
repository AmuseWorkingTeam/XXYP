
package com.xxyp.xxyp.common.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description : 网络调用管理类 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public class RetrofitServiceManager {

    /* 默认请求超时时间 */
    private static int DEFAULT_TIME_OUT = 5;

    /* 默认读取超时时间 */
    private static int DEFAULT_READ_TIME_OUT = 10;

    /* 默认写入超时时间 */
    private static int DEFAULT_WRITE_TIME_OUT = 10;

    private static RetrofitServiceManager mInstance;

    private static String mBaseUrl = "http://172.31.238.63:8090";

    private Retrofit mRetrofit;

    public static RetrofitServiceManager getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitServiceManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitServiceManager();
                }
            }
        }
        return mInstance;
    }

    private RetrofitServiceManager() {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        //增加拦截器 增加请求头信息
        builder.addInterceptor(new RequestInterceptor.Builder().build());

//        //log拦截器
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.addInterceptor(loggingInterceptor);

        mRetrofit = new Retrofit.Builder().client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mBaseUrl).build();
    }

    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}
