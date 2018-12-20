
package com.xxyp.xxyp.common.base;


import android.content.Context;

/**
 * Description : 基础view Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public interface IBaseView<T> {

    Context getContext();

    void setPresenter(T t);
}
