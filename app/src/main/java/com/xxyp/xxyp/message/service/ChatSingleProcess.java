
package com.xxyp.xxyp.message.service;

import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.model.ChatSingleModel;

/**
 * Description : 单聊消息处理
 * Created by sunpengfei on 2017/8/14.
 * Person in charge : sunpengfei
 */
public class ChatSingleProcess extends BaseMessageProcess {

    private static ChatSingleProcess mInstance;

    public static ChatSingleProcess getInstance(){
        if(mInstance == null){
            synchronized (ChatSingleProcess.class){
                if (mInstance == null)
                    mInstance = new ChatSingleProcess();
            }
        }
        return mInstance;
    }

    private ChatSingleProcess(){

    }

    @Override
    boolean saveMessage(ChatMessageBean chatMessageBean) {
        if(chatMessageBean == null){
            return false;
        }
        return new ChatSingleModel().addChatMessage(chatMessageBean) > 0;
    }
}
