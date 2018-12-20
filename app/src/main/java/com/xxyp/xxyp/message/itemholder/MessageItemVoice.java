
package com.xxyp.xxyp.message.itemholder;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.message.bean.MessageVoiceBean;
import com.xxyp.xxyp.message.interfaces.ChatActionListener;

/**
 * Description : 消息语音类型 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageItemVoice extends MessageItemBaseView {

    /* 语音布局 */
    private FrameLayout mFlChatVoice;

    /* 语音时间 */
    private TextView mVoiceTime;

    public MessageItemVoice(@NonNull Context context, ChatActionListener listener, int itemPos) {
        super(context, listener, itemPos);
    }


    @Override
    protected View initView(ViewGroup parent) {
        View view;
        if(mItemPos == ITEM_LEFT){
            view = View.inflate(mContext, R.layout.item_chat_voice_left, parent);
        }else{
            view = View.inflate(mContext, R.layout.item_chat_voice_right, parent);
        }
        mFlChatVoice = (FrameLayout) view.findViewById(R.id.fl_voice_message);
        mVoiceTime = ((TextView)view.findViewById(R.id.tv_voice_time));
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
                if(mActionListener != null){
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
    }

    /**
     * 展示语音数据
     */
    private void showVoice(){
        if (mChatMessageBean == null || (mChatMessageBean.getVoiceBean() == null && mChatMessageBean.getBodyBean() == null)) {
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mFlChatVoice.setLayoutParams(params);
    }
}
