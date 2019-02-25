
package com.xxyp.xxyp.message.interfaces;


import android.view.View;

import com.xxyp.xxyp.message.bean.ChatMessageBean;

/**
 * Description : 聊天操作监听
 * Created by sunpengfei on 2015/12/31.
 * Person in charge : sunpengfei
 */
public interface ChatActionListener {

    /**
     * 点击好友头像跳转到好友详情
     */
    void onGoToUserDetail(String userId);

    /**
     * 聊天页面内图片点击跳转大图监听
     */
    void onClickToBigImgListener(View view, ChatMessageBean chatBean);


    /**
     * 语音播放监听
     */
    void onPlayVoiceListener(ChatMessageBean chatBean);

    /**
     * 跳转到约拍详情
     * @param chatBean
     */
    void onGoToShotDetail(ChatMessageBean chatBean);

    /**
     * 更新约拍
     * @param chatBean
     */
    void onUpdateShot(ChatMessageBean chatBean, int targetStatus);

    /**
     * 接受约拍
     * @param chatBean
     */
    void onUpdateShotStatus(ChatMessageBean chatBean);

    /**
     * 消息重发监听
     */
    void onReSendMessageListener(ChatMessageBean chatBean);

    /**
     * 消息长按点击
     * @param chatBean 聊天消息体
     */
    void onMessageLongClick(ChatMessageBean chatBean);

}
