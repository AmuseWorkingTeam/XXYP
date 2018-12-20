package com.xxyp.xxyp.message.customsviews;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Description : （类描述，描述当前类具体作用和功能）
 * Created by sunpengfei on 2018/4/2.
 * Job number：135182
 * Phone ：18510428121
 * Email：sunpengfei@syswin.com
 * Person in charge : sunpengfei
 * Leader：wangxiaohui
 */
public class MessagePicDrawable extends Drawable {

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
    private ChatPicImageView.Position mPosition = ChatPicImageView.Position.CENTER;

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

    /* shader画笔 */
    private Paint mShaderPaint;

    private int mWidth, mHeight;

    public MessagePicDrawable(@NonNull Bitmap bitmap, int width, int height){
        init();
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();
        createShader(bitmap);
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

    @Override
    public void draw(@NonNull Canvas canvas) {
        drawPath(canvas);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    /**
     * 创建shader
     */
    private void createShader(Bitmap bitmap) {
        if (null != bitmap){
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            mShaderPaint.setShader(shader);
        }
    }

    public void setPosition(ChatPicImageView.Position position) {
        mPosition = position;
    }

    /**
     * 画出聊天框体
     */
    private void drawPath(Canvas canvas){
        mRoundRectPath.reset();
        mPointPath.reset();

        canvas.setDrawFilter(
                new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));
        if (mPosition == ChatPicImageView.Position.CENTER) {
            // 中间位置
            mRectF.set(0, 0, mWidth, mHeight);
            mRoundRectPath.addRoundRect(mRectF, mRoundRectRadius, mRoundRectRadius,
                    Path.Direction.CCW);
            mRoundRectPath.close();
            canvas.clipPath(mRoundRectPath);
            return;
        } else if (mPosition == ChatPicImageView.Position.RIGHT) {
            // 右边位置
            mRectF.set(0, 0, mWidth - mPointWidth, mHeight);

            mPointPath.moveTo(mWidth - mPointWidth, mPointPaddingTop);
            mPointPath.lineTo(mWidth, mPointPaddingTop + mPointHeight / 2);
            mPointPath.lineTo(mWidth - mPointWidth, mPointPaddingTop + mPointHeight);
            mPointPath.close();
        } else if (mPosition == ChatPicImageView.Position.LEFT) {
            // 左边位置
            mRectF.set(mPointWidth, 0, mWidth, mHeight);

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
    }
}
