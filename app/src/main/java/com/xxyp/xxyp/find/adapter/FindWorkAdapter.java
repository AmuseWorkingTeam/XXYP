
package com.xxyp.xxyp.find.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ImageUtils;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.utils.TimeUtils;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.ShapeImageView;
import com.xxyp.xxyp.common.view.recyclerView.BaseRecyclerAdapter;
import com.xxyp.xxyp.common.view.recyclerView.BaseViewHolder;
import com.xxyp.xxyp.main.bean.WorkBean;

/**
 * Description : 发现作品adapter Created by sunpengfei on 2017/7/31. Person in
 * charge : sunpengfei
 */
public class FindWorkAdapter extends BaseRecyclerAdapter<WorkBean> {
    
    /* 头像展示配置 */
    private ImageRequestConfig mAvatarConfig;
    
    /* 作品展示配置 */
    private ImageRequestConfig mWorkConfig;

    private OnFindWorkListener mListener;

    public FindWorkAdapter(Context context) {
        super(context);
        mAvatarConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(44))
                .setLoadHeight(ScreenUtils.dp2px(44)).setRender(true).build();

        mWorkConfig = new ImageRequestConfig.Builder().setLoadWidth(400)
                .setLoadHeight(400).setRender(true).build();
    }

    public void setOnFindWorkListener(OnFindWorkListener listener) {
        mListener = listener;
    }

    @Override
    protected int onCreateViewById(int viewType) {
        return R.layout.item_work_view;
    }

    @Override
    protected void onBindHolder(BaseViewHolder holder, int position) {
        RelativeLayout userContainer = holder.findViewById(R.id.user_container);
        ShapeImageView avatar = holder.findViewById(R.id.user_avatar);
        TextView name = holder.findViewById(R.id.user_name);
        TextView address = holder.findViewById(R.id.user_location);
        TextView time = holder.findViewById(R.id.user_time);

        LinearLayout workInfoContainer = holder.findViewById(R.id.ll_product_info);
        ShapeImageView pic = holder.findViewById(R.id.iv_work_pic);
        TextView title = holder.findViewById(R.id.tv_product_title);
        TextView desc = holder.findViewById(R.id.tv_product_desc);
        final WorkBean bean = getItem(position);
        if(bean != null){
            ImageLoader.getInstance().display(avatar, ImageUtils.getAvatarUrl(bean.getUserImage()),
                    mAvatarConfig);
            name.setText(!TextUtils.isEmpty(bean.getUserName()) ? bean.getUserName() : "");
            address.setText(
                    !TextUtils.isEmpty(bean.getWorksAddress()) ? bean.getWorksAddress() : "");
            time.setText(bean.getReleaseTime() > 0 ? TimeUtils.millis2String(bean.getReleaseTime())
                    : "");

            String picUrl = bean.getList() != null && bean.getList().size() > 0
                    ? bean.getList().get(0).getWorksPhoto() : "";
            if(!TextUtils.isEmpty(picUrl) && !picUrl.toUpperCase().startsWith("HTTP")){
                picUrl = "http://" + picUrl;
            }
            ImageLoader.getInstance().display(pic, picUrl, mWorkConfig);
            title.setText(!TextUtils.isEmpty(bean.getWorksTitle()) ? bean.getWorksTitle() : "");
            desc.setText(!TextUtils.isEmpty(bean.getWorksIntroduction()) ? bean.getWorksIntroduction() : "");

            userContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onOpenFrame(bean.getUserId());
                    }
                }
            });

            workInfoContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onWorkListener(bean.getUserId(), bean.getWorksId());
                    }
                }
            });

            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onWorkListener(bean.getUserId(), bean.getWorksId());
                    }
                }
            });
        }

    }

    public interface OnFindWorkListener {

        /**
         * 跳转作品
         */
        void onWorkListener(String userId, String workId);

        /**
         * 跳转frame
         */
        void onOpenFrame(String userId);
    }
}
