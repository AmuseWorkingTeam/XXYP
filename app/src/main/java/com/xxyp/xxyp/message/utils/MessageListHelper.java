
package com.xxyp.xxyp.message.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.xxyp.xxyp.message.adapter.ChatMessageAdapter;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.customsviews.ChatRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 聊天列表帮助类 Created by sunpengfei on 2017/8/16. Person in charge :
 * sunpengfei
 */
public class MessageListHelper {

    private ChatMessageAdapter mAdapter;

    private ChatRecyclerView mChatRecyclerView;

    private LinearLayoutManager mManager;

    private List<ChatMessageBean> mMessages = new ArrayList();

    public MessageListHelper(@NonNull ChatRecyclerView chatRecyclerView,
            @NonNull ChatMessageAdapter adapter) {
        mChatRecyclerView = chatRecyclerView;
        mAdapter = adapter;
        mManager = ((LinearLayoutManager)mChatRecyclerView.getLayoutManager());
    }

    public void addMessage(ChatMessageBean bean) {
        mMessages.add(bean);
        mAdapter.notifyItemInserted(mMessages.size() - 1);
    }

    public void addMessages(List<ChatMessageBean> beans) {
        int position = mMessages.size();
        mMessages.addAll(beans);
        mAdapter.notifyItemRangeInserted(position, beans.size());
    }

    public void addMessage(int index, ChatMessageBean bean) {
        mMessages.add(index, bean);
        mAdapter.notifyItemInserted(index);
    }

    public void addMessages(int index, List<ChatMessageBean> beans) {
        mMessages.addAll(index, beans);
        mAdapter.notifyItemRangeInserted(index, beans.size());
    }

    /**
     * 更新某一条消息
     *
     * @param bean 消息体
     */
    void updateMessage(ChatMessageBean bean) {
        if (bean.getMsgId() != null) {
            for (int i = 0; i < mMessages.size(); i++) {
                if (TextUtils.equals(mMessages.get(i).getMsgId(), bean.getMsgId())) {
                    mMessages.get(i).setChatMessageBean(bean);
                    mAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    /**
     * 更新消息状态
     *
     * @param msgId 消息id
     * @param status 消息状态
     */
    void updateSendStatus(String msgId, int status) {
        if (!TextUtils.isEmpty(msgId)) {
            for (int i = 0; i < mMessages.size(); i++) {
                ChatMessageBean bean = mMessages.get(i);
                if (TextUtils.equals(bean.getMsgId(), msgId)) {
                    bean.setSendStatus(status);
                    mMessages.set(i, bean);
                    mAdapter.notifyItemChanged(i, bean);
                    return;
                }
            }
        }
    }

    public int getChatCount() {
        return mAdapter.getItemCount();
    }

    public List<ChatMessageBean> getChatMessages() {
        return mMessages;
    }

    public ChatMessageBean getFirstMessage() {
        if (mMessages != null && mMessages.size() > 0) {
            return mMessages.get(0);
        }
        return null;
    }
}
