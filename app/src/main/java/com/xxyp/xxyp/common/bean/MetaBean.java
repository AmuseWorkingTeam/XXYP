
package com.xxyp.xxyp.common.bean;

/**
 * Description : 网络接口返回基础实体
 * Created by sunpengfei on 2017/7/27.
 * Person in charge : sunpengfei
 */
public class MetaBean extends BaseBean {
    private int code;

    private String message;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
