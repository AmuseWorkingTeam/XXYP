
package com.xxyp.xxyp.message.model;

import android.text.TextUtils;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.ConversationBean;
import com.xxyp.xxyp.message.contract.MessageContract;
import com.xxyp.xxyp.message.dao.ConversationDBManager;
import com.xxyp.xxyp.message.utils.MessageDescUtils;
import com.xxyp.xxyp.user.provider.UserProvider;

import java.util.List;

/**
 * Description : 会话model Created by sunpengfei on 2017/8/24. Person in charge :
 * sunpengfei
 */
public class MessageModel implements MessageContract.Model {

    @Override
    public long addOrUpdateConversation(ChatMessageBean bean) {
        if (bean == null) {
            return -1;
        }
        ConversationBean conversationBean = new ConversationBean();
        conversationBean.setChatId(bean.getChatId());
        conversationBean.setChatType(bean.getChatType());
        conversationBean.setCreateTime(bean.getMessageTime());
        conversationBean.setMsgDigest(MessageDescUtils.getMessageDes(bean));
        // 取出头像 名称
        UserInfo userInfo = UserProvider.getUserInfoByDB(bean.getChatId());
        if (userInfo != null) {
            conversationBean.setConversationAvatar(userInfo.getUserImage());
            conversationBean.setConversationName(userInfo.getUserName());
        }
        if (isExitConversation(bean.getChatId())) {
            return ConversationDBManager.getInstance().updateConversation(conversationBean);
        }
        return ConversationDBManager.getInstance().addConversationBean(conversationBean);
    }

    @Override
    public List<ConversationBean> getConversationList() {
        return ConversationDBManager.getInstance().getConversationList();
    }

    @Override
    public boolean isExitConversation(String chatId) {
        return !TextUtils.isEmpty(chatId)
                && ConversationDBManager.getInstance().isExistConversation(chatId);
    }

    @Override
    public String getConversationIdByChatId(String chatId) {
        if (TextUtils.isEmpty(chatId)) {
            return null;
        }
        return ConversationDBManager.getInstance().getConversationIdByChatId(chatId);
    }

    @Override
    public void updateConversationIdByChatId(String chatId, String conversationId) {
        if (TextUtils.isEmpty(chatId) || TextUtils.isEmpty(conversationId)) {
            return;
        }
        ConversationDBManager.getInstance().updateConversationIdByChatId(chatId, conversationId);
    }

    @Override
    public int getUnReadCountByChatId(String chatId) {
        if (TextUtils.isEmpty(chatId)) {
            return 0;
        }
        return ConversationDBManager.getInstance().getUnReadCountByChatId(chatId);
    }

    @Override
    public void updateUnReadCountByChatId(String chatId, int unReadCount) {
        if (TextUtils.isEmpty(chatId)) {
            return;
        }
        // 如果未读数大于0，则在原来基数上增加，否则减
        String unRead;
        if (unReadCount >= 0) {
            unRead = "+" + unReadCount;
        } else {
            unRead = String.valueOf(unReadCount);
        }
        ConversationDBManager.getInstance().updateUnReadCountByChatId(chatId, unRead);
    }

    @Override
    public void clearUnReadCountByChatId(String chatId) {
        if (TextUtils.isEmpty(chatId)) {
            return;
        }
        ConversationDBManager.getInstance().clearUnReadCountByChatId(chatId);
    }
}
