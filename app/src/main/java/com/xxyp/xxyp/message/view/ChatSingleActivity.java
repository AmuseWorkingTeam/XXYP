
package com.xxyp.xxyp.message.view;

import android.content.Context;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.contract.ChatSingleContract;
import com.xxyp.xxyp.message.presenter.ChatSinglePresenter;
import com.xxyp.xxyp.message.utils.MessageConfig;

/**
 * Description : 单聊activity
 * Created by sunpengfei on 2017/8/22.
 * Person in charge : sunpengfei
 */
public class ChatSingleActivity extends ChatBaseActivity implements ChatSingleContract.View {

    private ChatSingleContract.Presenter mPresenter;

    @Override
    protected void initChatInfo(String myUserId, String chatId) {
        mPresenter = new ChatSinglePresenter(this);
        mPresenter.setChatInfo(MessageConfig.MessageCatalog.CHAT_SINGLE, myUserId, chatId);
    }

    @Override
    protected void initChatData() {
        mPresenter.getChatMessages(MessageConfig.MessageCatalog.CHAT_SINGLE, mChatId);
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        mChatViewGroup.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //获取下拉历史消息
                ChatMessageBean firstBean = mChatViewHelper.getFirstMessage();
                mPresenter.getPullChatMessages(firstBean.getMessageTime(),
                        MessageConfig.MessageCatalog.CHAT_SINGLE, mChatId);
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }
}
