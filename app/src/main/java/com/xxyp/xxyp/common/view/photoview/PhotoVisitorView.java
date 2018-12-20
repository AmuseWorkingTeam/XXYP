
package com.xxyp.xxyp.common.view.photoview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.bean.PhotoViewBean;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ImageUtils;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.ImageLoader;

import java.io.File;

import javax.annotation.Nullable;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.OnViewTapListener;

/**
 * Description : 查看图片view
 * Created by sunpengfei on 2017/7/18.
 * Person in charge : sunpengfei
 */
public class PhotoVisitorView extends RelativeLayout {

    private Context mContext;

    /* 普通图原图 */
    private PhotoView mPhotoView;

    /* 图片信息 */
    private PhotoViewBean mPhotoViewBean;

    /* 图片配置 */
    private ImageRequestConfig mConfig;

    public PhotoVisitorView(Context context) {
        this(context, null);
    }

    public PhotoVisitorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private PhotoVisitorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.widthPixels)
                .setLoadHeight(ScreenUtils.heightPixels).setRender(true).build();
        initView(context);
    }

    public void setPhotoViewBean(PhotoViewBean photoViewBean, int position) {
        mPhotoViewBean = photoViewBean;
        showPhoto(position);
    }

    /**
     * 初始化view
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        View view = inflate(context, R.layout.photo_visitor_view, this);
        mPhotoView = (PhotoView) view.findViewById(R.id.photoView);
    }

    /**
     * 展示图片
     */
    private void showPhoto(final int position) {
        mPhotoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                onBack();
            }
        });

        mPhotoView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                onBack();
            }
        });
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPhotoView.setLayoutParams(params);

        String loadUrl = null;
        if (!TextUtils.isEmpty(mPhotoViewBean.getLocalPath())
                && new File(mPhotoViewBean.getLocalPath()).exists()) {
            loadUrl = "file://" + mPhotoViewBean.getLocalPath();
        } else if (!TextUtils.isEmpty(mPhotoViewBean.getHttpUrl())
                && mPhotoViewBean.getHttpUrl().toUpperCase().startsWith("HTTP")) {
            loadUrl = ImageUtils.getImgThumbUrl(mPhotoViewBean.getHttpUrl(), 0, 0);
        }
        mPhotoView.setVisibility(View.VISIBLE);

        ImageLoader.getInstance().display(mPhotoView, loadUrl, new BaseControllerListener<ImageInfo>(){

            @Override
            public void onFailure(String id, Throwable throwable) {
                //加载成功
            }

            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                mPhotoView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
    }

    /**
     * 返回
     */
    private void onBack(){
        ((Activity)mContext).finish();
        ((Activity)mContext).overridePendingTransition(0, R.anim.popwindow_alpha_out);
    }

}
