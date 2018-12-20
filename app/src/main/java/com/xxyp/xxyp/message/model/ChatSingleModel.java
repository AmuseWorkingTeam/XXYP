
package com.xxyp.xxyp.message.model;

import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.contract.ChatSingleContract;
import com.xxyp.xxyp.message.dao.ChatMessageDBManager;
import com.xxyp.xxyp.message.utils.MessageConfig;

import java.util.List;

/**
 * Description : 单聊model
 * Created by sunpengfei on 2017/8/22.
 * Person in charge : sunpengfei
 */
public class ChatSingleModel extends ChatBaseModel implements ChatSingleContract.Model {
    @Override
    public List<ChatMessageBean> getChatMessages(int chatType, String chatId, long messageTime,
            int pageSize) {
        if(pageSize <= 0){
            pageSize = MessageConfig.DEFAULT_PAGE_SIZE;
        }
        return ChatMessageDBManager.getInstance().getChatMessageList(
                MessageConfig.MessageCatalog.CHAT_SINGLE, chatId, messageTime, pageSize);
    }

    @Override
    public long addChatMessage(ChatMessageBean bean) {
        bean.setRelationSourceId(addRelationResource(bean));
        return ChatMessageDBManager.getInstance().addChatMessage(bean);
    }
}
