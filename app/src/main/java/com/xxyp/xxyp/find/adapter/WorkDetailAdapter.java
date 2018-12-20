
package com.xxyp.xxyp.find.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ImageUtils;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.recyclerView.BaseViewHolder;
import com.xxyp.xxyp.common.view.recyclerView.RecyclerWithHFAdapter;
import com.xxyp.xxyp.main.bean.WorkPhotoBean;

import java.util.List;

/**
 * Description : 作品详情adapter Created by sunpengfei on 2017/7/31. Person in
 * charge : sunpengfei
 */
public class WorkDetailAdapter extends RecyclerWithHFAdapter {

    private ImageRequestConfig mConfig;

    private Context mContext;

    /* 作品列表 */
    private List<WorkPhotoBean> mWorkPhotoList;

    public WorkDetailAdapter(Context context) {
        mContext = context;
        mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.widthPixels / 3)
                .setLoadHeight(ScreenUtils.widthPixels / 3).build();
    }

    public void setData(List<WorkPhotoBean> workPhotoList){
        mWorkPhotoList = workPhotoList;
        notifyDataSetChanged();
    }

    public List<WorkPhotoBean> getData(){
        return mWorkPhotoList;
    }

    protected int getAdapterCount() {
        return mWorkPhotoList != null ? mWorkPhotoList.size() : 0;
    }

    @Override
    protected BaseViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_frame, parent, false));
    }

    @Override
    protected void onBindHolder(BaseViewHolder holder, int position) {
        SimpleDraweeView pic = holder
                .findViewById(R.id.item_pic);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)pic
                .getLayoutParams();
        params.width = (ScreenUtils.widthPixels / 3);
        params.height = (ScreenUtils.widthPixels / 3);
        pic.setLayoutParams(params);
        if(mWorkPhotoList == null || mWorkPhotoList.size() == 0){
            return;
        }
        WorkPhotoBean bean = mWorkPhotoList.get(position);
        String url = bean.getWorksPhoto();
        if(!TextUtils.isEmpty(url) && !url.toUpperCase().startsWith("HTTP")){
            url = "http://" + url;
        }
        ImageLoader.getInstance().display(pic, ImageUtils.getImgThumbUrl(url), mConfig);
    }
}
