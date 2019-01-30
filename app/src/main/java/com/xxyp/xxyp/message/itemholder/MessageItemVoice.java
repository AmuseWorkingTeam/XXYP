
package com.xxyp.xxyp.message.itemholder;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.message.bean.MessageVoiceBean;
import com.xxyp.xxyp.message.interfaces.ChatActionListener;
import com.xxyp.xxyp.message.utils.MessageConfig;

/**
 * Description : 消息语音类型 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageItemVoice extends MessageItemBaseView {

    /* 语音布局 */
    private FrameLayout mFlChatVoice;

    private ImageView mPlayStatus;

    /* 语音时间 */
    private TextView mVoiceTime;

    public MessageItemVoice(@NonNull Context context, ChatActionListener listener, int itemPos) {
        super(context, listener, itemPos);
    }

    @Override
    protected View initView(ViewGroup parent) {
        View view;
        if (mItemPos == ITEM_LEFT) {
            view = View.inflate(mContext, R.layout.item_chat_voice_left, parent);
        } else {
            view = View.inflate(mContext, R.layout.item_chat_voice_right, parent);
        }
        mFlChatVoice = (FrameLayout)view.findViewById(R.id.fl_voice_message);
        mVoiceTime = ((TextView)view.findViewById(R.id.tv_voice_time));
        mPlayStatus = ((ImageView)view.findViewById(R.id.iv_voice_play));
        return view;
    }

    @Override
    protected void bindData() {
        fillView();
    }

    @Override
    protected void setItemViewListener() {
        mFlChatVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionListener != null) {
                    mActionListener.onPlayVoiceListener(mChatMessageBean);
                }
            }
        });
    }

    /**
     * 填充数据
     */
    private void fillView() {
        setItemViewLongClick(mFlChatVoice);
        showVoice();
        showVoiceAnimation();
    }

    /**
     * 展示语音播放动画
     */
    private void showVoiceAnimation() {
        if (mChatMessageBean == null || (mChatMessageBean.getVoiceBean() == null
                && mChatMessageBean.getBodyBean() == null)) {
            return;
        }
        MessageVoiceBean voiceInfo = mChatMessageBean.getVoiceBean();
        Drawable voice = mPlayStatus.getDrawable();
        if (voiceInfo.getStatus() == MessageConfig.VoiceStatus.VOICE_PLAY) {
            // 正在播放
            if (voice != null && !(voice instanceof AnimationDrawable)) {
                mPlayStatus.setImageDrawable(mContext.getResources()
                        .getDrawable(mItemPos == ITEM_LEFT ? R.drawable.voice_left_playanimation
                                : R.drawable.voice_right_playanimation));
                AnimationDrawable voiceAnimation = (AnimationDrawable)mPlayStatus.getDrawable();
                voiceAnimation.start();
            }
        } else {
            mPlayStatus.setImageResource(mItemPos == ITEM_LEFT ? R.drawable.voice_left_play_icon3
                    : R.drawable.voice_right_play_icon3);
        }
    }

    /**
     * 展示语音数据
     */
    private void showVoice() {
        if (mChatMessageBean == null || (mChatMessageBean.getVoiceBean() == null
                && mChatMessageBean.getBodyBean() == null)) {
            return;
        }
        MessageVoiceBean voiceInfo = mChatMessageBean.getVoiceBean();
        int voiceTime = 0;
        voiceTime = voiceInfo.getVoiceLen();
        mVoiceTime.setText(voiceTime + "''");
        int width = ScreenUtils.dp2px((196 - 78) / 60 * voiceTime + 78);
        if (width > ScreenUtils.dp2px(196)) {
            width = ScreenUtils.dp2px(196);
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFlChatVoice.getLayoutParams();
        params.width = width;
        mFlChatVoice.setLayoutParams(params);
    }
}
