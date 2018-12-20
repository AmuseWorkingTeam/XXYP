
package com.xxyp.xxyp.message.itemholder;

import android.view.View;

import com.xxyp.xxyp.message.bean.ChatMessageBean;

/**
 * Description : 聊天页面holder Created by sunpengfei on 2017/8/1. Person in charge
 * : sunpengfei
 */
public class BaseChatHolder extends BaseHolder<ChatMessageBean> {

    public BaseChatHolder(View view, IItemChatPanel chatPanel) {
        super(view, chatPanel);
    }

    public void showChatTime(boolean showTime) {
        ((IItemChatPanel)mPanel).showChatTime(showTime);
    }
}
