
package com.xxyp.xxyp.message.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.ConversationBean;

import java.util.List;

/**
 * Description : 会话contract
 * Created by sunpengfei on 2017/8/24.
 * Person in charge : sunpengfei
 */
public interface MessageContract {

    /**
     * 会话view
     */
    interface View extends IBaseView<Presenter> {

        /**
         * 展示会话数据
         * @param beans  数据
         */
        void showConversationList(List<ConversationBean> beans);
    }

    /**
     * 会话presenter
     */
    interface Presenter extends IBasePresenter<View> {

        /**
         * 获取会话数据
         */
        void getConversationList();

        /**
         * 打开聊天页面
         * @param chatId   会话id
         * @param chatType 会话type
         */
        void openChat(String chatId, int chatType);

        /**
         * 清除会话
         * @param chatId   会话id
         * @param chatType 聊天类型
         */
        void clearConversation(String chatId, int chatType);
    }

    /**
     * 会话model
     */
    interface Model {

        /**
         * 增加会话数据
         * @param bean  聊天数据
         * @return long
         */
        long addOrUpdateConversation(ChatMessageBean bean);

        /**
         * 获取会话数据
         * @return List<ConversationBean>
         */
        List<ConversationBean> getConversationList();

        /**
         * 判断是否存在会话
         * @param chatId  会话id
         * @return boolean
         */
        boolean isExitConversation(String chatId);

        /**
         * 获取会话的LeanCloud的conversationId
         * @param chatId  会话id
         * @return Sring
         */
        String getConversationIdByChatId(String chatId);

        /**
         * 更新会话的LeanCloud的conversationId
         * @param chatId  会话id
         */
        void updateConversationIdByChatId(String chatId, String conversationId);

        /**
         * 获取未读数
         * @param chatId 会话id
         * @return int
         */
        int getUnReadCountByChatId(String chatId);

        /**
         * 修改未读数
         * @param chatId 会话id
         * @param unReadCount 未读数
         * @return long
         */
        void updateUnReadCountByChatId(String chatId, int unReadCount);

        /**
         * 清除未读数
         * @param chatId 会话id
         */
        void clearUnReadCountByChatId(String chatId);

        /**
         * 删除
         * @param chatId 会话id
         */
        void deleteSession(String chatId);
    }
}
