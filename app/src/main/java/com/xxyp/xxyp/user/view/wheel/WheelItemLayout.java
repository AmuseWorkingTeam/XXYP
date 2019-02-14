
package com.xxyp.xxyp.user.view.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Description : 滚轮每个选项动画
 */
public class WheelItemLayout extends LinearLayout {

    private int mDelta;

    private int mCenterY;

    /** Default text color */
    // public static final int DEFAULT_TEXT_COLOR = 0xFF777E8C;

    // private int radian = WheelView.DEFAULT_RADIAN;

    // Text settings
    // private int textColor = DEFAULT_TEXT_COLOR;

    /** current text color */
    // public static final int CURRENT_COLOR = 0xFF000000;

    public WheelItemLayout(Context context) {
        this(context, null);
    }

    public WheelItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void draw(Canvas canvas, int deltaY, int centerY) {
        mDelta = deltaY;
        mCenterY = centerY;
        // this.radian = radian;
        super.draw(canvas);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        float multipleScale = 0.5f;
        float childCenterY = child.getTop() + child.getMeasuredHeight() / 2 - mDelta;
        // child移动位移
        // float dx = Math.abs(childCenterY - mCenterY) *
        // child.getMeasuredHeight()
        // / (float)getMeasuredHeight();
        int saveCount = canvas.save();
        if (child instanceof TextView) {
            TextView text = (TextView)child;
            float scale = 1 - Math.abs(childCenterY - mCenterY) * multipleScale / mCenterY;
            // text.setScaleX(scale);
            text.setScaleY(scale);
            // text.setTextColor(textColor);
        }
        // child画布移动
        // switch (radian) {
        // case WheelView.DEFAULT_RADIAN:
        // canvas.translate(0, 0);
        // break;
        // case WheelView.DEFAULT_LEFT_RADIAN:
        // canvas.translate(dx, 0);
        // break;
        // case WheelView.DEFAULT_RIGHT_RADIAN:
        // canvas.translate(-dx, 0);
        // break;
        // default:
        // break;
        // }

        boolean ret = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(saveCount);
        return ret;
    }

}
