
package com.xxyp.xxyp.common.view.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Description : 基础holder Created by sunpengfei on 2017/7/29. Person in charge :
 * sunpengfei
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private View mView;

    private SparseArray<View> mViews;

    public BaseViewHolder(View view) {
        super(view);
        this.mView = view;
        this.mViews = new SparseArray();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(int id) {
        T t = (T)mViews.get(id);
        if (t == null) {
            t = (T)mView.findViewById(id);
            mViews.put(id, t);
        }
        return t;
    }
}
