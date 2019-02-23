
package com.xxyp.xxyp.message.itemholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ImageUtils;
import com.xxyp.xxyp.common.utils.TimeUtils;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.message.bean.MessageShotBean;
import com.xxyp.xxyp.message.interfaces.ChatActionListener;
import com.xxyp.xxyp.message.utils.MessageConfig;
import com.xxyp.xxyp.publish.utils.PublishConfig;

/**
 * Description : 消息约拍类型 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageItemShot extends MessageItemBaseView {

    private RelativeLayout mLayoutShot;

    /* 约拍目的 */
    private TextView shotPurpose;

    /* 时间 */
    private TextView shotTime;

    /* 地点 */
    private TextView shotAddress;

    /* 状态 */
    private TextView datingStatus;

    /* 图片 */
    private SimpleDraweeView shotPic;

    public MessageItemShot(@NonNull Context context, ChatActionListener listener, int itemPos) {
        super(context, listener, itemPos);
    }

    @Override
    protected View initView(ViewGroup parent) {
        View view;
        if (mItemPos == ITEM_LEFT) {
            view = View.inflate(mContext, R.layout.item_chat_shot_left, parent);
            mLayoutShot = (RelativeLayout) view.findViewById(R.id.layout_shot_left);
        } else {
            view = View.inflate(mContext, R.layout.item_chat_shot_right, parent);
            mLayoutShot = (RelativeLayout) view.findViewById(R.id.layout_shot_right);
        }
        shotPurpose = (TextView) view.findViewById(R.id.tv_shot_purpose);
        shotTime = (TextView) view.findViewById(R.id.tv_time);
        shotAddress = (TextView) view.findViewById(R.id.tv_address);
        datingStatus = (TextView) view.findViewById(R.id.dating_status);
        shotPic = (SimpleDraweeView) view.findViewById(R.id.dating_avatar);
        return view;
    }

    @Override
    protected void bindData() {
        fillView();
    }

    @Override
    protected void setItemViewListener() {
    }

    /**
     * 填充数据
     */
    private void fillView() {
        setItemViewLongClick(mLayoutShot);
        showShot();
    }

    /**
     * 展示约拍数据
     */
    private void showShot() {
        if (mChatMessageBean == null || mChatMessageBean.getShotBean() == null) {
            return;
        }
        MessageShotBean shotBean = mChatMessageBean.getShotBean();
        //目的
        StringBuilder title = new StringBuilder();
        if(!TextUtils.isEmpty(shotBean.getPurpose())){
            switch (shotBean.getPurpose()) {
                case PublishConfig.ShotPurpose.FIND_CAMERA:
                    title.append("约摄影师·");
                    break;
                case PublishConfig.ShotPurpose.FIND_MODEL:
                    title.append("约模特·");
                    break;
                default:
                    break;
            }
        }
        if (!TextUtils.isEmpty(shotBean.getPaymentMethod())) {
            switch (shotBean.getPaymentMethod()) {
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
        }
        shotPurpose.setText(title);
        //时间
        shotTime.setText(TimeUtils.millis2String(shotBean.getDatingShotTime()));
        shotAddress.setText(shotBean.getDatingShotAddress());
        String shotPicUrl = shotBean.getDatingShotImage();
        ImageLoader.getInstance().display(shotPic, ImageUtils.getImgThumbUrl(shotPicUrl));
        //约拍状态
        String status = "";
        switch (shotBean.getStatus()) {
            case MessageConfig.ShotStatus.SHOT_DEL:
                status = "已取消";
                break;
            case MessageConfig.ShotStatus.SHOT_CREATE:
                status = "邀约";
                break;
            case MessageConfig.ShotStatus.SHOT_DATED:
                status = "约拍中";
                break;
            case MessageConfig.ShotStatus.SHOT_DONE:
                status = "拍摄完成";
                break;
            default:
                break;
        }
        datingStatus.setText(status);
    }
}
