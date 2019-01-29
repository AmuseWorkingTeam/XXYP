
package com.xxyp.xxyp.message.itemholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ImageUtils;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.utils.TimeUtils;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.ShapeImageView;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.interfaces.ChatActionListener;
import com.xxyp.xxyp.message.utils.MessageConfig;
import com.xxyp.xxyp.user.provider.UserProvider;

/**
 * Description : 聊天基础item Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public abstract class MessageItemBaseView extends FrameLayout implements IItemChatPanel {

    /* 头像 */
    protected ImageRequestConfig ICON_CONFIG;

    /**
     * 中间位置
     */
    public static final int ITEM_CENTER = -1;

    /**
     * 右边
     */
    public static final int ITEM_RIGHT = 0;

    /**
     * 左边
     */
    public static final int ITEM_LEFT = 1;

    protected ChatMessageBean mChatMessageBean;

    protected Context mContext;

    protected ChatActionListener mActionListener;

    protected int mItemPos;

    private View mItemView;

    private TextView mTxtMessageTime;

    private ShapeImageView mIconView;

    /* 发送失败图片 */
    private ImageView mSendFailImg;

    /* 发送中图片 */
    private ProgressBar mSendingImg;

    public MessageItemBaseView(Context context, ChatActionListener listener, int itemPos) {
        super(context);
        mContext = context;
        mActionListener = listener;
        mItemPos = itemPos;
        mItemView = initView(this);
        ICON_CONFIG = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(44))
                .setLoadHeight(ScreenUtils.dp2px(44)).setRender(true).build();
    }

    @Override
    public View obtainView(ViewGroup parent) {
        if (mItemPos == ITEM_LEFT) {
            mItemView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_base_left, parent,
                    false);
        } else if (mItemPos == ITEM_RIGHT) {
            mItemView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_base_right, parent,
                    false);
        } else {
            mItemView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_base_center,
                    parent, false);
        }
        FrameLayout frameLayout = (FrameLayout)mItemView.findViewById(R.id.fl_message);
        frameLayout.removeAllViews();
        frameLayout.addView(this);
        bindCommonView();
        return mItemView;
    }

    @Override
    public void bindData(ChatMessageBean chatMessageBean) {
        mChatMessageBean = chatMessageBean;
        mTxtMessageTime.setText(TimeUtils.millis2String(mChatMessageBean.getMessageTime()));
        showHead();
        showSendStatus();
        bindData();
    }

    /**
     * 公共view
     */
    private void bindCommonView() {
        mTxtMessageTime = ((TextView)mItemView.findViewById(R.id.message_time));
        mIconView = (ShapeImageView)mItemView.findViewById(R.id.message_avatar);
        switch (mItemPos) {
            case ITEM_RIGHT:
                mSendFailImg = (ImageView)mItemView.findViewById(R.id.img_sendfail_right);
                mSendFailImg.setVisibility(View.GONE);
                mSendingImg = (ProgressBar)mItemView.findViewById(R.id.img_sending_right);
                mSendingImg.setVisibility(View.GONE);
                break;
            case ITEM_LEFT:
                break;
            case ITEM_CENTER:
                break;
            default:
                break;
        }
    }

    protected abstract View initView(ViewGroup parent);

    protected abstract void bindData();

    protected abstract void setItemViewListener();

    @Override
    public void showChatTime(boolean isShowTime) {
        if (isShowTime) {
            mTxtMessageTime.setVisibility(VISIBLE);
        } else {
            mTxtMessageTime.setVisibility(GONE);
        }
    }

    /**
     * 展示头像
     */
    private void showHead() {
        if (mItemPos == ITEM_CENTER || mChatMessageBean == null) {
            return;
        }
        UserInfo userInfo = UserProvider.getUserInfoByDB(mChatMessageBean.getSendId());
        if (mIconView != null) {
            ImageLoader.getInstance().display(mIconView,
                    userInfo != null && !TextUtils.isEmpty(userInfo.getUserImage())
                            ? ImageUtils.getAvatarUrl(userInfo.getUserImage())
                            : "",
                    ICON_CONFIG);
            mIconView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionListener != null) {
                        mActionListener.onGoToUserDetail(mChatMessageBean.getSendId());
                    }
                }
            });
        }
    }

    /**
     * 展示发送消息的发送状态
     */
    private void showSendStatus() {
        if (mSendFailImg == null || mSendingImg == null) {
            return;
        }
        // 消息发送状态是否失败
        if (mChatMessageBean.getSendStatus() == MessageConfig.MessageSendStatus.SEND_MSG_ING) {
            mSendFailImg.setVisibility(View.GONE);
            mSendingImg.setVisibility(View.VISIBLE);
        } else if (mChatMessageBean
                .getSendStatus() == MessageConfig.MessageSendStatus.SEND_NSG_FAIL) {
            mSendFailImg.setVisibility(View.VISIBLE);
            mSendingImg.setVisibility(View.GONE);
        } else {
            mSendFailImg.setVisibility(View.GONE);
            mSendingImg.setVisibility(View.GONE);
        }
    }

    private class ItemLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            if (mActionListener != null) {
                mActionListener.onMessageLongClick(mChatMessageBean);
            }
            return true;
        }
    }

    /**
     * 设置聊天item的长按点击
     *
     * @param view 控件view
     */
    protected void setItemViewLongClick(View view) {
        view.setOnLongClickListener(new ItemLongClickListener());
        // 发送失败图标监听
        setItemViewListener();
    }
}
