
package com.xxyp.xxyp.message.itemholder;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.EmojiUtils;
import com.xxyp.xxyp.message.interfaces.ChatActionListener;
import com.xxyp.xxyp.message.utils.MessageConfig;

/**
 * Description : 消息文本类型 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class MessageItemText extends MessageItemBaseView {

    private TextView mChatText;

    public MessageItemText(@NonNull Context context, ChatActionListener listener, int itemPos) {
        super(context, listener, itemPos);
    }


    @Override
    protected View initView(ViewGroup parent) {
        View view;
        if(mItemPos == ITEM_LEFT){
            view = View.inflate(mContext, R.layout.item_chat_text_left, parent);
        }else{
            view = View.inflate(mContext, R.layout.item_chat_text_right, parent);
        }
        mChatText = ((TextView)view.findViewById(R.id.txt_message));
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
        setItemViewLongClick(mChatText);
        showText();
    }

    /**
     * 展示文本数据
     */
    private void showText(){
        if (mChatMessageBean == null || mChatMessageBean.getBodyBean() == null) {
            mChatText.setText("");
            return;
        }
        if(mChatMessageBean.getMsgType() != MessageConfig.MessageType.MSG_TEXT){
            //非文本转为无法识别
            mChatText.setText("未知消息");
            return;
        }
        if(TextUtils.isEmpty(mChatMessageBean.getBodyBean().getText())){
            mChatText.setText("");
            return;
        }
        String text = mChatMessageBean.getBodyBean().getText();
        String zhengze = "\\[[^\\[\\]]{1,3}\\]"; // 正则表达式，用来判断消息内是否有表情
        try {
            SpannableString spannable = EmojiUtils.getInstance().getExpressionString(
                    text, zhengze);
            mChatText.setText(spannable);
        } catch (Exception e) {
            e.printStackTrace();
            mChatText.setText(text);
            XXLog.log_d("MessageItemText", "表情匹配错误");
        }
    }
}
