
package com.xxyp.xxyp.common.view.recyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xxyp.xxyp.common.utils.ScreenUtils;

/**
 * Description : gridView分割线 Created by sunpengfei on 2017/7/29. Person in
 * charge : sunpengfei
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;

    private Drawable mDivider;

    private int mDividerHeight;

    public DividerGridItemDecoration(Context context, @DrawableRes int resId) {
        this(context, context.getResources().getDrawable(resId), 0);
    }

    public DividerGridItemDecoration(Context context, @DrawableRes int resId, int dividerHeight) {
        this(context, context.getResources().getDrawable(resId), dividerHeight);
    }

    public DividerGridItemDecoration(Context context, @Nullable Drawable drawable) {
        this(context, drawable, 0);
    }

    public DividerGridItemDecoration(Context context, @Nullable Drawable drawable,
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
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {
        int spanCount = ((GridLayoutManager)parent.getLayoutManager()).getSpanCount();
        int childCount = parent.getAdapter().getItemCount();
        int position = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        if (isLastRaw(parent, position, spanCount, childCount)) {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            return;
        }
        if (isLastColumn(parent, position, spanCount, childCount)) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            return;
        }
        outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
    }

    /**
     * 横的分割线
     * 
     * @param canvas canvas
     * @param recyclerView recyclerView
     */
    public void drawHorizontal(Canvas canvas, RecyclerView recyclerView) {
        int count = recyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = recyclerView.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)childView
                    .getLayoutParams();
            int left = childView.getLeft() - params.leftMargin;
            int top = childView.getBottom() + params.bottomMargin;
            int right = childView.getRight() + params.rightMargin;
            int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    /**
     * 竖的分割线
     * @param canvas canvas
     * @param recyclerView recyclerView
     */
    public void drawVertical(Canvas canvas, RecyclerView recyclerView) {
        int count = recyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = recyclerView.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)childView
                    .getLayoutParams();
            int left = childView.getRight() + params.rightMargin;
            int top = childView.getTop() - params.topMargin;
            int right = left + mDividerHeight;
            int bottom = childView.getBottom() + params.bottomMargin;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    /**
     * 是否是最后一列
     * 
     * @return boolean
     */
    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
        return (pos + 1) % spanCount == 0;
    }

    /**
     * 是否是最后一行
     * 
     * @return boolean
     */
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        return pos >= childCount - childCount % spanCount;
    }
}
