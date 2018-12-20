package com.xxyp.xxyp.message.presenter;

import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.contract.ChatSingleContract;
import com.xxyp.xxyp.message.model.ChatSingleModel;
import com.xxyp.xxyp.message.utils.MessageConfig;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Description : 单聊presenter
 * Created by sunpengfei on 2017/8/22.
 * Person in charge : sunpengfei
 */
public class ChatSinglePresenter extends ChatBasePresenter implements ChatSingleContract.Presenter {

    private ChatSingleContract.View mView;

    private ChatSingleContract.Model mModel;

    public ChatSinglePresenter(ChatSingleContract.View view) {
        super();
        mView = view;
        mModel = new ChatSingleModel();
        mView.setPresenter(this);
        setChatBaseView(mView, mModel);
    }

    @Override
    public void getChatMessages(int chatType, String chatId) {
        Observable.just(chatId).map(new Func1<String, List<ChatMessageBean>>() {
            @Override
            public List<ChatMessageBean> call(String s) {
                return mModel.getChatMessages(MessageConfig.MessageCatalog.CHAT_SINGLE, s, 0,
                        MessageConfig.DEFAULT_PAGE_SIZE);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<ChatMessageBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ChatMessageBean> chatMessageBeen) {
                if (mView != null) {
                    mView.showChatMessages(chatMessageBeen);
                }
            }
        });
    }

    @Override
    public void getPullChatMessages(final long messageTime, int chatType, String chatId) {
        Observable.just(chatId).map(new Func1<String, List<ChatMessageBean>>() {
            @Override
            public List<ChatMessageBean> call(String s) {
                return mModel.getChatMessages(MessageConfig.MessageCatalog.CHAT_SINGLE, s, messageTime,
                        MessageConfig.DEFAULT_PAGE_SIZE);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<ChatMessageBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ChatMessageBean> chatMessageBeen) {
                if (mView != null) {
                    mView.showPullChatMessages(chatMessageBeen);
                }
            }
        });
    }
}
