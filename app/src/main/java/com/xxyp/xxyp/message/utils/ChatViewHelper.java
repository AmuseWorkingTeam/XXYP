
package com.xxyp.xxyp.message.utils;

import android.app.Activity;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.xxyp.xxyp.common.view.WeakHandler;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.contract.ChatBaseContract;
import com.xxyp.xxyp.message.customsviews.ChatRecyclerView;
import com.xxyp.xxyp.message.interfaces.ChatActionListener;

import java.util.List;

/**
 * Description : 发送消息帮助类 Created by sunpengfei on 2017/8/16. Person in charge :
 * sunpengfei
 */
public class ChatViewHelper implements ChatActionListener {

    private static final int SCROLL_TO_BOTTOM = 10000;

    private static final int SCROLL_POSITION = 10001;

    private static final int ADD_CHAT_MESSAGE = 10002;

    private static final int ADD_CHAT_MESSAGES = 10003;

    private static final int ADD_CHAT_MESSAGE_TOP = 10004;

    private static final int ADD_CHAT_MESSAGES_TOP = 10005;

    private static final int UPDATE_CHAT_MESSAGES = 10006;

    private static final int CHANGE_SEND_STATUS = 10007;

    private ChatRecyclerView mChatRecyclerView;

    private Activity mContext;

    private WeakHandler<Activity> mHandler;

    private LinearLayoutManager mLayoutManager;

    private MessageListHelper mMessageListHelper;

    private ChatBaseContract.Presenter mPresenter;

    public ChatViewHelper(Activity activity, ChatRecyclerView chatRecyclerView) {
        mContext = activity;
        mChatRecyclerView = chatRecyclerView;
        mChatRecyclerView.setItemListener(this);
        mMessageListHelper = mChatRecyclerView.getMessageListHelper();
        mLayoutManager = ((LinearLayoutManager)mChatRecyclerView.getLayoutManager());
        initHandler();
    }

    public void setChatPresenter(ChatBaseContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @SuppressWarnings("unchecked")
    private void initHandler() {
        mHandler = new WeakHandler(mContext) {
            @Override
            protected void handlerWeakMessage(Message msg) {
                super.handlerWeakMessage(msg);
                switch (msg.what) {
                    case SCROLL_TO_BOTTOM:
                        mChatRecyclerView.scrollToPosition(mMessageListHelper.getChatCount() - 1);
                        break;
                    case SCROLL_POSITION:
                        int position = msg.arg1;
                        mChatRecyclerView.scrollToPosition(position);
                        break;
                    case ADD_CHAT_MESSAGE:
                        ChatMessageBean addBean = (ChatMessageBean)msg.obj;
                        mMessageListHelper.addMessage(addBean);
                        mChatRecyclerView.scrollToPosition(mMessageListHelper.getChatCount() - 1);
                        break;
                    case ADD_CHAT_MESSAGES:
                        List<ChatMessageBean> addBeans = (List<ChatMessageBean>)msg.obj;
                        mMessageListHelper.addMessages(addBeans);
                        mChatRecyclerView.scrollToPosition(mMessageListHelper.getChatCount() - 1);
                        break;
                    case ADD_CHAT_MESSAGE_TOP:
                        ChatMessageBean topBean = (ChatMessageBean)msg.obj;
                        mMessageListHelper.addMessage(0, topBean);
                        mChatRecyclerView.scrollToPosition(0);
                        break;
                    case ADD_CHAT_MESSAGES_TOP:
                        List<ChatMessageBean> topBeans = (List<ChatMessageBean>)msg.obj;
                        mMessageListHelper.addMessages(0, topBeans);
                        mChatRecyclerView.scrollToPosition(topBeans.size() - 1);
                        break;
                    case UPDATE_CHAT_MESSAGES:
                        ChatMessageBean updateBean = (ChatMessageBean)msg.obj;
                        mMessageListHelper.updateMessage(updateBean);
                        break;
                    case CHANGE_SEND_STATUS:
                        String messageId = (String)msg.obj;
                        if (null != messageId) {
                            int status = msg.arg1;
                            mMessageListHelper.updateSendStatus(messageId, status);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 添加消息到列表
     * 
     * @param bean 消息
     */
    public void addChatMessage(ChatMessageBean bean) {
        Message message = Message.obtain();
        message.what = ADD_CHAT_MESSAGE;
        message.obj = bean;
        mHandler.sendMessage(message);
    }

    /**
     * 添加消息列表到列表
     * 
     * @param beans 消息
     */
    public void addChatMessages(List<ChatMessageBean> beans) {
        Message message = Message.obtain();
        message.what = ADD_CHAT_MESSAGES;
        message.obj = beans;
        mHandler.sendMessage(message);
    }

    /**
     * 添加消息到列表顶部
     * 
     * @param bean 消息
     */
    public void addTopChatMessage(ChatMessageBean bean) {
        Message message = Message.obtain();
        message.what = ADD_CHAT_MESSAGE_TOP;
        message.obj = bean;
        mHandler.sendMessage(message);
    }

    /**
     * 添加消息到列表顶部
     * 
     * @param beans 消息
     */
    public void addTopChatMessages(List<ChatMessageBean> beans) {
        Message message = Message.obtain();
        message.what = ADD_CHAT_MESSAGES_TOP;
        message.obj = beans;
        mHandler.sendMessage(message);
    }

    /**
     * 更新某条消息
     * 
     * @param bean 消息
     */
    public void updateChatMessage(ChatMessageBean bean) {
        if (bean == null) {
            return;
        }
        Message message = Message.obtain();
        message.what = UPDATE_CHAT_MESSAGES;
        message.obj = bean;
        mHandler.sendMessage(message);
    }

    /**
     * 更新消息发送状态
     *
     * @param msgId 消息
     * @param status 消息状态
     */
    public void updateChatMessageStatus(String msgId, int status) {
        Message msg = Message.obtain();
        msg.what = CHANGE_SEND_STATUS;
        msg.obj = msgId;
        msg.arg1 = status;
        mHandler.sendMessage(msg);
    }

    /**
     * 获取第一条消息
     * 
     * @return ChatMessageBean
     */
    public ChatMessageBean getFirstMessage() {
        if (mMessageListHelper != null) {
            return mMessageListHelper.getFirstMessage();
        }
        return null;
    }

    /**
     * 滚动到底部
     */
    public void scrollBottom() {
        mHandler.sendEmptyMessageDelayed(SCROLL_TO_BOTTOM, 300);
    }

    @Override
    public void onGoToUserDetail(String userId) {
        if (mPresenter != null) {
            mPresenter.onGoToUserDetail(userId);
        }
    }

    @Override
    public void onClickToBigImgListener(View view, ChatMessageBean chatBean) {
        if (mPresenter != null) {
            mPresenter.onClickToBigImgListener(view, chatBean);
        }
    }

    @Override
    public void onPlayVoiceListener(ChatMessageBean chatBean) {
        if (mPresenter != null) {
            mPresenter.onPlayVoiceListener(chatBean);
        }
    }

    @Override
    public void onGoToShotDetail(ChatMessageBean chatBean) {
        if (mPresenter != null) {
            mPresenter.onGoToShotDetail(chatBean);
        }
    }

    @Override
    public void onUpdateShot(ChatMessageBean chatBean, int targetStatus) {
        if (mPresenter != null) {
            mPresenter.onUpdateShot(chatBean, targetStatus);
        }
    }

    @Override
    public void onUpdateShotStatus(ChatMessageBean chatBean) {

    }

    @Override
    public void onReSendMessageListener(ChatMessageBean chatBean) {

    }

    @Override
    public void onChatCopy(ChatMessageBean chatBean) {

    }

    @Override
    public void onChatDel(ChatMessageBean chatBean) {

    }

    @Override
    public void onMessageLongClick(ChatMessageBean chatBean) {

    }
}
