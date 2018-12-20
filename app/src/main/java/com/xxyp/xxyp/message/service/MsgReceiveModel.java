
package com.xxyp.xxyp.message.service;

import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.MessageBean;
import com.xxyp.xxyp.message.utils.MessageConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description : 消息接收model
 * Created by sunpengfei on 2017/8/14.
 * Person in charge : sunpengfei
 */
public class MsgReceiveModel {

    private static MsgReceiveModel mInstance;

    private Map<String, MessageListenerManager.OnMsgReceiveListener> recMsgListeners = new HashMap<>();

    public static MsgReceiveModel getInstance() {
        if (mInstance == null) {
            synchronized (MsgReceiveModel.class){
                if (mInstance == null){
                    mInstance = new MsgReceiveModel();
                }
            }
        }
        return mInstance;
    }

    private MsgReceiveModel(){

    }

    /**
     * 收到消息
     * @param messageBean  消息体
     */
    @SuppressWarnings("unchecked")
    public void receiveMessage(MessageBean messageBean) {
        int chatType = messageBean.getChatType();
        Observable observable = null;
        switch (chatType) {
            case MessageConfig.MessageCatalog.CHAT_SINGLE:
                observable = ChatSingleProcess.getInstance().processMessage(messageBean);
                break;
            default:
                break;
        }
        if(observable != null){
            notifyUI(observable);
        }
    }

    /**
     * 消息接受回调给页面
     * @param observable
     */
    private void notifyUI(Observable<ChatMessageBean> observable) {
        if (observable == null) {
            return;
        }
        observable.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChatMessageBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ChatMessageBean chatMessageBean) {
                        for (MessageListenerManager.OnMsgReceiveListener onMsgReceiveListener : recMsgListeners
                                .values()) {
                            onMsgReceiveListener.onReceiveMessage(chatMessageBean);
                        }
                    }
                });
    }

    /**
     * 注册接收消息监听
     * @param onMsgReceiveListener  监听
     */
    public void registerReceiveListener(
            MessageListenerManager.OnMsgReceiveListener onMsgReceiveListener) {
        if (onMsgReceiveListener != null) {
            recMsgListeners.put(onMsgReceiveListener.getClass().getName(), onMsgReceiveListener);
        }
    }

    /**
     * 取消注册消息监听
     * @param onMsgReceiveListener  监听
     */
    public void removeReceiveListener(
            MessageListenerManager.OnMsgReceiveListener onMsgReceiveListener) {
        if (onMsgReceiveListener != null) {
            recMsgListeners.remove(onMsgReceiveListener.getClass().getName());
        }
    }

}
