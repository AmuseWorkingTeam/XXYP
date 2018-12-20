
package com.xxyp.xxyp.message.service;

import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.MessageBean;
import com.xxyp.xxyp.message.model.MessageModel;
import com.xxyp.xxyp.message.utils.MessageConfig;
import com.xxyp.xxyp.message.utils.MessageUtils;

import rx.Observable;
import rx.functions.Func1;

/**
 * Description : 消息处理基类
 * Created by sunpengfei on 2017/8/14.
 * Person in charge : sunpengfei
 */
public abstract class BaseMessageProcess {

    /**
     * 消息处理
     * @param messageBean  消息体
     * @return Observable<ChatMessageBean>
     */
    public Observable<ChatMessageBean> processMessage(MessageBean messageBean) {
        if (messageBean == null) {
            return Observable.empty();
        }
        return Observable.just(messageBean).map(new Func1<MessageBean, ChatMessageBean>() {
            @Override
            public ChatMessageBean call(MessageBean messageBean) {
                return MessageUtils.buildChatMsgBean(messageBean);
            }
        }).filter(new Func1<ChatMessageBean, Boolean>() {
            @Override
            public Boolean call(ChatMessageBean chatMessageBean) {
                return saveMessage(chatMessageBean);
            }
        }).map(new Func1<ChatMessageBean, ChatMessageBean>() {
            @Override
            public ChatMessageBean call(ChatMessageBean bean) {
                // 收到消息同时插入/更新到最近会话表
                MessageModel messageModel = new MessageModel();
                long resultId = messageModel.addOrUpdateConversation(bean);
                // 不是我发的更新未读消息数
                if (resultId > 0 && bean.getSender() == MessageConfig.MessageSender.OTHER_SEND) {
                    messageModel.updateUnReadCountByChatId(bean.getChatId(), 1);
                }
                return bean;
            }
        });
    }

    /**
     * 保存消息
     * @param chatMessageBean
     * @return
     */
    abstract boolean saveMessage(ChatMessageBean chatMessageBean);
}
