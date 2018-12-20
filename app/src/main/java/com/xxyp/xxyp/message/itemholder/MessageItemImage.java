
package com.xxyp.xxyp.message.itemholder;


import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.drawee.controller.BaseControllerListener;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ImageUtils;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.message.bean.MessageImageBean;
import com.xxyp.xxyp.message.customsviews.ChatPicImageView;
import com.xxyp.xxyp.message.interfaces.ChatActionListener;

import java.io.File;

import javax.annotation.Nullable;

/**
 * Description : 消息图片类型 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageItemImage extends MessageItemBaseView {

    private ChatPicImageView mImageView;

    /* 图片 */
    protected ImageRequestConfig mImageConfig;

    public MessageItemImage(@NonNull Context context, ChatActionListener listener, int itemPos) {
        super(context, listener, itemPos);
        mImageConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(300))
                .setLoadHeight(ScreenUtils.dp2px(300)).setRender(true).build();
    }


    @Override
    protected View initView(ViewGroup parent) {
        View view;
        if(mItemPos == ITEM_LEFT){
            view = View.inflate(mContext, R.layout.item_chat_image_left, parent);
            mImageView = ((ChatPicImageView) view.findViewById(R.id.image_message));

        }else{
            view = View.inflate(mContext, R.layout.item_chat_image_right, parent);
            mImageView = ((ChatPicImageView) view.findViewById(R.id.image_message));
        }
        mImageView.setPosition(ChatPicImageView.Position.CENTER);
        return view;
    }

    @Override
    protected void bindData() {
        fillView();
    }

    @Override
    protected void setItemViewListener() {
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mActionListener != null){
                    mActionListener.onClickToBigImgListener(v, mChatMessageBean);
                }
            }
        });
    }

    /**
     * 填充数据
     */
    private void fillView() {
        setItemViewLongClick(mImageView);
        showPic();
    }

    /**
     * 展示图片
     */
    private void showPic(){
        if (mChatMessageBean == null || (mChatMessageBean.getImageBean() == null && mChatMessageBean.getBodyBean() == null)) {
            ImageLoader.getInstance().display(mImageView, "");
            return;
        }
        String imgWidth = null, imgHeight = null;
        FrameLayout.LayoutParams imgLayoutParams = null;
        if (mChatMessageBean.getImageBean() != null) {
            imgWidth = mChatMessageBean.getImageBean().getImageWidth() + "";
            imgHeight = mChatMessageBean.getImageBean().getImageHeight() + "";
        } else if (mChatMessageBean.getBodyBean() != null) {
            imgWidth = mChatMessageBean.getBodyBean().getW();
            imgHeight = mChatMessageBean.getBodyBean().getH();
        }
        double maxLength = ScreenUtils.widthPixels * 0.5;
        double minLength = ScreenUtils.widthPixels * 0.5 * 0.5;
        if (!TextUtils.isEmpty(imgWidth) && !TextUtils.isEmpty(imgHeight)) {
            double limitDiff = maxLength / minLength;
            double diff = 0;
            double w = 0, h = 0;
            try {
                w = Double.valueOf(imgWidth);
                h = Double.valueOf(imgHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (w > h) {
                // 图片角度是横
                diff = w / h;
                if (diff < limitDiff) {
                    // 宽高比不超过标准宽高比 小的一边按比例展示 大的一边标准长
                    imgLayoutParams = new FrameLayout.LayoutParams((int) maxLength,
                            (int) (maxLength / diff));
                } else {
                    // 宽高比大于最大宽高比 按最大宽高比展示
                    imgLayoutParams = new FrameLayout.LayoutParams((int) maxLength,
                            (int) minLength);
                }
            } else if (w == h) {
                imgLayoutParams = new FrameLayout.LayoutParams((int) maxLength,
                        (int) maxLength);
            } else {
                diff = h / w;
                if (diff < limitDiff) {
                    // 宽高比不超过标准宽高比 小的一边按比例展示 大的一边标准长
                    imgLayoutParams = new FrameLayout.LayoutParams((int) (maxLength / diff),
                            (int) maxLength);
                } else {
                    // 宽高比大于最大宽高比 按最大宽高比展示
                    imgLayoutParams = new FrameLayout.LayoutParams(
                            (int) minLength, (int) maxLength);
                }
            }
        } else {
            // 宽高比超过标准宽高比 按标准展示
            imgLayoutParams = new FrameLayout.LayoutParams((int) maxLength,
                    (int) (maxLength * 3 / 4));
        }
        mImageView.setLayoutParams(imgLayoutParams);
        String picUrl = null;
        MessageImageBean imgInfo = mChatMessageBean.getImageBean();
        if (imgInfo != null) {
            if (!TextUtils.isEmpty(imgInfo.getBigImagePath())
                    && new File(imgInfo.getBigImagePath()).exists()) {
                picUrl = "file://" + imgInfo.getBigImagePath();
            } else if (!TextUtils.isEmpty(imgInfo.getLocalImagePath())
                    && new File(imgInfo.getLocalImagePath()).exists()) {
                picUrl = "file://" + imgInfo.getLocalImagePath();
            } else if(!TextUtils.isEmpty(imgInfo.getThumbImageUrl())){
                // 本地无此照片 展示网络地址
                picUrl = imgInfo.getThumbImageUrl();
            } else if(!TextUtils.isEmpty(imgInfo.getImageUrl())){
                picUrl = ImageUtils.getImgThumbUrl(imgInfo.getImageUrl());
            }
        }else if(mChatMessageBean.getBodyBean() != null){
            picUrl = ImageUtils.getImgThumbUrl(mChatMessageBean.getBodyBean().getUrl());
        }
        ImageLoader.getInstance().display(mImageView, picUrl, mImageConfig,
                new BaseControllerListener() {
                    @Override
                    public void onFailure(String id, Throwable throwable) {

                    }

                    @Override
                    public void onFinalImageSet(String id, @Nullable Object imageInfo,
                            @Nullable Animatable animatable) {
                    }
                });
    }

}
