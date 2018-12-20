
package com.xxyp.xxyp.message.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
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
import com.xxyp.xxyp.message.bean.ConversationBean;

/**
 * Description : 消息列表 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageListAdapter extends BaseRecyclerAdapter<ConversationBean> {

    private ImageRequestConfig mConfig;

    public MessageListAdapter(Context context) {
        super(context);
        mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(44))
                .setLoadHeight(ScreenUtils.dp2px(44)).setRender(true).build();
    }

    @Override
    protected int onCreateViewById(int viewType) {
        return R.layout.item_message_view;
    }

    @Override
    protected void onBindHolder(BaseViewHolder holder, int position) {
        ShapeImageView avatar = holder.findViewById(R.id.message_avatar);
        TextView title = holder.findViewById(R.id.message_title);
        TextView desc = holder.findViewById(R.id.message_desc);
        TextView time = holder.findViewById(R.id.message_time);
        TextView unReadCount = holder.findViewById(R.id.message_unread_num);
        ConversationBean bean = getItem(position);
        ImageLoader.getInstance().display(avatar,
                bean != null ? ImageUtils.getAvatarUrl(bean.getConversationAvatar()) : "", mConfig);
        title.setText(bean != null && !TextUtils.isEmpty(bean.getConversationName())
                ? bean.getConversationName()
                : "聊天");
        desc.setText(
                bean != null && !TextUtils.isEmpty(bean.getMsgDigest()) ? bean.getMsgDigest() : "");
        time.setText(bean != null && bean.getCreateTime() > 0
                ? TimeUtils.millis2String(bean.getCreateTime())
                : "");
        if (bean != null && bean.getUnReadCount() > 0) {
            unReadCount.setVisibility(View.VISIBLE);
            unReadCount.setText(bean.getUnReadCount() > 99 ? "99+" : bean.getUnReadCount() + "");
        } else {
            unReadCount.setVisibility(View.GONE);
        }
    }
}
