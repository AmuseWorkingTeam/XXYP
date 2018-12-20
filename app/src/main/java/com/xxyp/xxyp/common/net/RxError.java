
package com.xxyp.xxyp.common.net;

/**
 * Description : Rxjava调用返回的错误 Created by sunpengfei on 2017/7/27. Person in
 * charge : sunpengfei
 */
public class RxError extends Exception {

    /**
     * 错误类型-未知错误
     */
    public static final int ERROR_UNKNOWN = -1;

    /**
     * 错误类型-数据错误
     */
    public static final int ERROR_TYPE_DATA = 1;

    /**
     * 错误类型-通用错误 包含（数据错误，网络错误）
     */
    public static final int ERROR_TYPE_COMMON = 2;

    public int errorCode = ERROR_UNKNOWN;

    public int type = ERROR_TYPE_COMMON;

    public RxError() {
    }

    private RxError(String message) {
        super(message);
    }

    private RxError(String message, Throwable throwable) {
        super(message, throwable);
    }

    private RxError(Throwable throwable) {
        super(throwable);
    }

    public static RxError create(int type, int errorCode) {
        return create(type, errorCode, "", null);
    }

    public static RxError create(int type, int errorCode, String paramString) {
        return create(type, errorCode, paramString, null);
    }

    public static RxError create(int type, int errorCode, Throwable throwable) {
        return create(type, errorCode, "", throwable);
    }

    public static RxError create(int type, int errorCode, String message, Throwable throwable) {
        RxError error = new RxError(message, throwable);
        error.type = type;
        error.errorCode = errorCode;
        return error;
    }
}
