
package com.xxyp.xxyp.message.customsviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.bean.ItemEmoji;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.AppContextUtils;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.message.customsviews.panel.AutoComputerInputMethodHeightView;
import com.xxyp.xxyp.message.customsviews.panel.OnPanelItemListener;
import com.xxyp.xxyp.message.customsviews.panel.PanelContainer;
import com.xxyp.xxyp.message.customsviews.panel.PanelFactoryImp;

/**
 * Description : 聊天输入面板 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageInputBar extends AutoComputerInputMethodHeightView
        implements View.OnClickListener, OnPanelItemListener {

    private static final String TAG = MessageInputBar.class.getSimpleName();
    private boolean isShowKeyBoard = false;

    private ChatEditText mChatEditText;

    private ImageView mEmojiView;

    private OnInputPanelListener mListener;

    private ImageView mMoreView;

    private PanelContainer mPanel;

    private TextView mSendView;

    /* 切换语音 文本按钮*/
    private ImageView mKeyBoardView;

    /* 语音按钮 */
    private TextView mVoiceView;

    /* 是否是输入框 */
    private boolean mIsChatEdit = true;

    public static final int VOICE_START = 1;

    public static final int VOICE_FINISH = 2;

    public static final int VOICE_CANCEL = 3;

    public static final int VOICE_TIME_OUT = 4;

    public static final int VOICE_NORMAL= 5;

    public static final int VOICE_OVER = 6;

    /* 语音计时 */
    private int mCurrentTime;

    /* 是否计时中 */
    private boolean mTimeing = false;

    public MessageInputBar(Context context) {
        this(context, null);
    }

    public MessageInputBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageInputBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.c2));
        initView();
    }

    private void initView() {
        findViewById(inflate(getContext(), R.layout.chat_control_bar, this));
        mPanel = new PanelContainer(getContext());
        addView(mPanel);
    }

    private void findViewById(View view) {
        mKeyBoardView = ((ImageView)view.findViewById(R.id.control_voice));
        mEmojiView = ((ImageView)view.findViewById(R.id.control_emoji));
        mMoreView = ((ImageView)view.findViewById(R.id.control_more));
        mSendView = ((TextView)view.findViewById(R.id.view_send));
        mChatEditText = ((ChatEditText)view.findViewById(R.id.chat_edit_text));
        mVoiceView = (TextView) view.findViewById(R.id.chat_voice_view);
        setViewListener();
    }

    private void requestEditTextFocus() {
        mChatEditText.setFocusableInTouchMode(true);
        mChatEditText.setFocusable(true);
        if (!mChatEditText.hasFocus()) {
            mChatEditText.requestFocus();
        }
    }

    private void setViewListener() {
        mKeyBoardView.setOnClickListener(this);
        mEmojiView.setOnClickListener(this);
        mMoreView.setOnClickListener(this);
        mSendView.setOnClickListener(this);
        mChatEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                requestEditTextFocus();
                showKeyboard(mPanel);
                if (event.getAction() == 0
                        && mListener != null) {
                    mListener.onTagChanged(OnInputPanelListener.TAG_EMPTY);
                }
                return false;
            }
        });
        mChatEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hidePanel();
                }
            }
        });
        mChatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    mSendView.setVisibility(GONE);
                }else{
                    mSendView.setVisibility(VISIBLE);
                }
            }
        });

        //语音按钮
        mVoiceView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                float height = mPanel.getY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        actionDown();
                        if (null != mListener) {
                            mListener.onSendVoiceRequest(VOICE_START, mCurrentTime);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(event.getY() < height){
                            //超出范围抬起 返回录音取消
                            if(mListener != null){
                                mListener.onSendVoiceRequest(VOICE_OVER, mCurrentTime);
                            }
                        }else{
                            if (null != mListener) {
                                mListener.onSendVoiceRequest(VOICE_NORMAL, mCurrentTime);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(event.getY() < height){
                            //超出范围抬起 返回录音取消
                            if(mListener != null){
                                mListener.onSendVoiceRequest(VOICE_CANCEL, mCurrentTime);
                            }
                        }else{
                            if (null != mListener) {
                                mListener.onSendVoiceRequest(VOICE_FINISH, mCurrentTime);
                            }
                        }
                        actionUp();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (null != mListener) {
                            mListener.onSendVoiceRequest(VOICE_CANCEL, mCurrentTime);
                        }
                        actionUp();
                        break;
                }
                return true;
            }
        });
    }

    public void hideKeyboard() {
        dismissKeyBoard();
        isShowKeyBoard = false;
    }

    public void hidePanel() {
        mChatEditText.clearFocus();
        mPanel.hidePanel();
        hideKeyboard();
    }

    public boolean isShowKeyBoard() {
        return isShowKeyBoard;
    }

    public boolean isShowPanel() {
        return mPanel.isShown();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_send:
                //发送文本
                if(mListener != null){
                    mListener.onSendTextRequest(mChatEditText.getText().toString());
                    mChatEditText.setText("");
                }
                break;
            case R.id.control_voice:
                if(mListener != null){
                    mListener.onTagChanged(OnInputPanelListener.TAG_EMPTY);
                }
                changeEdit(!mIsChatEdit);
                break;
            case R.id.control_emoji:
                changeEdit(true);
                if(mListener != null){
                    mListener.onTagChanged(OnInputPanelListener.TAG_EMPTY);
                }
                hideKeyboard();
                mPanel.showPanel(PanelFactoryImp.TYPE_EMOJI, this);
                break;
            case R.id.control_more:
                changeEdit(true);
                if(mListener != null){
                    mListener.onTagChanged(OnInputPanelListener.TAG_EMPTY);
                }
                hideKeyboard();
                mPanel.showPanel(PanelFactoryImp.TYPE_FUNCTION, this);
                break;
        }
    }

    public void setInputListener(OnInputPanelListener listener) {
        mListener = listener;
    }

    public void showKeyboard(View view) {
        showKeyBoard(view);
        isShowKeyBoard = true;
    }

    /**
     * 切换输入框和语音
     */
    private void changeEdit(boolean isChatEdit){
        mIsChatEdit = isChatEdit;
        if(isChatEdit){
            mKeyBoardView.setBackgroundResource(R.drawable.chat_keyboard_icon_selector);
            mVoiceView.setVisibility(GONE);
            mChatEditText.setVisibility(VISIBLE);
        }else{
            mKeyBoardView.setBackgroundResource(R.drawable.chat_voice_icon_selector);
            mVoiceView.setVisibility(VISIBLE);
            mChatEditText.setVisibility(GONE);
            mVoiceView.setPressed(false);
            mVoiceView.setText("按住说话");
            hidePanel();
        }
    }

    @Override
    public void onPanelItemClick(int type, Object item) {
        if(item == null){
            return;
        }
        switch (type){
            case PanelFactoryImp.TYPE_FUNCTION:
                if(mListener != null){
                    mListener.onFunctionRequest((Integer) item);
                }
                break;
            case PanelFactoryImp.TYPE_EMOJI:
                ItemEmoji emoji = (ItemEmoji) item;
                insertInEditText(emoji);
                break;
            default:
                break;
        }
    }

    /**
     * 语音按下
     */
    private void actionDown() {
        mVoiceView.setPressed(true);
        mVoiceView.setText("上滑取消");
        startRecord();
    }

    /**
     * 语音抬起
     */
    private void actionUp() {
        if (mCurrentTime < 1) {
            mVoiceView.postDelayed(rResetHint, 500);
        } else {
            mVoiceView.setPressed(false);
            mVoiceView.setText("按住说话");
        }
        stopRecord();
    }

    private void startRecord() {
        mTimeing = true;
        mCurrentTime = 0;
        mVoiceView.postDelayed(rTime, 1000);
    }

    private void stopRecord() {
        mTimeing = false;
        mCurrentTime = 0;
        mVoiceView.removeCallbacks(rTime);
    }

    private Runnable rResetHint = new Runnable() {
        @Override
        public void run() {
            mVoiceView.setPressed(false);
            mVoiceView.setText("按住说话");
        }
    };

    private Runnable rTime = new Runnable() {
        @Override
        public void run() {
            mCurrentTime++;
            XXLog.log_d(TAG, "time:" + mCurrentTime);
            if (mTimeing) {
                mVoiceView.postDelayed(rTime, 1000);
            }
        }
    };

    /**
     * emoji插入输入框
     * @param item  表情
     */
    private void insertInEditText(ItemEmoji item) {
        Editable editable = mChatEditText.getText();
        int cursorIndex = mChatEditText.getSelectionStart();
        int resId = item.getEmojiResId();
        Drawable drawable = AppContextUtils.getAppContext().getResources().getDrawable(resId);
        drawable.setBounds(0, 0, ScreenUtils.dp2px(25), ScreenUtils.dp2px(25));
        ImageSpan imageSpan = new ImageSpan(drawable);
        SpannableString spannableString = new SpannableString(item.getEmojiName());
        spannableString.setSpan(imageSpan, 0, item.getEmojiName().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editable.insert(cursorIndex, spannableString);
    }

    public interface OnInputPanelListener {

        int TAG_EMPTY = -1;

        int TAG_EDIT = 1;

        int TAG_EMOJI = 2;

        int TAG_FUNC = 3;

        int TAG_VOICE = 4;

        /* 功能监听 */
        void onFunctionRequest(int functionType);

        /* 发送文本 */
        void onSendTextRequest(String sendText);

        /* 发送语音 */
        void onSendVoiceRequest(int voiceAction, long time);

        /* 标记位 */
        void onTagChanged(int tag);
    }
}
