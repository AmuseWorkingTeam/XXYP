
package com.xxyp.xxyp.main.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ImageUtils;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.main.bean.WorkBean;

import java.util.List;

/**
 * Description : 首页热门轮播adapter Created by sunpengfei on 2017/7/31. Person in
 * charge : sunpengfei
 */
public class HotWorkBannerAdapter extends PagerAdapter {

    private ImageRequestConfig mConfig;

    private Context mContext;

    private List<WorkBean> mList;
    
    private OnWorkListener mListener;

    public HotWorkBannerAdapter(Context context) {
        mContext = context;
        mConfig = new ImageRequestConfig.Builder().setLoadWidth(400)
                .setLoadHeight(400).setRender(true).build();
    }

    public void setWorkListener(OnWorkListener listener){
        mListener = listener;
    }
    
    public void setList(List<WorkBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    public WorkBean getItem(int position) {
        if (mList == null || position >= mList.size()) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FrameLayout localFrameLayout = (FrameLayout)LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_main_pager_adapter, null);
        SimpleDraweeView image = (SimpleDraweeView)localFrameLayout.findViewById(R.id.image);
        image.setLayoutParams(
                new FrameLayout.LayoutParams(ScreenUtils.widthPixels, ScreenUtils.widthPixels));
        if (mList != null && mList.size() > 0) {
            final WorkBean workBean = mList.get(position);
            String url = workBean.getList() != null && workBean.getList().size() > 0
                    ? workBean.getList().get(0).getWorksPhoto() : "";
            if(!TextUtils.isEmpty(url) && !url.toUpperCase().startsWith("HTTP")){
                url = "http://" + url;
            }
            ImageLoader.getInstance().display(image, ImageUtils.getImgThumbUrl(url, 0, 0), mConfig);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onWorkListener(workBean);
                    }
                }
            });
        }
        container.addView(localFrameLayout);
        return localFrameLayout;
    }
    
    public interface OnWorkListener{
        
        void onWorkListener(WorkBean workBean);
    }
}
