
package com.xxyp.xxyp.user.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import com.xxyp.xxyp.main.bean.WorkBean;

import java.util.List;

/**
 * Description : 用户详情adapter Created by sunpengfei on 2017/8/2. Person in charge
 * : sunpengfei
 */
public class FrameAdapter extends RecyclerWithHFAdapter {

    private ImageRequestConfig mConfig;

    private Context mContext;

    private List<WorkBean> mWorkBeans;
    
    private OnWorkListener mListener;

    public FrameAdapter(Context context) {
        mContext = context;
        mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.widthPixels / 3)
                .setLoadHeight(ScreenUtils.widthPixels / 3).build();
    }
    
    public void setWorkListener(OnWorkListener listener){
        mListener = listener;
    }

    public void setData(List<WorkBean> workBeans) {
        mWorkBeans = workBeans;
        notifyDataSetChanged();
    }

    @Override
    protected int getAdapterCount() {
        return mWorkBeans != null ? mWorkBeans.size() : 0;
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
        if (mWorkBeans == null || mWorkBeans.size() == 0) {
            return;
        }
        final WorkBean workBean = mWorkBeans.get(position);
        String path = null;
        if (workBean.getList() != null && workBean.getList().size() > 0) {
            path = workBean.getList().get(0).getWorksPhoto();
            if ((!TextUtils.isEmpty(path)) && (!path.startsWith("http"))) {
                path = "http://" + path;
            }
        }
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.openProduct(workBean.getUserId(), workBean.getWorksId());
                }
            }
        });
        ImageLoader.getInstance().display(pic, ImageUtils.getImgThumbUrl(path), mConfig);
    }
    
    public interface OnWorkListener{

        /**
         * 进入作品详情
         * @param userId   用户id
         * @param workId   作品id
         */
        void openProduct(String userId, String workId);
    }

}
