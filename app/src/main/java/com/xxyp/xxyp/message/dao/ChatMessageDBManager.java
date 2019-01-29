
package com.xxyp.xxyp.message.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.DBUtils;
import com.xxyp.xxyp.dao.BaseDao;
import com.xxyp.xxyp.dao.ChatMsgEntityDao;
import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.utils.MessageConfig;
import com.xxyp.xxyp.message.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 聊天数据库 Created by sunpengfei on 2017/8/22. Person in charge :
 * sunpengfei
 */
public class ChatMessageDBManager extends BaseDao {

    private static ChatMessageDBManager mInstance;

    public static ChatMessageDBManager getInstance() {
        if (mInstance == null) {
            synchronized (ChatMessageDBManager.class) {
                if (mInstance == null) {
                    mInstance = new ChatMessageDBManager();
                }
                mInstance.connectionDB();
            }
        }
        return mInstance;
    }

    @Override
    public void initAccess() {

    }

    /**
     * 添加消息到数据库
     * 
     * @param bean 消息数据
     * @return long
     */
    public long addChatMessage(ChatMessageBean bean) {
        if (bean == null) {
            return -1;
        }
        String tableName = getChatMessageTable(bean.getChatType());
        if (TextUtils.isEmpty(tableName)) {
            return -1;
        }
        SQLiteStatement statement = null;
        try {
            String insertSql = DBUtils.buildInsertSql(tableName, new String[] {
                    "MSG_ID", "AVIM_MSG_ID", "CONVERSATION_ID", "CONTENT", "MSG_TYPE",
                    "CREATE_TIME", "SEND_STATUS", "SENDER", "RELATION_SOURCE_ID", "SEND_ID",
                    "CHAT_ID"
            }).toString();
            statement = getDatabase().compileStatement(insertSql);
            return bindValues(statement, bean).executeInsert();
        } catch (Exception e) {
            XXLog.log_e("ChatMessageDBManager", "addChatMessage is failed" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 获取聊天信息
     * 
     * @param chatType 聊天类型
     * @param chatId 会话id
     * @param messageTime 消息时间
     * @param pageSize 会话条目
     * @return List
     */
    public List<ChatMessageBean> getChatMessageList(int chatType, String chatId, long messageTime,
            int pageSize) {
        if (TextUtils.isEmpty(chatId)) {
            return null;
        }
        String tableName = getChatMessageTable(chatType);
        if (TextUtils.isEmpty(tableName)) {
            return null;
        }
        // 查询的列名数组
        String[] selectColumns = {
                "_id", "MSG_ID", "AVIM_MSG_ID", "CONVERSATION_ID", "CONTENT", "MSG_TYPE",
                "CREATE_TIME", "SEND_STATUS", "SENDER", "RELATION_SOURCE_ID", "SEND_ID", "CHAT_ID"
        };
        StringBuilder where = new StringBuilder(" where ");
        DBUtils.buildColumn(where, tableName, "CHAT_ID");
        where.append("='").append(chatId).append("'");
        if (messageTime > 0) {
            where.append(" AND ");
            DBUtils.buildColumn(where, tableName, "CREATE_TIME");
            where.append(" < ").append(messageTime);
        }
        where.append(" ORDER BY ");
        DBUtils.buildColumn(where, tableName, "CREATE_TIME");
        where.append(" DESC , ");
        DBUtils.buildColumn(where, tableName, "_id");
        where.append(" DESC LIMIT ");
        where.append(pageSize);

        String tempTableName = DBUtils.buildSelectSql(tableName, where.toString(), selectColumns)
                .toString();

        StringBuilder selectSql = DBUtils.buildSelectSql(" (", tempTableName + ") ", selectColumns);
        selectSql.append(" AS ").append("temptable").append(" ORDER BY ");
        DBUtils.buildColumn(selectSql, "temptable", "CREATE_TIME");
        selectSql.append(" ASC , ");
        DBUtils.buildColumn(selectSql, "temptable", "_id");
        selectSql.append(" ASC ");
        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(selectSql.toString(), null);
            if (cursor != null) {
                List<ChatMessageBean> beans = new ArrayList<>();
                while (cursor.moveToNext()) {
                    beans.add(cursor2Bean(cursor, chatType));
                }
                return beans;
            }
            return null;
        } catch (Exception e) {
            XXLog.log_e("database", "getChatMessageList is failed:" + e.getMessage());
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 根据消息id更新content
     *
     * @param msgId 消息id
     * @return long
     */
    public long updateMessageContent(int chatType, String msgId, String content) {
        String tableName = getChatMessageTable(chatType);
        SQLiteStatement statement = null;
        try {
            String updateSql = DBUtils.buildUpdateSql(tableName, new String[] {
                    "MSG_ID"
            }, "CONTENT").toString();
            statement = getDatabase().compileStatement(updateSql);
            statement.bindString(1, content);
            statement.bindString(2, msgId);
            return statement.executeUpdateDelete();
        } catch (Exception e) {
            XXLog.log_e("database", "updateMessageContent is failed:" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 根据消息id更新sendStatus
     *
     * @param msgId 消息id
     * @return long
     */
    public long updateMessageStatus(int chatType, String msgId, int status) {
        String tableName = getChatMessageTable(chatType);
        SQLiteStatement statement = null;
        try {
            String updateSql = DBUtils.buildUpdateSql(tableName, new String[] {
                    "MSG_ID"
            }, "SEND_STATUS").toString();
            statement = getDatabase().compileStatement(updateSql);
            statement.bindLong(1, status);
            statement.bindString(2, msgId);
            return statement.executeUpdateDelete();
        } catch (Exception e) {
            XXLog.log_e("database", "updateMessageStatus is failed:" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 清除聊天消息
     *
     * @param chatId 聊天对象id
     * @param chatType 聊天类型
     */
    public int clearChatMessageByChatId(String chatId, int chatType) {

        String tableName = getChatMessageTable(chatType);
        SQLiteStatement statement = null;
        try {
            String deleteSql = DBUtils.buildDeleteSql(tableName, "CHAT_ID").toString();
            statement = getDatabase().compileStatement(deleteSql);
            statement.bindString(1, chatId);
            return statement.executeUpdateDelete();
        } catch (Exception e) {
            XXLog.log_e("database", "clearChatMessageByTalker is failed:" + e.getMessage());
            return 0;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 获取聊天表
     * 
     * @param chatType 聊天类型
     * @return String
     */
    private String getChatMessageTable(int chatType) {
        String table = null;
        switch (chatType) {
            case MessageConfig.MessageCatalog.CHAT_SINGLE:
                table = ChatMsgEntityDao.TABLENAME;
                break;
            default:
                break;
        }
        return table;
    }

    /**
     * 为SQLiteStatement绑定值
     * 
     * @param statement statement
     * @param bean 聊天数据
     * @return SQLiteStatement
     */
    private SQLiteStatement bindValues(SQLiteStatement statement, ChatMessageBean bean) {
        if (statement == null || bean == null) {
            return null;
        }
        statement.clearBindings();
        if (!TextUtils.isEmpty(bean.getMsgId())) {
            statement.bindString(1, bean.getMsgId());
        }
        if (!TextUtils.isEmpty(bean.getAvimMsgId())) {
            statement.bindString(2, bean.getAvimMsgId());
        }
        if (!TextUtils.isEmpty(bean.getConversationId())) {
            statement.bindString(3, bean.getConversationId());
        }
        if (!TextUtils.isEmpty(bean.getContent())) {
            statement.bindString(4, bean.getContent());
        }
        statement.bindLong(5, bean.getMsgType());
        statement.bindLong(6, bean.getMessageTime());
        statement.bindLong(7, bean.getSendStatus());
        statement.bindLong(8, bean.getSender());
        statement.bindLong(9, bean.getRelationSourceId());
        if (!TextUtils.isEmpty(bean.getSendId())) {
            statement.bindString(10, bean.getSendId());
        }
        if (!TextUtils.isEmpty(bean.getChatId())) {
            statement.bindString(11, bean.getChatId());
        }
        return statement;
    }

    /**
     * cursor转换数据
     * 
     * @param cursor cursor
     * @param chatType 聊天类型
     * @return UserInfo
     */
    private ChatMessageBean cursor2Bean(Cursor cursor, int chatType) {
        if (cursor == null) {
            return null;
        }
        ChatMessageBean bean = new ChatMessageBean();
        bean.setChatType(chatType);
        bean.setMsgId(cursor.getString(1));
        bean.setAvimMsgId(cursor.getString(2));
        bean.setConversationId(cursor.getString(3));
        bean.setContent(cursor.getString(4));
        int msgType = cursor.getInt(5);
        bean.setMsgType(msgType);
        bean.setMessageTime(cursor.getLong(6));
        bean.setSendStatus(cursor.getInt(7));
        bean.setSender(cursor.getInt(8));
        long relationSourceId = cursor.getLong(9);
        bean.setRelationSourceId(relationSourceId);
        bean.setSendId(cursor.getString(10));
        bean.setChatId(cursor.getString(11));
        switch (msgType) {
            case MessageConfig.MessageType.MSG_VOICE:
                bean.setVoiceBean(
                        RelationResourceDBManager.getInstance().getMessageVoice(relationSourceId));
                break;
            case MessageConfig.MessageType.MSG_IMAGE:
                bean.setImageBean(
                        RelationResourceDBManager.getInstance().getMessageImage(relationSourceId));
                break;
            default:
                break;
        }
        MessageUtils.handlePacketContent(bean, bean.getContent());
        return bean;
    }
}
