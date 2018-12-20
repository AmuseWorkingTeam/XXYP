
package com.xxyp.xxyp.message.customsviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import javax.annotation.Nullable;

/**
 * Description : 聊天页面图片item类型
 */
public class ChatPicImageView extends SimpleDraweeView {

    /* 聊天圆弧矩形path */
    private Path mRoundRectPath;

    /* 聊天发送者的指向path */
    private Path mPointPath;

    /* 遮罩的path */
    private Path mShadowPath;

    /* 聊天框体位置 */
    public enum Position{
        RIGHT, LEFT, CENTER
    }

    /* 聊天框体位置 */
    private Position mPosition = Position.CENTER;

    /* 圆角矩形半径 */
    private float mRoundRectRadius;

    /* 指向箭头的宽 */
    private int mPointWidth;

    /* 指向箭头的高 */
    private int mPointHeight;

    /* 指向箭头距顶部距离 */
    private int mPointPaddingTop;

    /* 聊天框体矩形 */
    private RectF mRectF;

    /* 遮罩的矩形 */
    private RectF mShadowRectF;

    /* shader画笔 */
    private Paint mShaderPaint;

    /* 遮罩的shader画笔*/
    private Paint mShaderShadowPaint;

    /* 资源id */
    private int mResource;

    /* 遮罩的颜色 */
    private int mShadowColor;

    /* 遮罩的高度 */
    private int mShadowHeight;
    
    /* 遮罩的圆角 */
    private float[] mShadowCorners;

    public ChatPicImageView(Context context) {
        this(context, null, 0);
    }

    public ChatPicImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatPicImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mRoundRectRadius = 12f;
        mPointWidth = 21;
        mPointHeight = 35;
        mPointPaddingTop = 45;
        mRoundRectPath = new Path();
        mPointPath = new Path();
        mRectF = new RectF();
        mShaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setShadow(@ColorInt int shadowColor, int shadowHeight){
        if(shadowHeight <= 0){
            return;
        }
        mShadowColor = shadowColor;
        mShadowHeight = shadowHeight;
        mShadowPath = new Path();
        mShadowRectF = new RectF();
        mShaderShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowCorners = new float[] {
                0f, 0f, 0f, 0f, mRoundRectRadius, mRoundRectRadius, mRoundRectRadius,
                mRoundRectRadius
        };
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        if (!isInEditMode()) {
//            createShader();
//            drawPath(canvas);
//        } else {
//            super.onDraw(canvas);
//        }
//    }

    public void setPosition(Position position) {
        mPosition = position;
    }

    /**
     * 画出聊天框体
     */
    private void drawPath(Canvas canvas){
        mRoundRectPath.reset();
        mPointPath.reset();

        int mWight = getMeasuredWidth();
        int mHeight = getMeasuredHeight();

        canvas.setDrawFilter(
                new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));
        if (mPosition == Position.CENTER) {
            // 中间位置
            mRectF.set(0, 0, mWight, mHeight);
            mRoundRectPath.addRoundRect(mRectF, mRoundRectRadius, mRoundRectRadius,
                    Path.Direction.CCW);
            mRoundRectPath.close();
            canvas.clipPath(mRoundRectPath);
            canvasShadow(canvas, mWight, mHeight);
            return;
        } else if (mPosition == Position.RIGHT) {
            // 右边位置
            mRectF.set(0, 0, mWight - mPointWidth, mHeight);

            mPointPath.moveTo(mWight - mPointWidth, mPointPaddingTop);
            mPointPath.lineTo(mWight, mPointPaddingTop + mPointHeight / 2);
            mPointPath.lineTo(mWight - mPointWidth, mPointPaddingTop + mPointHeight);
            mPointPath.close();
        } else if (mPosition == Position.LEFT) {
            // 左边位置
            mRectF.set(mPointWidth, 0, mWight, mHeight);

            mPointPath.moveTo(mPointWidth, mPointPaddingTop);
            mPointPath.lineTo(0, mPointPaddingTop + mPointHeight / 2);
            mPointPath.lineTo(mPointWidth, mPointPaddingTop + mPointHeight);
            mPointPath.close();
        }
        mRoundRectPath.addRoundRect(mRectF, mRoundRectRadius, mRoundRectRadius, Path.Direction.CCW);
        mRoundRectPath.close();
        if(mShaderPaint.getShader() != null){
            canvas.drawPath(mRoundRectPath, mShaderPaint);
            canvas.drawPath(mPointPath, mShaderPaint);
        }
        canvasShadow(canvas, mWight, mHeight);
    }

    /**
     * 绘制遮罩层
     */
    private void canvasShadow(Canvas canvas, int width, int height){
        if(mShadowPath == null || mShadowRectF == null || mShaderShadowPaint == null){
            return;
        }
        mShadowPath.reset();
        // 遮罩
        if(height - mShadowHeight <= 0){
            return;
        }
        if (mPosition == Position.CENTER) {
            // 中间位置
            mShadowRectF.set(0, height - mShadowHeight, width, height);
        } else if (mPosition == Position.RIGHT) {
            // 右边位置
            mShadowRectF.set(0, height - mShadowHeight, width - mPointWidth, height);
        } else if (mPosition == Position.LEFT) {
            // 左边位置
            mShadowRectF.set(mPointWidth, height - mShadowHeight, width, height);
        }
        mShadowPath.addRoundRect(mShadowRectF, mShadowCorners,
                Path.Direction.CCW);
        mShadowPath.close();
        if(mShaderShadowPaint.getShader() != null){
            canvas.drawPath(mShadowPath, mShaderShadowPaint);
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        if (mResource != resId) {
            mResource = resId;
            setImageDrawable(resolveResource());
        }
    }

    @Override
    public void setController(@Nullable DraweeController draweeController) {
        super.setController(draweeController);
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        setImageDrawable(new BitmapDrawable(getResources(), bm));
    }

    /**
     * 资源文件对应Drawable
     * @return Drawable
     */
    private Drawable resolveResource() {
        Resources rsrc = getResources();
        if (rsrc == null) {
            return null;
        }

        Drawable d = null;
        if (mResource != 0) {
            try {
                d = rsrc.getDrawable(mResource);
            } catch (Exception e) {
                // Don't try again.
                mResource = 0;
            }
        }
        return fromDrawable(d);
    }

    /**
     * 创建drawable
     * @param drawable imageView持有的drawable
     * @return Drawable
     */
    private Drawable fromDrawable(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                return drawable;
            } else if (drawable instanceof LayerDrawable) {
                LayerDrawable ld = (LayerDrawable) drawable;
                drawable = ld.getDrawable(0);
            }else if(drawable instanceof StateListDrawable){
                StateListDrawable stateListDrawable = (StateListDrawable) drawable;
                drawable = stateListDrawable.getCurrent();
            }

            if (!(drawable instanceof BitmapDrawable)){
                // try to get a bitmap from the drawable
                Bitmap bm = drawableToBitmap(drawable);
                if (bm != null) {
                    drawable = new BitmapDrawable(getResources(), bm);
                }
            }
        }
        return drawable;
    }

    /**
     * 创建shader
     */
    private void createShader() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawableToBitmap(drawable);
            if (null != bitmap){
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                        Shader.TileMode.CLAMP);
                shader.setLocalMatrix(getImageMatrix());
                mShaderPaint.setShader(shader);
            }
        }
        //绘制遮罩画笔
        if(mShaderShadowPaint != null){
            Drawable color = new ColorDrawable(mShadowColor);
            Bitmap bitmap = drawableToBitmap(color);
            if (null != bitmap){
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                        Shader.TileMode.CLAMP);
                shader.setLocalMatrix(getImageMatrix());
                mShaderShadowPaint.setShader(shader);
            }
        }
    }

    /**
     * 获取drawable转bitmap
     * @param drawable imageView持有的drawable
     * @return Bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        try {
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            } else {
                int width = Math.max(drawable.getIntrinsicWidth(), 2);
                int height = Math.max(drawable.getIntrinsicHeight(), 2);
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }
}
