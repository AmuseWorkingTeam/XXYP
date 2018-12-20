package com.xxyp.xxyp.message.presenter;

import android.app.Activity;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.message.bean.ConversationBean;
import com.xxyp.xxyp.message.contract.MessageContract;
import com.xxyp.xxyp.message.model.ChatSingleModel;
import com.xxyp.xxyp.message.model.MessageModel;
import com.xxyp.xxyp.message.provider.ChatProvider;
import com.xxyp.xxyp.message.utils.MessageConfig;
import com.xxyp.xxyp.user.provider.UserProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Description : 会话presenter
 * Created by sunpengfei on 2017/8/24.
 * Person in charge : sunpengfei
 */
public class MessagePresenter implements MessageContract.Presenter {

    private MessageContract.View mView;

    private MessageContract.Model mModel;

    public MessagePresenter(MessageContract.View view) {
        mView = view;
        mModel = new MessageModel();
    }

    @Override
    public void getConversationList() {
        Observable.just("").map(new Func1<String, List<ConversationBean>>() {
            @Override
            public List<ConversationBean> call(String s) {
                return mModel.getConversationList();
            }
        }).filter(new Func1<List<ConversationBean>, Boolean>() {
            @Override
            public Boolean call(List<ConversationBean> conversationBeen) {
                return conversationBeen != null && conversationBeen.size() > 0;
            }
        }).map(new Func1<List<ConversationBean>, List<ConversationBean>>() {
            @Override
            public List<ConversationBean> call(List<ConversationBean> conversationBeanList) {
                List<String> notFound = new ArrayList<>();
                for (ConversationBean bean : conversationBeanList) {
                    switch (bean.getChatType()) {
                        case MessageConfig.MessageCatalog.CHAT_SINGLE:
                            UserInfo userInfo = UserProvider.getUserInfoByDB(bean.getChatId());
                            if(userInfo != null){
                                bean.setConversationAvatar(userInfo.getUserImage());
                                bean.setConversationName(userInfo.getUserName());
                            }else{
                                notFound.add(bean.getChatId());
                            }
                            break;
                        default:
                            break;
                    }
                }
                sortByTime(conversationBeanList);
                updateConversation(notFound);
                return conversationBeanList;
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<ConversationBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ConversationBean> conversationBeen) {
                if (mView != null) {
                    mView.showConversationList(conversationBeen);
                }
            }
        });
    }

    /**
     * 会话需要重新设置头像和名称
     *
     * @param userIds id列表
     */
    private void updateConversation(List<String> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return;
        }
        UserProvider.obtainUserInfos(userIds).map(new Func1<List<UserInfo>, List<ConversationBean>>() {
            @Override
            public List<ConversationBean> call(List<UserInfo> userInfos) {
                return mModel.getConversationList();
            }
        }).filter(new Func1<List<ConversationBean>, Boolean>() {
            @Override
            public Boolean call(List<ConversationBean> conversationBeanList) {
                return conversationBeanList != null && conversationBeanList.size() > 0;
            }
        }).map(new Func1<List<ConversationBean>, List<ConversationBean>>() {
            @Override
            public List<ConversationBean> call(List<ConversationBean> conversationBeanList) {
                for (ConversationBean bean : conversationBeanList) {
                    switch (bean.getChatType()) {
                        case MessageConfig.MessageCatalog.CHAT_SINGLE:
                            UserInfo userInfo = UserProvider.getUserInfoByDB(bean.getChatId());
                            if(userInfo != null){
                                bean.setConversationAvatar(userInfo.getUserImage());
                                bean.setConversationName(userInfo.getUserName());
                            }
                            break;
                        default:
                            break;
                    }
                }
                sortByTime(conversationBeanList);
                return conversationBeanList;
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<ConversationBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ConversationBean> conversationBeanList) {
                if (mView != null) {
                    mView.showConversationList(conversationBeanList);
                }
            }
        });
    }

    /**
     * 时间倒序排列
     */
    private void sortByTime(List<ConversationBean> conversationBeanList) {
        if (null == conversationBeanList || conversationBeanList.size() == 0) {
            return;
        }
        Collections.sort(conversationBeanList, new Comparator<ConversationBean>() {
            @Override
            public int compare(ConversationBean lhs, ConversationBean rhs) {
                if (null != lhs && null != rhs) {
                    if (lhs.getCreateTime() < rhs.getCreateTime()) {
                        return 1;
                    }
                    if (lhs.getCreateTime() > rhs.getCreateTime()) {
                        return -1;
                    }
                }
                return 0;
            }
        });
    }

    @Override
    public void openChat(String chatId, int chatType) {
        ChatProvider.openChatActivity((Activity) mView.getContext(), chatType, chatId);
    }

    @Override
    public void clearConversation(String chatId, int chatType) {
        switch (chatType){
            case MessageConfig.MessageCatalog.CHAT_SINGLE:
                new ChatSingleModel().clearChatMessage(chatId, chatType);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
        mModel = null;
    }
}
