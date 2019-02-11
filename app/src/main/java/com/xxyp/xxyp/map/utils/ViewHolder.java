package com.xxyp.xxyp.map.utils;

import android.util.SparseArray;
import android.view.View;

public class ViewHolder {

    public ViewHolder() {
    }

    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray();
            view.setTag(viewHolder);
        }

        View childView = (View) viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        return (T) childView;
    }
}
