
package com.xxyp.xxyp.message.itemholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Description : 基础holder Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class BaseHolder<T> extends RecyclerView.ViewHolder {

    protected IItemPanel<T> mPanel;

    public BaseHolder(View view, IItemPanel<T> panel) {
        super(view);
        mPanel = panel;
    }

    public void setData(T data) {
        mPanel.bindData(data);
    }
}
