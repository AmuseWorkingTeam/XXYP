
package com.xxyp.xxyp.message.customsviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.xxyp.xxyp.message.adapter.ChatMessageAdapter;
import com.xxyp.xxyp.message.interfaces.ChatActionListener;
import com.xxyp.xxyp.message.utils.MessageListHelper;

/**
 * Description : 聊天页面recyclerView Created by sunpengfei on 2017/8/1. Person in
 * charge : sunpengfei
 */
public class ChatRecyclerView extends RecyclerView {

    private MessageListHelper mMessageListHelper;

    private ChatMessageAdapter mAdapter;

    public ChatRecyclerView(Context context) {
        this(context, null);
    }

    public ChatRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mAdapter = new ChatMessageAdapter(context);
        setLayoutManager(new LinearLayoutManager(context));
        mMessageListHelper = new MessageListHelper(this, mAdapter);
        mAdapter.setChatMessages(mMessageListHelper.getChatMessages());
        setAdapter(mAdapter);
    }

    /**
     * 设置RecyclerView的holder监听
     * @param listener 聊天item监听
     */
    public void setItemListener(ChatActionListener listener) {
        mAdapter.setActionListener(listener);
    }

    public MessageListHelper getMessageListHelper() {
        return mMessageListHelper;
    }
}
