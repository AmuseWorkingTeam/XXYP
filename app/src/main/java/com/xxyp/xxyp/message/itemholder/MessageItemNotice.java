
package com.xxyp.xxyp.message.itemholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.message.bean.MessageNoticeBean;
import com.xxyp.xxyp.message.interfaces.ChatActionListener;

/**
 * Description : 消息通知类型 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageItemNotice extends MessageItemBaseView {

    /* 约拍目的 */
    private TextView mNotice;

    public MessageItemNotice(@NonNull Context context, ChatActionListener listener, int itemPos) {
        super(context, listener, itemPos);
    }

    @Override
    protected View initView(ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_chat_notice, parent);
        mNotice = (TextView) view.findViewById(R.id.txt_invite);
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
        showShot();
    }

    /**
     * 展示约拍数据
     */
    private void showShot() {
        if (mChatMessageBean == null || mChatMessageBean.getNoticeBean() == null) {
            return;
        }
        MessageNoticeBean noticeBean = mChatMessageBean.getNoticeBean();
        mNotice.setText(noticeBean.getText());
    }
}
