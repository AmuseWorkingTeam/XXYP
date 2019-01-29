package com.xxyp.xxyp.user.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ImageUtils;
import com.xxyp.xxyp.common.utils.TimeUtils;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.recyclerView.BaseRecyclerAdapter;
import com.xxyp.xxyp.common.view.recyclerView.BaseViewHolder;
import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.publish.utils.PublishConfig;

/**
 * Description : 我的约拍adapter
 * Created by sunpengfei on 2017/8/10.
 * Person in charge : sunpengfei
 */
public class MyDatingShotAdapter extends BaseRecyclerAdapter<ShotBean> {

    public MyDatingShotAdapter(Context context) {
        super(context);
    }

    @Override
    protected int onCreateViewById(int viewType) {
        return R.layout.dating_shot_item;
    }

    @Override
    protected void onBindHolder(BaseViewHolder holder, int position) {
        TextView datingCount = holder.findViewById(R.id.tv_dating_count);
        TextView shotPurpose = holder.findViewById(R.id.tv_shot_purpose);
        TextView shotTime = holder.findViewById(R.id.tv_time);
        TextView shotAddress = holder.findViewById(R.id.tv_address);
        TextView datingStatus = holder.findViewById(R.id.dating_status);
        SimpleDraweeView shotPic = holder.findViewById(R.id.dating_avatar);
        ShotBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        StringBuilder title = new StringBuilder();
        switch (bean.getPurpose()) {
            case PublishConfig.ShotPurpose.FIND_CAMERA:
                title.append("约摄影师·");
                break;
            case PublishConfig.ShotPurpose.FIND_MODEL:
                title.append("约模特·");
                break;
            default:
                break;
        }
        switch (bean.getPaymentMethod()) {
            case PublishConfig.ShotPayMethod.GATHER:
                title.append("收款");
                break;
            case PublishConfig.ShotPayMethod.FREE:
                title.append("双方互免");
                break;
            case PublishConfig.ShotPayMethod.PAY:
                title.append("付款");
                break;
            default:
                break;
        }

        datingStatus.setText(bean.getStatus() == 0 ? "进行中" : "已完成");
//        datingCount.setText("人预约");
        datingCount.setVisibility(View.GONE);
        shotPurpose.setText(title);
        shotTime.setText(TimeUtils.millis2String(bean.getReleaseTime()));
        shotAddress.setText(bean.getDatingShotAddress());
        if (bean.getDatingShotImages() != null && bean.getDatingShotImages().size() > 0) {
            String shotPicUrl = bean.getDatingShotImages().get(0).getDatingShotPhoto();
            ImageLoader.getInstance().display(shotPic, ImageUtils.getImgThumbUrl(shotPicUrl));
        }
    }
}
