
package com.xxyp.xxyp.message.itemholder;

import com.xxyp.xxyp.message.bean.ChatMessageBean;

/**
 * Description : 聊天item Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public interface IItemChatPanel extends IItemPanel<ChatMessageBean> {

    void showChatTime(boolean isShowTime);
}
