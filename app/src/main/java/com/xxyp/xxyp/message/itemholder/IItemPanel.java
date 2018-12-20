
package com.xxyp.xxyp.message.itemholder;

import android.view.View;
import android.view.ViewGroup;

/**
 * Description : item控制 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public interface IItemPanel<T> {

    View obtainView(ViewGroup parent);

    void bindData(T t);
}
