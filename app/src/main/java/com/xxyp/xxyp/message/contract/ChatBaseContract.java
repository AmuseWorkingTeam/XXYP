
package com.xxyp.xxyp.message.contract;

import android.content.Intent;
import android.os.Bundle;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.MessageImageBean;
import com.xxyp.xxyp.message.bean.MessageVoiceBean;

import java.util.List;

/**
 * Description : 聊天基类contact Created by sunpengfei on 2017/8/15. Person in
 * charge : sunpengfei Leader：wangxiaohui
 */
public interface ChatBaseContract {

    /**
     * 聊天基类view
     */
    interface View extends IBaseView<Presenter> {

        /**
         * 设置聊天双方id
         * @param chatType  聊天类型
         * @param myUserId      我的id
         * @param chatId    对方id
         */
        void setChatIds(int chatType, String myUserId, String chatId);

        /**
         * 展示消息
         * @param beans 消息列表
         */
        void showChatMessages(List<ChatMessageBean> beans);

        /**
         * 展示下拉消息
         * @param beans  消息列表
         */
        void showPullChatMessages(List<ChatMessageBean> beans);

        /**
         * 发送消息
         * @param bean  消息体
         */
        void sendChatMessage(ChatMessageBean bean);

        /**
         * 发送消息列表
         * @param beans  消息列表
         */
        void sendChatMessages(List<ChatMessageBean> beans);

        /**
         * 更新某条消息
         * @param bean 消息
         */
        void updateChatMessage(ChatMessageBean bean);

        /**
         * 展示录制语音view
         */
        void showRecordView();

        /**
         * 隐藏语音录制view
         */
        void hideRecordView();
    }

    /**
     * 聊天基类presenter
     */
    interface Presenter extends IBasePresenter<View> {

        /**
         * 保存页面信息
         *
         * @param outState 页面数据
         */
        void onSaveInstanceState(Bundle outState);

        /**
         * 获取页面信息
         *
         * @param outState 页面数据
         */
        void onRestoreInstanceState(Bundle outState);

        /**
         * 设置聊天双方数据
         * @param chatType  聊天类型
         * @param myUserId  我的id
         * @param chatId    对方id
         */
        void setChatInfo(int chatType, String myUserId, String chatId);

        /**
         * 设置聊天presenter model
         * @param view   view
         * @param model  model
         */
        void setChatBaseView(ChatBaseContract.View view, ChatBaseContract.Model model);

        /**
         * 获取消息列表
         * @param chatType  聊天类型
         * @param chatId    对方id
         */
        void getChatMessages(int chatType, String chatId);

        /**
         * 获取下拉消息
         * @param messageTime 消息时间
         * @param chatType  聊天类型
         * @param chatId    对方id
         */
        void getPullChatMessages(long messageTime, int chatType, String chatId);

        /**
         * 发送文本消息
         * @param text 文本
         */
        void onSendTextRequest(String text);

        /**
         * 功能面板
         * @param functionType 类型
         */
        void onFunctionRequest(int functionType);

        /**
         * 发送语音
         * @param voiceAction  语音录制事件
         * @param voiceTime 语音录制时间
         */
        void onSendVoiceRequest(int voiceAction, long voiceTime);

        /**
         * 停止一切占用音频播放的
         */
        void stopAudio();

        /**
         * 返回数据
         * @param requestCode  请求码
         * @param resultCode   返回码
         * @param data         返回数据
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);

        /**
         * 点击好友头像跳转到好友详情
         */
        void onGoToUserDetail(String userId);

        /**
         * 聊天页面内图片点击跳转大图监听
         */
        void onClickToBigImgListener(android.view.View view, ChatMessageBean chatBean);


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
         * 消息重发监听
         */
        void onReSendMessageListener(ChatMessageBean chatBean);

        /**
         * 消息复制监听
         * @param chatBean 聊天消息体
         */
        void onChatCopy(ChatMessageBean chatBean);

        /**
         * 消息删除监听
         * @param chatBean 聊天消息体
         */
        void onChatDel(ChatMessageBean chatBean);

        /**
         * 消息长按点击
         * @param chatBean 聊天消息体
         */
        void onMessageLongClick(ChatMessageBean chatBean);

    }

    /**
     * 消息model
     */
    interface Model {

        /**
         * 获取消息列表
         * @param chatType  消息类型
         * @param chatId    对方id
         * @param messageTime  消息时间
         * @param pageSize  消息条目
         * @return List
         */
        List<ChatMessageBean> getChatMessages(int chatType, String chatId, long messageTime,
                                              int pageSize);

        /**
         * 添加消息入库
         * @param bean 消息体
         * @return long
         */
        long addChatMessage(ChatMessageBean bean);

        /**
         * 添加图片消息入库
         * @param imageBean  图片信息
         * @param belongTo   发送者id
         * @return long
         */
        long addMessageImage(MessageImageBean imageBean, String belongTo);

        /**
         * 更新图片消息
         * @param imageBean  图片信息
         * @return long
         */
        long updateMessageImage(MessageImageBean imageBean);

        /**
         * 根据belongTo获取所有图片
         * @param belongTo  所属会话id
         * @return List<MessageImageBean>
         */
        List<MessageImageBean> getMessageImages(String belongTo);

        /**
         * 添加语音消息入库
         * @param voiceBean  语音信息
         * @param belongTo   发送者id
         * @return long
         */
        long addMessageVoice(MessageVoiceBean voiceBean, String belongTo);


        /**
         * 更新语音消息
         * @param voiceBean  语音信息
         * @return long
         */
        long updateMessageVoice(MessageVoiceBean voiceBean);

        /**
         * 添加资源消息入库
         * @param bean  消息
         * @return long
         */
        long addRelationResource(ChatMessageBean bean);

        /**
         * 根据msgId更新消息content
         * @param chatType  聊天类型
         * @param content   content
         * @param msgId     消息id
         * @return long
         */
        long updateMessageContent(int chatType, String content, String msgId);

        /**
         * 根据msgId更新消息sendStatus
         * @param chatType  聊天类型
         * @param status   发送状态
         * @param msgId     消息id
         * @return long
         */
        long updateMessageSendStatus(int chatType, int status, String msgId);

        /**
         * 更新语音状态
         *
         * @param status 0未读，1已读
         * @param voiceId 语音id
         * @return long
         */
        long updateVoiceMessageStatus(int status, long voiceId);

        /**
         * 移除会话下语音信息
         *
         * @param belongTo 所属会话
         */
        void removeVoiceMessageByBelongTo(String belongTo);

        /**
         * 移除会话下图片信息
         *
         * @param belongTo 所属会话
         */
        void removeMessageImageByBelongTo(String belongTo);

        /**
         * 移除会话下的资源信息
         * @param belongTo 所属会话
         */
        void removeMessageResource(String belongTo);

        /**
         * 清除和聊天对象的消息
         *
         * @param chatId 聊天对象id：群聊id或者聊id
         * @param chatType 聊天类型
         */
        int clearChatMessage(String chatId, int chatType);
    }

}
