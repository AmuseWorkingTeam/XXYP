
package com.xxyp.xxyp.message.model;

import android.text.TextUtils;

import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.MessageImageBean;
import com.xxyp.xxyp.message.bean.MessageShotBean;
import com.xxyp.xxyp.message.bean.MessageVoiceBean;
import com.xxyp.xxyp.message.contract.ChatBaseContract;
import com.xxyp.xxyp.message.dao.ChatMessageDBManager;
import com.xxyp.xxyp.message.dao.RelationResourceDBManager;
import com.xxyp.xxyp.message.utils.MessageConfig;

import java.util.List;

/**
 * Description : 聊天基础model Created by sunpengfei on 2017/8/22. Person in charge
 * : sunpengfei
 */
public abstract class ChatBaseModel implements ChatBaseContract.Model {

    @Override
    public long addRelationResource(ChatMessageBean bean) {
        if (bean == null) {
            return -1;
        }
        long relationSourceId = -1;
        switch (bean.getMsgType()) {
            case MessageConfig.MessageType.MSG_VOICE:
                relationSourceId = addMessageVoice(bean.getVoiceBean(), bean.getChatId());
                if (bean.getVoiceBean() != null) {
                    bean.getVoiceBean().setVoiceId(relationSourceId);
                }
                break;
            case MessageConfig.MessageType.MSG_IMAGE:
                relationSourceId = addMessageImage(bean.getImageBean(), bean.getChatId());
                if (bean.getImageBean() != null) {
                    bean.getImageBean().setImgId(relationSourceId);
                }
                break;
            case MessageConfig.MessageType.MSG_APPOINTMENT:
                relationSourceId = addMessageShot(bean.getShotBean(), bean.getChatId());
                if (bean.getShotBean() != null) {
                    bean.getShotBean().setShotId(relationSourceId);
                }
                break;
            default:
                break;
        }
        return relationSourceId;
    }

    @Override
    public long addMessageImage(MessageImageBean imageBean, String belongTo) {
        if (imageBean == null) {
            return -1;
        }
        imageBean.setBelongTo(belongTo);
        imageBean.setImgId(-1);
        return RelationResourceDBManager.getInstance().addMessageImage(imageBean);
    }

    @Override
    public long updateMessageImage(MessageImageBean imageBean) {
        if (imageBean == null) {
            return -1;
        }
        return RelationResourceDBManager.getInstance().updateMessageImage(imageBean);
    }

    @Override
    public void deleteMessageImage(long imgId, String belongTo) {
        if(imgId < 0 || TextUtils.isEmpty(belongTo)){
            RelationResourceDBManager.getInstance().removeMessageImage(imgId, belongTo);
        }
    }

    @Override
    public List<MessageImageBean> getMessageImages(String belongTo) {
        if (TextUtils.isEmpty(belongTo)) {
            return null;
        }
        return RelationResourceDBManager.getInstance().getMessageImages(belongTo);
    }

    @Override
    public long addMessageVoice(MessageVoiceBean voiceBean, String belongTo) {
        if (voiceBean == null) {
            return -1;
        }
        voiceBean.setBelongTo(belongTo);
        voiceBean.setVoiceId(-1);
        return RelationResourceDBManager.getInstance().addMessageVoice(voiceBean);
    }

    @Override
    public long updateMessageVoice(MessageVoiceBean voiceBean) {
        if (voiceBean == null) {
            return -1;
        }
        return RelationResourceDBManager.getInstance().updateMessageVoice(voiceBean);
    }

    @Override
    public void deleteMessageVoice(long voiceId, String belongTo) {
        if(voiceId < 0 || TextUtils.isEmpty(belongTo)){
            RelationResourceDBManager.getInstance().removeMessageVoice(voiceId, belongTo);
        }
    }

    @Override
    public long addMessageShot(MessageShotBean shotBean, String belongTo) {
        if (shotBean == null) {
            return -1;
        }
        shotBean.setBelongTo(belongTo);
        shotBean.setShotId(-1);
        return RelationResourceDBManager.getInstance().addMessageShot(shotBean);
    }

    @Override
    public long updateMessageShot(MessageShotBean shotBean) {
        if (shotBean == null) {
            return -1;
        }
        return RelationResourceDBManager.getInstance().updateMessageShot(shotBean);
    }

    @Override
    public void deleteMessageShot(long shotId, String belongTo) {
        if(shotId < 0 || TextUtils.isEmpty(belongTo)){
            RelationResourceDBManager.getInstance().removeMessageShot(shotId, belongTo);
        }
    }

    @Override
    public long updateMessageContent(int chatType, String msgId, String content) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(msgId)) {
            return -1;
        }
        return ChatMessageDBManager.getInstance().updateMessageContent(chatType, msgId, content);
    }

    @Override
    public long updateMessageSendStatus(int chatType, int status, String msgId) {
        if (TextUtils.isEmpty(msgId)) {
            return -1;
        }
        return ChatMessageDBManager.getInstance().updateMessageStatus(chatType, msgId, status);
    }

    @Override
    public long updateVoiceMessageStatus(int status, long voiceId) {
        if (status == -1 || voiceId == -1) {
            return -1;
        }
        return RelationResourceDBManager.getInstance().updateVoiceMessageStatus(status, voiceId);
    }

    @Override
    public void removeVoiceMessageByBelongTo(String belongTo) {
        RelationResourceDBManager.getInstance().removeMessageVoiceByBelongTo(belongTo);
    }

    @Override
    public void removeMessageImageByBelongTo(String belongTo) {
        RelationResourceDBManager.getInstance().removeMessageImageByBelongTo(belongTo);
    }

    @Override
    public void removeMessageShotByBelongTo(String belongTo) {
        RelationResourceDBManager.getInstance().removeMessageImageByBelongTo(belongTo);
    }

    @Override
    public void removeMessageResource(String belongTo) {
        removeMessageImageByBelongTo(belongTo);
        removeVoiceMessageByBelongTo(belongTo);
        removeMessageShotByBelongTo(belongTo);
    }

    @Override
    public int clearChatMessage(String chatId, int chatType) {
        // 清除聊天数据
        ChatMessageDBManager.getInstance().clearChatMessageByChatId(chatId, chatType);
        // 清除资源信息
        removeMessageResource(chatId);
        return 0;
    }

    @Override
    public void deleteMessage(ChatMessageBean bean) {
        // 清除聊天数据
        ChatMessageDBManager.getInstance().deleteChatMessage(bean);
        // 清除资源信息
        long resourceId = bean.getRelationSourceId();
        switch (bean.getMsgType()) {
            case MessageConfig.MessageType.MSG_IMAGE:
                RelationResourceDBManager.getInstance().removeMessageImage(resourceId, bean.getChatId());
                break;
            case MessageConfig.MessageType.MSG_VOICE:
                RelationResourceDBManager.getInstance().removeMessageVoice(resourceId, bean.getChatId());
                break;
            case MessageConfig.MessageType.MSG_APPOINTMENT:
                RelationResourceDBManager.getInstance().removeMessageShot(resourceId, bean.getChatId());
                break;
            default:
                break;
        }
    }
}
