
package com.xxyp.xxyp.message.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuUtils;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.MessageBean;
import com.xxyp.xxyp.message.bean.MessageBodyBean;
import com.xxyp.xxyp.message.bean.MessageContentBean;
import com.xxyp.xxyp.message.bean.MessageImageBean;
import com.xxyp.xxyp.message.bean.MessageNoticeBean;
import com.xxyp.xxyp.message.bean.MessageOperateBean;
import com.xxyp.xxyp.message.bean.MessageShotBean;
import com.xxyp.xxyp.message.bean.MessageVoiceBean;
import com.xxyp.xxyp.message.service.PacketMessage;

import java.util.UUID;

/**
 * Description : 消息构造工具类 Created by sunpengfei on 2017/8/14. Person in charge :
 * sunpengfei
 */
public class MessageUtils {

    /**
     * 构造聊天消息体
     * 
     * @param chatMessageBean
     */
    public static void buildChatMessageContent(ChatMessageBean chatMessageBean) {
        if (chatMessageBean == null) {
            return;
        }
        handlePacketContent(chatMessageBean, chatMessageBean.getContent());
        MessageContentBean contentBean = chatMessageBean.getContentBean();
        int chatType = contentBean.getContentType();
        chatMessageBean.setMsgType(chatType);
        switch (chatType) {
            case MessageConfig.MessageType.MSG_TEXT:
                break;
            case MessageConfig.MessageType.MSG_VOICE:
                buildMessageVoice(chatMessageBean, chatMessageBean.getBodyBean());
                break;
            case MessageConfig.MessageType.MSG_IMAGE:
                buildMessageImage(chatMessageBean, chatMessageBean.getBodyBean());
                break;
            case MessageConfig.MessageType.MSG_APPOINTMENT:
                buildMessageShot(chatMessageBean, chatMessageBean.getBodyBean());
                break;
            case MessageConfig.MessageType.MSG_NOTICE:
                buildMessageNotice(chatMessageBean, chatMessageBean.getBodyBean());
                break;
            case MessageConfig.MessageType.MSG_OPERATE:
                buildMessageOperate(chatMessageBean, chatMessageBean.getBodyBean());
                break;
            default:
                break;
        }
    }

    /**
     * 构造聊天消息体
     * 
     * @param messageBean 消息体
     * @return ChatMessageBean
     */
    public static ChatMessageBean buildChatMsgBean(MessageBean messageBean) {
        if (messageBean == null) {
            return null;
        }
        ChatMessageBean chatMessageBean = new ChatMessageBean(messageBean);
        if (isMySend(messageBean.getFromId())) {
            chatMessageBean.setSender(MessageConfig.MessageSender.MY_SEND);
            // 我发送的 chatId为对方
            chatMessageBean.setChatId(messageBean.getToId());
        } else {
            // 别人发送的 chatId为发送者
            chatMessageBean.setSender(MessageConfig.MessageSender.OTHER_SEND);
            chatMessageBean.setChatId(messageBean.getFromId());
        }
        chatMessageBean.setSendId(messageBean.getFromId());
        chatMessageBean.setSendStatus(MessageConfig.MessageSendStatus.SEND_MSG_SUCCESS);
        buildChatMessageContent(chatMessageBean);
        return chatMessageBean;
    }

    /**
     * 通过消息包构造消息体
     * 
     * @param packetMessage 消息包
     * @param avimMsgId leanCloud的avimMsgId
     * @param conversationId leanCloud的conversationId
     * @return MessageBean
     */
    public static MessageBean buildMessageBeanByPacket(PacketMessage packetMessage,
            String avimMsgId, String conversationId) {
        if (packetMessage == null) {
            return null;
        }
        MessageBean messageBean = new MessageBean();
        messageBean.setChatType(packetMessage.getCatalog());
        messageBean.setContent(packetMessage.getMessage());
        messageBean.setFromId(packetMessage.getFromId());
        messageBean.setToId(packetMessage.getToId());
        if (!TextUtils.isEmpty(packetMessage.getMsgTime())) {
            messageBean.setMessageTime(Long.valueOf(packetMessage.getMsgTime()));
        }
        messageBean.setMsgId(packetMessage.getMsgId());
        messageBean.setAvimMsgId(avimMsgId);
        messageBean.setConversationId(conversationId);
        handlePacketContent(messageBean, messageBean.getContent());
        return messageBean;
    }

    /**
     * 构造图片消息
     * 
     * @param chatMessageBean
     * @param bodyBean
     */
    private static void buildMessageImage(ChatMessageBean chatMessageBean,
            MessageBodyBean bodyBean) {
        if (chatMessageBean == null || bodyBean == null) {
            return;
        }
        MessageImageBean imageBean = new MessageImageBean();
        if (!TextUtils.isEmpty(bodyBean.getUrl())) {
            String url = bodyBean.getUrl();
            imageBean.setImageUrl(url);
            imageBean.setThumbImageUrl(QiNiuUtils.buildPicThumbUrl(url, 3, 300, 300, "webp"));
        }
        if (!TextUtils.isEmpty(bodyBean.getW())) {
            imageBean.setImageWidth(Integer.parseInt(bodyBean.getW()));
        }
        if (!TextUtils.isEmpty(bodyBean.getH())) {
            imageBean.setImageHeight(Integer.parseInt(bodyBean.getH()));
        }
        chatMessageBean.setImageBean(imageBean);
    }

    /**
     * 构造语音消息
     * 
     * @param chatMessageBean
     * @param bodyBean
     */
    private static void buildMessageVoice(ChatMessageBean chatMessageBean,
            MessageBodyBean bodyBean) {
        if (chatMessageBean == null || bodyBean == null) {
            return;
        }
        MessageVoiceBean voiceBean = new MessageVoiceBean();
        if (!TextUtils.isEmpty(bodyBean.getUrl())) {
            voiceBean.setVoiceUrl(bodyBean.getUrl());
        }
        if (!TextUtils.isEmpty(bodyBean.getTime())) {
            voiceBean.setVoiceLen(Integer.parseInt(bodyBean.getTime()));
        }
        voiceBean.setStatus(MessageConfig.VoiceStatus.VOICE_UNREAD);
        chatMessageBean.setVoiceBean(voiceBean);
    }

    /**
     * 构造约拍消息
     *
     * @param chatMessageBean
     * @param bodyBean
     */
    private static void buildMessageShot(ChatMessageBean chatMessageBean,
                                          MessageBodyBean bodyBean) {
        if (chatMessageBean == null || bodyBean == null) {
            return;
        }
        MessageShotBean shotBean = new MessageShotBean();

        shotBean.setDatingShotId(bodyBean.getDatingShotId());
        shotBean.setUserId(bodyBean.getUserId());
        shotBean.setDatingShotAddress(bodyBean.getDatingShotAddress());
        shotBean.setDatingShotTime(bodyBean.getDatingShotTime());
        shotBean.setPurpose(bodyBean.getPurpose());
        shotBean.setPaymentMethod(bodyBean.getPaymentMethod());
        shotBean.setDatingShotIntroduction(bodyBean.getDatingShotIntroduction());
        shotBean.setDescription(bodyBean.getDescription());
        shotBean.setDatingUserId(bodyBean.getDatingUserId());
        shotBean.setStatus(bodyBean.getStatus());
        shotBean.setDatingShotImage(bodyBean.getDatingShotImage());
        chatMessageBean.setShotBean(shotBean);
    }

    /**
     * 构造通知消息
     *
     * @param chatMessageBean
     * @param bodyBean
     */
    public static void buildMessageNotice(ChatMessageBean chatMessageBean,
                                          MessageBodyBean bodyBean) {
        if (chatMessageBean == null || bodyBean == null) {
            return;
        }
        MessageNoticeBean noticeBean = new MessageNoticeBean();
        noticeBean.setText(bodyBean.getText());
        chatMessageBean.setNoticeBean(noticeBean);
    }

    /**
     * 构造操作消息
     *
     * @param chatMessageBean
     * @param bodyBean
     */
    private static void buildMessageOperate(ChatMessageBean chatMessageBean,
                                          MessageBodyBean bodyBean) {
        if (chatMessageBean == null || bodyBean == null) {
            return;
        }
        MessageOperateBean operateBean = new MessageOperateBean();
        operateBean.setOperateType(bodyBean.getOperateType());
        operateBean.setOperateUserId(bodyBean.getOperateUserId());
        operateBean.setOperateMsgId(bodyBean.getOperateMsgId());
        chatMessageBean.setOperateBean(operateBean);
    }

    /**
     * 构造发送的消息包
     * 
     * @param messageBean 消息体
     * @return PacketMessage
     */
    public static PacketMessage buildPacketMessage(MessageBean messageBean) {
        PacketMessage packetMessage = new PacketMessage();
        if (messageBean == null) {
            return packetMessage;
        }
        packetMessage.setCatalog(messageBean.getChatType());
        packetMessage.setMessage(messageBean.getContent());
        packetMessage.setFromId(messageBean.getFromId());
        packetMessage.setToId(messageBean.getToId());
        packetMessage.setMsgTime(
                String.valueOf(messageBean.getMessageTime() != 0 ? messageBean.getMessageTime()
                        : System.currentTimeMillis()));
        packetMessage.setMsgId(
                !TextUtils.isEmpty(messageBean.getMsgId()) ? messageBean.getMsgId() : buildMsgId());
        return packetMessage;
    }

    /**
     * 构造发送的消息体
     * 
     * @param chatMessageBean 消息体
     */
    public static void buildSendMessage(ChatMessageBean chatMessageBean) {
        if (chatMessageBean == null) {
            return;
        }
        MessageBodyBean bodyBean = chatMessageBean.getBodyBean();
        if (bodyBean == null) {
            bodyBean = new MessageBodyBean();
            chatMessageBean.setBodyBean(bodyBean);
        }
        MessageContentBean contentBean = chatMessageBean.getContentBean();
        if (contentBean == null) {
            contentBean = new MessageContentBean();
            chatMessageBean.setContentBean(contentBean);
        }
        int chatType = chatMessageBean.getMsgType();
        contentBean.setContentType(chatType);
        switch (chatType) {
            case MessageConfig.MessageType.MSG_VOICE:
                MessageVoiceBean voiceBean = chatMessageBean.getVoiceBean();
                if (voiceBean != null) {
                    bodyBean.setTime(String.valueOf(voiceBean.getVoiceLen()));
                    bodyBean.setUrl(voiceBean.getVoiceUrl());
                }
                break;
            case MessageConfig.MessageType.MSG_IMAGE:
                MessageImageBean imageBean = chatMessageBean.getImageBean();
                if (imageBean != null) {
                    bodyBean.setW(String.valueOf(imageBean.getImageWidth()));
                    bodyBean.setH(String.valueOf(imageBean.getImageHeight()));
                    bodyBean.setUrl(imageBean.getImageUrl());
                }
                break;
            case MessageConfig.MessageType.MSG_APPOINTMENT:
                MessageShotBean shotBean = chatMessageBean.getShotBean();
                if (shotBean != null) {
                    bodyBean.setDatingShotId(shotBean.getDatingShotId());
                    bodyBean.setUserId(shotBean.getUserId());
                    bodyBean.setDatingShotAddress(shotBean.getDatingShotAddress());
                    bodyBean.setDatingShotTime(shotBean.getDatingShotTime());
                    bodyBean.setPurpose(shotBean.getPurpose());
                    bodyBean.setPaymentMethod(shotBean.getPaymentMethod());
                    bodyBean.setDatingShotIntroduction(shotBean.getDatingShotIntroduction());
                    bodyBean.setDescription(shotBean.getDescription());
                    bodyBean.setDatingUserId(shotBean.getDatingUserId());
                    bodyBean.setStatus(shotBean.getStatus());
                    bodyBean.setDatingShotImage(shotBean.getDatingShotImage());
                }
                break;
            case MessageConfig.MessageType.MSG_OPERATE:
                MessageOperateBean operateBean = chatMessageBean.getOperateBean();
                if (operateBean != null) {
                    bodyBean.setOperateMsgId(operateBean.getOperateMsgId());
                    bodyBean.setOperateType(operateBean.getOperateType());
                    bodyBean.setOperateUserId(operateBean.getOperateUserId());
                }
                break;
            case MessageConfig.MessageType.MSG_NOTICE:
                MessageNoticeBean noticeBean = chatMessageBean.getNoticeBean();
                if (noticeBean != null) {
                    bodyBean.setText(noticeBean.getText());
                }
                break;
            default:
                break;
        }
        // 消息体的content内容
        Gson gson = new Gson();
        String body = gson.toJson(bodyBean);
        // 把body的内容封装到网络body中
        contentBean.setContent(TextUtils.isEmpty(body) ? "" : body);
        // 重新设置contentType 防止出现空值现象
        contentBean.setContentType(chatType);
        // 需要封装发给对方
        chatMessageBean.setContent(gson.toJson(contentBean));
    }

    /**
     * 处理PacketContent
     * 
     * @param messageBean 消息体
     * @param messageContent 消息content
     */
    public static void handlePacketContent(MessageBean messageBean, String messageContent) {
        if (messageBean == null || TextUtils.isEmpty(messageContent)) {
            return;
        }
        MessageContentBean contentBean = messageBean.getContentBean();
        if (contentBean == null) {
            contentBean = new Gson().fromJson(messageContent, MessageContentBean.class);
        }
        messageBean.setContentBean(contentBean);
        buildBodyBean(messageBean, contentBean);
    }

    /**
     * 构造消息body
     * 
     * @param messageBean
     * @param contentBean
     */
    private static void buildBodyBean(MessageBean messageBean, MessageContentBean contentBean) {
        if (messageBean == null || contentBean == null
                || TextUtils.isEmpty(contentBean.getContent())) {
            return;
        }
        MessageBodyBean bodyBean = new Gson().fromJson(contentBean.getContent(),
                MessageBodyBean.class);
        messageBean.setBodyBean(bodyBean);
    }

    /**
     * 是否是我发送的消息
     * 
     * @param fromId 发送者id
     * @return boolean
     */
    public static boolean isMySend(String fromId) {
        return TextUtils.equals(SharePreferenceUtils.getInstance().getUserId(), fromId);
    }

    /**
     * 生成消息id
     * 
     * @return String
     */
    public static String buildMsgId() {
        return UUID.randomUUID().toString();
    }
}
