
package com.xxyp.xxyp.common.net;

import java.io.IOException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description : 网络请求拦截器 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public class RequestInterceptor implements Interceptor {

    private Map<String, String> mHeaders;

    public RequestInterceptor() {
        mHeaders = NetUtils.buildHeader();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder();
        builder.method(oldRequest.method(), oldRequest.body());
        if (mHeaders != null && mHeaders.size() > 0) {
            builder.headers(Headers.of(mHeaders));
        }
        return chain.proceed(builder.build());
    }

    public static class Builder {
        private RequestInterceptor mRequestInterceptor = new RequestInterceptor();

        public Builder addHeaderParams(String key, double value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, float value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, int value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, long value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, String value) {
            mRequestInterceptor.mHeaders.put(key, value);
            return this;
        }

        public Builder addHeaderParams(Map<String, String> headers) {
            mRequestInterceptor.mHeaders.putAll(headers);
            return this;
        }

        public RequestInterceptor build() {
            return mRequestInterceptor;
        }
    }
}
