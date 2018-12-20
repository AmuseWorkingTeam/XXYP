
package com.xxyp.xxyp.common.view.recyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xxyp.xxyp.common.utils.ScreenUtils;

/**
 * Description : recyclerView分割线 Created by sunpengfei on 2017/7/29. Person in
 * charge : sunpengfei
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;

    private Drawable mDivider;

    private int mDividerHeight;

    public DividerItemDecoration(Context context, @DrawableRes int resId) {
        this(context, context.getResources().getDrawable(resId), 0);
    }

    public DividerItemDecoration(Context context, @DrawableRes int resId, int dividerHeight) {
        this(context, context.getResources().getDrawable(resId), dividerHeight);
    }

    public DividerItemDecoration(Context context, @Nullable Drawable drawable) {
        this(context, drawable, 0);
    }

    public DividerItemDecoration(Context context, @Nullable Drawable drawable,
            int dividerHeight) {
        mContext = context;
        mDivider = drawable;
        if (dividerHeight > 0) {
            mDividerHeight = dividerHeight;
        }
        mDividerHeight = ScreenUtils.dp2px(0.3f);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)childView
                    .getLayoutParams();
            int top = childView.getBottom() + params.bottomMargin;
            int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mDividerHeight);
    }
}
