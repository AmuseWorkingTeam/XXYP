
package com.xxyp.xxyp.common.view;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Description : 头像imageView
 * Created by sunpengfei on 2017/8/22.
 * Person in charge : sunpengfei
 */
public class ShapeImageView extends SimpleDraweeView {

    public ShapeImageView(Context context) {
        this(context, null);
    }

    public ShapeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
