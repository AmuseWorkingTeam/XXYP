
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
import com.xxyp.xxyp.main.bean.ShotBean;

/**
 * Description : 发现约拍adapter Created by sunpengfei on 2017/7/31. Person in
 * charge : sunpengfei
 */
public class AppointAdapter extends BaseRecyclerAdapter<ShotBean> {

    /* 头像展示配置 */
    private ImageRequestConfig mAvatarConfig;

    /* 约拍展示配置 */
    private ImageRequestConfig mShotConfig;

    private OnFindShotListener mShotListener;

    public AppointAdapter(Context context) {
        super(context);
        mAvatarConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(44))
                .setLoadHeight(ScreenUtils.dp2px(44)).setRender(true).build();

        mShotConfig = new ImageRequestConfig.Builder().setLoadWidth(400).setLoadHeight(400)
                .setRender(true).build();
    }

    public void setOnFindShotListener(OnFindShotListener shotListener) {
        mShotListener = shotListener;
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

        LinearLayout shotInfoContainer = holder.findViewById(R.id.ll_product_info);
        ShapeImageView showPic = holder.findViewById(R.id.iv_work_pic);
        TextView title = holder.findViewById(R.id.tv_product_title);
        TextView desc = holder.findViewById(R.id.tv_product_desc);

        final ShotBean bean = getItem(position);
        if (bean != null) {
            ImageLoader.getInstance().display(avatar, ImageUtils.getAvatarUrl(bean.getUserImage()),
                    mAvatarConfig);

            name.setText(!TextUtils.isEmpty(bean.getUserName()) ? bean.getUserName() : "");
            address.setText(
                    !TextUtils.isEmpty(bean.getDatingShotAddress()) ? bean.getDatingShotAddress()
                            : "");
            String shotTime = "";
            if (bean.getReleaseTime() > 0) {
                shotTime = TimeUtils.millis2String(bean.getReleaseTime());
            }
            time.setText(!TextUtils.isEmpty(shotTime) ? shotTime : "");

            String picUrl = bean.getDatingShotImages() != null
                    && bean.getDatingShotImages().size() > 0
                            ? bean.getDatingShotImages().get(0).getDatingShotPhoto()
                            : "";
            if (!TextUtils.isEmpty(picUrl) && !picUrl.toUpperCase().startsWith("HTTP")) {
                picUrl = "http://" + picUrl;
            }
            ImageLoader.getInstance().display(showPic, picUrl, mShotConfig);
            title.setText(!TextUtils.isEmpty(bean.getDatingShotIntroduction())
                    ? bean.getDatingShotIntroduction()
                    : "");
            desc.setText(!TextUtils.isEmpty(bean.getDescription()) ? bean.getDescription() : "");

            userContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mShotListener != null){
                        mShotListener.onOpenFrame(bean.getUserId());
                    }
                }
            });

            shotInfoContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mShotListener != null){
                        mShotListener.onShotListener(bean.getUserId(), bean.getDatingShotId());
                    }
                }
            });

            showPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mShotListener != null){
                        mShotListener.onShotListener(bean.getUserId(), bean.getDatingShotId());
                    }
                }
            });
        }
    }

    public interface OnFindShotListener {

        /**
         * 跳转约拍
         */
        void onShotListener(String userId, String shotId);

        /**
         * 跳转frame
         */
        void onOpenFrame(String userId);
    }
}
