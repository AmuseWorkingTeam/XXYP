
package com.xxyp.xxyp.common.view.recyclerView.layoutManager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Description : 带header footer的gridLayoutManager Created by sunpengfei on
 * 2017/7/29. Person in charge : sunpengfei
 */
public class HeaderFooterGridLayoutManager extends GridLayoutManager {

    public HeaderFooterGridLayoutManager(Context context, final int spanCount, final boolean header,
                                         final boolean footer) {
        super(context, spanCount);
        setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (header && position == 0) {
                    return spanCount;
                }
                if (footer && position == (getItemCount() - 1)) {
                    return spanCount;
                }
                return 1;
            }
        });
    }

}
