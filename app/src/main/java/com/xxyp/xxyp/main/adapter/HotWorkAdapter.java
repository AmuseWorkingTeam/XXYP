
package com.xxyp.xxyp.main.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ImageUtils;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.utils.TimeUtils;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.ShapeImageView;
import com.xxyp.xxyp.common.view.recyclerView.BaseViewHolder;
import com.xxyp.xxyp.common.view.recyclerView.RecyclerWithHFAdapter;
import com.xxyp.xxyp.main.bean.WorkBean;

import java.util.List;

/**
 * Description : 首页热门adapter Created by sunpengfei on 2017/7/31. Person in
 * charge : sunpengfei
 */
public class HotWorkAdapter extends RecyclerWithHFAdapter {

    private ImageRequestConfig mConfig;

    private Context mContext;

    private List<WorkBean> mList;

    private OnWorkListener mListener;

    public HotWorkAdapter(Context context) {
        mContext = context;
        mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(44))
                .setLoadHeight(ScreenUtils.dp2px(44)).setRender(true).build();
    }

    public void setList(List<WorkBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setWorkListener(OnWorkListener listener) {
        mListener = listener;
    }

    @Override
    protected int getAdapterCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public WorkBean getItem(int position) {
        if (position >= 0 && mList.size() > position) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    protected BaseViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_user_view, parent, false));
    }

    @Override
    protected void onBindHolder(BaseViewHolder holder, int position) {
        RelativeLayout relativeLayout = holder
                .findViewById(R.id.user_container);
        TextView name = holder.findViewById(R.id.user_name);
        TextView address = holder.findViewById(R.id.user_location);
        TextView time = holder.findViewById(R.id.user_time);
        ShapeImageView product = holder
                .findViewById(R.id.user_product);
        product.setVisibility(View.VISIBLE);
        ShapeImageView avatar = holder
                .findViewById(R.id.user_avatar);
        if (mList == null || mList.size() == 0) {
            return;
        }
        final WorkBean workBean = mList.get(position);
        if(workBean == null){
            return;
        }
        ImageLoader.getInstance().display(avatar, ImageUtils.getAvatarUrl(workBean.getUserImage()),
                mConfig);
        name.setText(workBean.getUserName());
        address.setText(workBean.getWorksAddress());
        time.setText(TimeUtils.millis2String(workBean.getReleaseTime()));

        String productUrl = workBean.getList().get(0).getWorksPhoto();
        if (!TextUtils.isEmpty(productUrl) && !productUrl.toUpperCase().startsWith("HTTP")) {
            productUrl = "http://" + productUrl;
        }
        ImageLoader.getInstance().display(product, ImageUtils.getImgThumbUrl(productUrl));
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onOpenFrame(workBean.getUserId());
                }
            }
        });
        product.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onWorkListener(workBean.getUserId(), workBean.getWorksId());
                }
            }
        });
    }

    public interface OnWorkListener {

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
