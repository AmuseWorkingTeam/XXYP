
package com.xxyp.xxyp.message.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.contract.ChatBaseContract;
import com.xxyp.xxyp.message.customsviews.ChatRecyclerView;
import com.xxyp.xxyp.message.customsviews.ChatViewGroup;
import com.xxyp.xxyp.message.customsviews.MessageInputBar;
import com.xxyp.xxyp.message.model.MessageModel;
import com.xxyp.xxyp.message.service.MessageListenerManager;
import com.xxyp.xxyp.message.service.MsgServiceManager;
import com.xxyp.xxyp.message.utils.ChatViewHelper;
import com.xxyp.xxyp.message.utils.MessageConfig;

import java.util.List;

/**
 * Description : 基础聊天activity Created by sunpengfei on 2017/8/15. Person in
 * charge : sunpengfei
 */
public abstract class ChatBaseActivity extends BaseTitleActivity
        implements ChatBaseContract.View, MessageInputBar.OnInputPanelListener,
        MessageListenerManager.OnMsgReceiveListener, MessageListenerManager.OnMsgSendListener {

    protected String mMyUserId;

    protected String mChatId;

    protected int mChatType;

    protected ChatViewGroup mChatViewGroup;

    protected ChatViewHelper mChatViewHelper;

    private MessageInputBar mInputBar;

    private ChatRecyclerView mRecyclerView;

    /* 语音录制view */
    private View mRecordView;

    /* 录制音量 */
    private ImageView mRecordMicView;

    protected ChatBaseContract.Presenter mPresenter;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setTitle(R.string.message);
        builder.setBackIcon(R.drawable.header_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.activity_chat_base, null);
        mChatViewGroup = ((ChatViewGroup)view.findViewById(R.id.chat_view_group));
        mRecyclerView = ((ChatRecyclerView)view.findViewById(R.id.swipe_target));
        mRecordView = view.findViewById(R.id.record_voice_view);
        mRecordMicView = (ImageView) view.findViewById(R.id.record_voice_mic);
        mInputBar = ((MessageInputBar)view.findViewById(R.id.chat_control_bar));
        mChatViewHelper = new ChatViewHelper(this, mRecyclerView);
        MsgServiceManager.getInstance().registerIMListener(this, this);
        return view;
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        mMyUserId = SharePreferenceUtils.getInstance().getUserId();
        if (intent == null) {
            return;
        }
        mChatType = intent.getIntExtra(MessageConfig.CHAT_TYPE,
                MessageConfig.MessageCatalog.CHAT_SINGLE);
        mChatId = intent.getStringExtra(MessageConfig.CHAT_ID);
    }

    @Override
    protected void initDataForActivity() {
        initChatInfo(mMyUserId, mChatId);
        initChatData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mPresenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mPresenter.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected abstract void initChatInfo(String myUserId, String chatId);

    protected abstract void initChatData();

    @Override
    protected void setViewListener() {
        mChatViewGroup.setInterceptTouchListener(new ChatViewGroup.InterceptTouchListener() {
            @Override
            public boolean setInterceptTouchListener(MotionEvent ev) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mInputBar.isShowKeyBoard() || mInputBar.isShowPanel()) {
                            mInputBar.hidePanel();
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mInputBar.setInputListener(this);
    }

    @Override
    public void setChatIds(int chatType, String myUserId, String chatId) {
        mChatType = chatType;
        mMyUserId = myUserId;
        mChatId = chatId;
    }

    @Override
    public void showChatMessages(List<ChatMessageBean> beans) {
        if (beans != null && beans.size() > 0) {
            mChatViewHelper.addChatMessages(beans);
        }
    }

    @Override
    public void showPullChatMessages(List<ChatMessageBean> beans) {
        mChatViewGroup.setRefreshing(false);
        if (beans != null && beans.size() > 0) {
            mChatViewHelper.addTopChatMessages(beans);
        }
    }

    @Override
    public void sendChatMessage(ChatMessageBean bean) {
        if (bean != null) {
            mChatViewHelper.addChatMessage(bean);
        }
    }

    @Override
    public void sendChatMessages(List<ChatMessageBean> beans) {
        if (beans != null && beans.size() > 0) {
            mChatViewHelper.addChatMessages(beans);
        }
    }

    @Override
    public void updateChatMessage(ChatMessageBean bean) {
        mChatViewHelper.updateChatMessage(bean);
    }

    @Override
    public void setPresenter(ChatBaseContract.Presenter presenter) {
        mPresenter = presenter;
        mChatViewHelper.setChatPresenter(presenter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //清除未读数
        new MessageModel().clearUnReadCountByChatId(mChatId);
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mInputBar.isShowKeyBoard()) {
            mInputBar.hideKeyboard();
        }
    }

    @Override
    public void onFunctionRequest(int functionType) {
        mPresenter.onFunctionRequest(functionType);
    }

    @Override
    public void onSendTextRequest(String sendText) {
        mPresenter.onSendTextRequest(sendText);
    }

    @Override
    public void onSendVoiceRequest(int voiceAction, long time) {
        mPresenter.onSendVoiceRequest(voiceAction, time);
    }

    @Override
    public void onTagChanged(int tag) {
        mChatViewHelper.scrollBottom();
    }

    @Override
    public void onReceiveMessage(ChatMessageBean bean) {
        if (bean == null) {
            return;
        }
        if (TextUtils.equals(bean.getChatId(), mChatId)) {
            mChatViewHelper.addChatMessage(bean);
        }
    }

    @Override
    public void onReceiveMessages(List<ChatMessageBean> beans) {
    }

    @Override
    public void onSendSuccess(String chatId, int chatType, String msgId, String conversationId) {
        mChatViewHelper.updateChatMessageStatus(msgId,
                MessageConfig.MessageSendStatus.SEND_MSG_SUCCESS);
    }

    @Override
    public void onSendFail(String chatId, int chatType, String msgId, String conversationId) {
        mChatViewHelper.updateChatMessageStatus(msgId,
                MessageConfig.MessageSendStatus.SEND_NSG_FAIL);
    }

    @Override
    public void onSendProgress(String chatId, int chatType, String msgId) {

    }

    @Override
    public void showRecordView() {
        mRecordView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRecordMicView(int radio) {
        if (mRecordView != null && mRecordView.isShown()) {
            if (radio > 20) {
                mRecordMicView.setBackgroundResource(R.drawable.voice_mic_3);
                return;
            }
            if (radio <= 8) {
                mRecordMicView.setBackgroundResource(R.drawable.voice_mic_1);
            } else if (radio <= 14) {
                mRecordMicView.setBackgroundResource(R.drawable.voice_mic_2);
            } else if (radio <= 20) {
                mRecordMicView.setBackgroundResource(R.drawable.voice_mic_3);
            }
        }
    }

    @Override
    public void hideRecordView() {
        mRecordView.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        MsgServiceManager.getInstance().removeIMListener(this, this);
        if(mPresenter != null){
            mPresenter.onDestroyPresenter();
        }
        setNull(mPresenter);
        setNull(mChatViewHelper);
        super.onDestroy();
    }
}
