
package com.xxyp.xxyp.message.utils;

/**
 * Description : 消息常量
 * Created by sunpengfei on 2017/8/14.
 * Person in charge : sunpengfei
 */
public interface MessageConfig {

    /**
     * 会话id
     */
    String CHAT_ID = "chat_id";

    /**
     * 会话type
     */
    String CHAT_TYPE = "chat_type";

    /**
     * 拍照路径
     */
    String KEY_CAMERA_PATH = "camera_path";

    /**
     * 默认消息条目
     */
    int DEFAULT_PAGE_SIZE = 20;

    /**
     * 消息展示列表type
     */
    interface ChatListViewItemType {

        int TEXT_LEFT = 0;

        int TEXT_RIGHT = 1;

        int VOICE_LEFT = 2;

        int VOICE_RIGHT = 3;

        int IMAGE_LEFT = 4;

        int IMAGE_RIGHT = 5;

        int APPOINTMENT_LEFT = 6;

        int APPOINTMENT_RIGHT = 7;

        int NOTICE = 8;
    }

    /**
     * 消息类型
     */
    interface MessageCatalog {
        //单聊
        int CHAT_SINGLE = 1;
    }

    /**
     * 消息类型
     */
    interface MessageType {
        int MSG_TEXT = 1;

        int MSG_VOICE = 2;

        int MSG_IMAGE = 3;

        int MSG_APPOINTMENT = 4;

        // 通知类消息
        int MSG_NOTICE = 5;

        // 操作类消息
        int MSG_OPERATE = 6;
    }

    /**
     * 消息发送状态
     */
    interface MessageSendStatus {

        int SEND_MSG_ING = 0;

        int SEND_NSG_FAIL = 1;

        int SEND_MSG_SUCCESS = 2;
    }

    /**
     * 消息发送者
     */
    interface MessageSender {

        int MY_SEND = 0;

        int OTHER_SEND = 1;
    }

    /**
     * 媒体的格式
     */
    interface MediaFormat {
        /** 音频文件格式 **/
        String VOICE_FORMAT = ".amr";
    }

    /**
     * 语音状态
     */
    interface VoiceStatus {

        // 语音消息未读状态
        int VOICE_UNREAD = 0;

        // 语音消息正在播放状态
        int VOICE_PLAY = 1;

        // 语音消息正在录制状态
        int VOICE_RECORD = 2;

        // 语音消息已读状态
        int VOICE_READED = 3;
    }

    /**
     * 语音状态
     */
    interface ShotStatus {

        // 删除状态
        int SHOT_DEL = 0;

        // 新建
        int SHOT_CREATE = 1;

        // 已被接受
        int SHOT_DATED = 2;

        // 完成状态
        int SHOT_DONE = 3;
    }

    /**
     * 语音状态
     */
    interface OperateType {

        // 删除状态
        int TYPE_DEL = 0;

        // 更新状态
        int TYPE_UPDATE = 1;

    }

}
