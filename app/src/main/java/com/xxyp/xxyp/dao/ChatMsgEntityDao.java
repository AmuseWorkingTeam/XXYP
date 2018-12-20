package com.xxyp.xxyp.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xxyp.xxyp.dao.entity.ChatMsgEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CHAT_MSG_ENTITY".
*/
public class ChatMsgEntityDao extends AbstractDao<ChatMsgEntity, Long> {

    public static final String TABLENAME = "CHAT_MSG_ENTITY";

    /**
     * Properties of entity ChatMsgEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MsgId = new Property(1, String.class, "msgId", false, "MSG_ID");
        public final static Property ChatId = new Property(2, String.class, "chatId", false, "CHAT_ID");
        public final static Property SendId = new Property(3, String.class, "sendId", false, "SEND_ID");
        public final static Property MsgType = new Property(4, Integer.class, "msgType", false, "MSG_TYPE");
        public final static Property Content = new Property(5, String.class, "content", false, "CONTENT");
        public final static Property CreateTime = new Property(6, Long.class, "createTime", false, "CREATE_TIME");
        public final static Property SendStatus = new Property(7, Integer.class, "sendStatus", false, "SEND_STATUS");
        public final static Property IsRead = new Property(8, Integer.class, "isRead", false, "IS_READ");
        public final static Property Sender = new Property(9, Integer.class, "sender", false, "SENDER");
        public final static Property RelationSourceId = new Property(10, Long.class, "relationSourceId", false, "RELATION_SOURCE_ID");
        public final static Property AvimMsgId = new Property(11, String.class, "avimMsgId", false, "AVIM_MSG_ID");
        public final static Property ConversationId = new Property(12, String.class, "conversationId", false, "CONVERSATION_ID");
    }


    public ChatMsgEntityDao(DaoConfig config) {
        super(config);
    }
    
    public ChatMsgEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHAT_MSG_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"MSG_ID\" TEXT," + // 1: msgId
                "\"CHAT_ID\" TEXT," + // 2: chatId
                "\"SEND_ID\" TEXT," + // 3: sendId
                "\"MSG_TYPE\" INTEGER," + // 4: msgType
                "\"CONTENT\" TEXT," + // 5: content
                "\"CREATE_TIME\" INTEGER," + // 6: createTime
                "\"SEND_STATUS\" INTEGER," + // 7: sendStatus
                "\"IS_READ\" INTEGER," + // 8: isRead
                "\"SENDER\" INTEGER," + // 9: sender
                "\"RELATION_SOURCE_ID\" INTEGER," + // 10: relationSourceId
                "\"AVIM_MSG_ID\" TEXT," + // 11: avimMsgId
                "\"CONVERSATION_ID\" TEXT);"); // 12: conversationId
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_CHAT_MSG_ENTITY_MSG_ID ON CHAT_MSG_ENTITY" +
                " (\"MSG_ID\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHAT_MSG_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ChatMsgEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String msgId = entity.getMsgId();
        if (msgId != null) {
            stmt.bindString(2, msgId);
        }
 
        String chatId = entity.getChatId();
        if (chatId != null) {
            stmt.bindString(3, chatId);
        }
 
        String sendId = entity.getSendId();
        if (sendId != null) {
            stmt.bindString(4, sendId);
        }
 
        Integer msgType = entity.getMsgType();
        if (msgType != null) {
            stmt.bindLong(5, msgType);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(6, content);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(7, createTime);
        }
 
        Integer sendStatus = entity.getSendStatus();
        if (sendStatus != null) {
            stmt.bindLong(8, sendStatus);
        }
 
        Integer isRead = entity.getIsRead();
        if (isRead != null) {
            stmt.bindLong(9, isRead);
        }
 
        Integer sender = entity.getSender();
        if (sender != null) {
            stmt.bindLong(10, sender);
        }
 
        Long relationSourceId = entity.getRelationSourceId();
        if (relationSourceId != null) {
            stmt.bindLong(11, relationSourceId);
        }
 
        String avimMsgId = entity.getAvimMsgId();
        if (avimMsgId != null) {
            stmt.bindString(12, avimMsgId);
        }
 
        String conversationId = entity.getConversationId();
        if (conversationId != null) {
            stmt.bindString(13, conversationId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ChatMsgEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String msgId = entity.getMsgId();
        if (msgId != null) {
            stmt.bindString(2, msgId);
        }
 
        String chatId = entity.getChatId();
        if (chatId != null) {
            stmt.bindString(3, chatId);
        }
 
        String sendId = entity.getSendId();
        if (sendId != null) {
            stmt.bindString(4, sendId);
        }
 
        Integer msgType = entity.getMsgType();
        if (msgType != null) {
            stmt.bindLong(5, msgType);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(6, content);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(7, createTime);
        }
 
        Integer sendStatus = entity.getSendStatus();
        if (sendStatus != null) {
            stmt.bindLong(8, sendStatus);
        }
 
        Integer isRead = entity.getIsRead();
        if (isRead != null) {
            stmt.bindLong(9, isRead);
        }
 
        Integer sender = entity.getSender();
        if (sender != null) {
            stmt.bindLong(10, sender);
        }
 
        Long relationSourceId = entity.getRelationSourceId();
        if (relationSourceId != null) {
            stmt.bindLong(11, relationSourceId);
        }
 
        String avimMsgId = entity.getAvimMsgId();
        if (avimMsgId != null) {
            stmt.bindString(12, avimMsgId);
        }
 
        String conversationId = entity.getConversationId();
        if (conversationId != null) {
            stmt.bindString(13, conversationId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ChatMsgEntity readEntity(Cursor cursor, int offset) {
        ChatMsgEntity entity = new ChatMsgEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // msgId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // chatId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // sendId
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // msgType
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // content
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // createTime
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // sendStatus
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // isRead
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // sender
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10), // relationSourceId
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // avimMsgId
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // conversationId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ChatMsgEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMsgId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setChatId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSendId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMsgType(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setContent(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCreateTime(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setSendStatus(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setIsRead(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setSender(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setRelationSourceId(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
        entity.setAvimMsgId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setConversationId(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ChatMsgEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ChatMsgEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ChatMsgEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
