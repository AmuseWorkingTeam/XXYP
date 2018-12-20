
package com.xxyp.xxyp.publish.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.recyclerView.BaseViewHolder;
import com.xxyp.xxyp.common.view.recyclerView.RecyclerWithHFAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 发布页面adapter Created by sunpengfei on 2017/8/2. Person in charge
 * : sunpengfei
 */
public class PublishAdapter extends RecyclerWithHFAdapter {

    private ImageRequestConfig mConfig;

    private Context mContext;

    private OnPublishPicListener mListener;

    private List<String> mPics;

    public PublishAdapter(Context context) {
        mContext = context;
        mPics = new ArrayList();
        mPics.add("-1");
        mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.widthPixels / 3)
                .setLoadHeight(ScreenUtils.widthPixels / 3).build();
    }

    @Override
    protected int getAdapterCount() {
        return mPics != null ? mPics.size() : 0;
    }

    public void setPics(List<String> pics) {
        if(pics == null || pics.size() == 0){
            return;
        }
        mPics.addAll(mPics.size() - 1, pics);
        if (mPics.size() > 9) {
            mPics = mPics.subList(0, 9);
        }
        notifyDataSetChanged();
    }

    public List<String> getPics() {
        if (mPics == null || mPics.size() == 0) {
            return null;
        }
        if (TextUtils.equals(mPics.get(mPics.size() - 1), "-1")) {
            return mPics.subList(0, mPics.size() - 1);
        }
        return mPics;
    }
    
    public void remove(String pic) {
        if (!TextUtils.isEmpty(pic) && mPics != null && mPics.remove(pic)) {
            notifyDataSetChanged();
        }
    }

    public void setOnPublishListener(OnPublishPicListener listener) {
        mListener = listener;
    }

    public int getPicsSize() {
        return getAdapterCount() - 1;
    }

    @Override
    protected BaseViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_publish, parent, false));
    }

    @Override
    protected void onBindHolder(BaseViewHolder holder, int position) {
        RelativeLayout relativeLayout = holder
                .findViewById(R.id.rl_publish_pic);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)relativeLayout
                .getLayoutParams();
        params.width = (ScreenUtils.widthPixels / 3);
        params.height = (ScreenUtils.widthPixels / 3);
        relativeLayout.setLayoutParams(params);
        SimpleDraweeView image = holder
                .findViewById(R.id.item_publish_pic);
        View choosePic = holder.findViewById(R.id.item_pic_choose);
        ImageView deletePic = holder.findViewById(R.id.delete_image_item);
        if (mPics == null || mPics.size() == 0) {
            return;
        }
        final String pic = mPics.get(position);
        if (TextUtils.equals("-1", pic)) {
            image.setVisibility(View.GONE);
            deletePic.setVisibility(View.GONE);
            choosePic.setVisibility(View.VISIBLE);
        }else{
            image.setVisibility(View.VISIBLE);
            deletePic.setVisibility(View.VISIBLE);
            choosePic.setVisibility(View.GONE);
            ImageLoader.getInstance().display(image, "file://" + pic, mConfig);
        }
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener == null) {
                    return;
                }
                if(TextUtils.equals("-1", pic)) {
                    mListener.addPicListener();
                }
            }
        });
        deletePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.deletePicListener(pic);
                }
            }
        });
    }

    public interface OnPublishPicListener {

        void addPicListener();

        void deletePicListener(String pic);
    }
}
