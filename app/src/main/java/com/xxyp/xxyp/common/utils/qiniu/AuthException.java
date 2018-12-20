
package com.xxyp.xxyp.common.utils.qiniu;

/**
 */

public class AuthException extends Exception {

    private static final long serialVersionUID = 1L;

    protected AuthException() {
    }

    public AuthException(String detailMessage) {
        super(detailMessage);
    }

    public AuthException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AuthException(Throwable throwable) {
        super(throwable);
    }
}
