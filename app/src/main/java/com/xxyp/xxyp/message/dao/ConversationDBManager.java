
package com.xxyp.xxyp.message.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.DBUtils;
import com.xxyp.xxyp.dao.BaseDao;
import com.xxyp.xxyp.dao.ConversationEntityDao;
import com.xxyp.xxyp.message.bean.ConversationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 会话列表数据管理类
 * Created by sunpengfei on 2017/8/24.
 * Person in charge : sunpengfei
 */
public class ConversationDBManager extends BaseDao {

    private static ConversationDBManager mInstance;

    public static ConversationDBManager getInstance() {
        if (mInstance == null) {
            synchronized (ConversationDBManager.class) {
                if (mInstance == null) {
                    mInstance = new ConversationDBManager();
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
     * 增加会话数据
     * @param bean  会话
     * @return long
     */
    public long addConversationBean(ConversationBean bean){
        if(bean == null){
            return -1;
        }
        SQLiteStatement statement = null;
        try {
            String insertSql = DBUtils.buildInsertSql(ConversationEntityDao.TABLENAME,
                    ConversationEntityDao.Properties.ChatId.columnName,
                    ConversationEntityDao.Properties.ChatType.columnName,
                    ConversationEntityDao.Properties.ConversationId.columnName,
                    ConversationEntityDao.Properties.ConversationAvatar.columnName,
                    ConversationEntityDao.Properties.ConversationName.columnName,
                    ConversationEntityDao.Properties.MsgDigest.columnName,
                    ConversationEntityDao.Properties.CreateTime.columnName,
                    ConversationEntityDao.Properties.UnReadCount.columnName).toString();
            statement = getDatabase().compileStatement(insertSql);
            return bindValues(statement, bean).executeInsert();
        } catch (Exception e) {
            XXLog.log_e("ConversationDBManager", "addConversationBean is failed" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 根据会话对象id查询之前是否存在
     *
     * @param chatId 会话id
     * @return boolean
     */
    public boolean isExistConversation(String chatId) {
        String tableName = ConversationEntityDao.TABLENAME;
        StringBuilder where = new StringBuilder(" where ");
        DBUtils.buildColumn(where, tableName, ConversationEntityDao.Properties.ChatId.columnName)
                .append("='").append(chatId).append("'");
        Cursor cursor = null;
        try {
            cursor = getDatabase()
                    .rawQuery(
                            DBUtils.buildSelectSql(tableName, where.toString(),
                                    ConversationEntityDao.Properties.ChatId.columnName).toString(),
                            null);
            if (cursor != null && cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            XXLog.log_e("ConversationDBManager", "getChatId is failed:" + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    /**
     * 获取会话列表
     *
     * @return MessageImageBean
     */
    public List<ConversationBean> getConversationList() {
        // 时间倒序
        StringBuilder where = new StringBuilder();
        where.append(" ORDER BY ");
        DBUtils.buildColumn(where, ConversationEntityDao.TABLENAME,
                ConversationEntityDao.Properties.CreateTime.columnName);
        where.append(" DESC ");
        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(
                    DBUtils.buildSelectSql(ConversationEntityDao.TABLENAME, where.toString(),
                            ConversationEntityDao.Properties.ChatId.columnName,
                            ConversationEntityDao.Properties.ChatType.columnName,
                            ConversationEntityDao.Properties.ConversationId.columnName,
                            ConversationEntityDao.Properties.ConversationAvatar.columnName,
                            ConversationEntityDao.Properties.ConversationName.columnName,
                            ConversationEntityDao.Properties.MsgDigest.columnName,
                            ConversationEntityDao.Properties.CreateTime.columnName,
                            ConversationEntityDao.Properties.UnReadCount.columnName).toString(),
                    null);
            if (cursor != null) {
                List<ConversationBean> beans = new ArrayList<>();
                while (cursor.moveToNext()) {
                    beans.add(cursor2Bean(cursor));
                }
                return beans;
            }
            return null;
        } catch (Exception e) {
            XXLog.log_e("ConversationDBManager", "getConversationList is failed:" + e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 获取会话的leanCloud的ConversationId
     *
     * @param chatId 会话id
     * @return String
     */
    public String getConversationIdByChatId(String chatId) {
        String tableName = ConversationEntityDao.TABLENAME;
        StringBuilder where = new StringBuilder(" where ");
        DBUtils.buildColumn(where, tableName, ConversationEntityDao.Properties.ChatId.columnName)
                .append("='").append(chatId).append("'");
        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(
                    DBUtils.buildSelectSql(tableName, where.toString(),
                            ConversationEntityDao.Properties.ConversationId.columnName).toString(),
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
            return null;
        } catch (Exception e) {
            XXLog.log_e("ConversationDBManager",
                    "getConversationIdByChatId is failed:" + e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 更新会话的LeanCloud的conversationId
     *
     * @param chatId 会话id
     * @param conversationId conversationId
     */
    public long updateConversationIdByChatId(String chatId, String conversationId) {
        if (TextUtils.isEmpty(chatId) || TextUtils.isEmpty(conversationId)) {
            return -1;
        }
        String updateSql = ConversationEntityDao.Properties.ChatId.columnName + "='"
                + chatId + "'";
        try {
            ContentValues values = new ContentValues();
            values.put(ConversationEntityDao.Properties.ConversationId.columnName,
                    conversationId);
            return getDatabase().update(ConversationEntityDao.TABLENAME, values, updateSql, null);
        } catch (Exception e) {
            XXLog.log_e("ConversationDBManager", "updateConversationIdByChatId is failed:" + e.getMessage());
        }
        return -1;
    }

    /**
     * 修改会话
     *
     * @param bean 会话
     */
    public long updateConversation(ConversationBean bean) {
        String updateSql = ConversationEntityDao.Properties.ChatId.columnName + "='"
                + bean.getChatId() + "'";
        try {
            ContentValues values = new ContentValues();
            if (!TextUtils.isEmpty(bean.getChatId())) {
                values.put(ConversationEntityDao.Properties.ChatId.columnName, bean.getChatId());
            }
            if (!TextUtils.isEmpty(bean.getConversationId())) {
                values.put(ConversationEntityDao.Properties.ConversationId.columnName,
                        bean.getConversationId());
            }
            if (bean.getChatType() != 0) {
                values.put(ConversationEntityDao.Properties.ChatType.columnName,
                        bean.getChatType());
            }
            if (!TextUtils.isEmpty(bean.getConversationAvatar())) {
                values.put(ConversationEntityDao.Properties.ConversationAvatar.columnName,
                        bean.getConversationAvatar());
            }
            if (!TextUtils.isEmpty(bean.getConversationName())) {
                values.put(ConversationEntityDao.Properties.ConversationName.columnName,
                        bean.getConversationName());
            }
            values.put(ConversationEntityDao.Properties.MsgDigest.columnName, bean.getMsgDigest());
            if (bean.getCreateTime() > 0) {
                values.put(ConversationEntityDao.Properties.CreateTime.columnName,
                        bean.getCreateTime());
            }
            return getDatabase().update(ConversationEntityDao.TABLENAME, values, updateSql, null);
        } catch (Exception e) {
            XXLog.log_e("ConversationDBManager", "updateConversation is failed:" + e.getMessage());
        }
        return -1;
    }

    /**
     * 修改会话未读数
     * @param chatId 会话id
     * @param unReadCount 未读数
     */
    public void updateUnReadCountByChatId(String chatId, String unReadCount) {
        String updateSql = "update " + ConversationEntityDao.TABLENAME + " set "
                + ConversationEntityDao.Properties.UnReadCount.columnName + "=" + ConversationEntityDao.Properties.UnReadCount.columnName
                + unReadCount + " where " + ConversationEntityDao.Properties.ChatId.columnName + "='" + chatId;
        updateSql = updateSql + "'";
        try {
            getDatabase().execSQL(updateSql);
        } catch (Exception e) {
            XXLog.log_e("ConversationDBManager", "updateUnReadCountByChatId is failed:" + e.getMessage());
        }
    }

    /**
     * 清除会话未读数
     * 
     * @param chatId 会话id
     */
    public void clearUnReadCountByChatId(String chatId) {
        String updateSql = "update " + ConversationEntityDao.TABLENAME + " set "
                + ConversationEntityDao.Properties.UnReadCount.columnName + "=0" + " where "
                + ConversationEntityDao.Properties.ChatId.columnName + "='" + chatId;
        updateSql = updateSql + "'";
        try {
            getDatabase().execSQL(updateSql);
        } catch (Exception e) {
            XXLog.log_e("ConversationDBManager",
                    "updateUnReadCountByChatId is failed:" + e.getMessage());
        }
    }

    /**
     * 获取会话的leanCloud的ConversationId
     *
     * @param chatId 会话id
     * @return String
     */
    public int getUnReadCountByChatId(String chatId) {
        String tableName = ConversationEntityDao.TABLENAME;
        StringBuilder where = new StringBuilder(" where ");
        DBUtils.buildColumn(where, tableName, ConversationEntityDao.Properties.ChatId.columnName)
                .append("='").append(chatId).append("'");
        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(
                    DBUtils.buildSelectSql(tableName, where.toString(),
                            ConversationEntityDao.Properties.UnReadCount.columnName).toString(),
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } catch (Exception e) {
            XXLog.log_e("ConversationDBManager",
                    "getUnReadCountByChatId is failed:" + e.getMessage());
            return 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 为SQLiteStatement绑定数据
     * @param statement statement
     * @param bean      数据
     * @return SQLiteStatement
     */
    private SQLiteStatement bindValues(SQLiteStatement statement, ConversationBean bean) {
        if (statement == null || bean == null) {
            return null;
        }
        statement.clearBindings();
        if (!TextUtils.isEmpty(bean.getChatId())) {
            statement.bindString(1, bean.getChatId());
        }
        statement.bindLong(2, bean.getChatType());
        if (!TextUtils.isEmpty(bean.getConversationId())) {
            statement.bindString(3, bean.getConversationId());
        }
        if (!TextUtils.isEmpty(bean.getConversationAvatar())) {
            statement.bindString(4, bean.getConversationAvatar());
        }
        if (!TextUtils.isEmpty(bean.getConversationName())) {
            statement.bindString(5, bean.getConversationName());
        }
        if (!TextUtils.isEmpty(bean.getMsgDigest())) {
            statement.bindString(6, bean.getMsgDigest());
        }
        statement.bindLong(7, bean.getCreateTime());
        statement.bindLong(8, bean.getUnReadCount());
        return statement;
    }

    /**
     * cursor转数据
     *
     * @param cursor cursor
     * @return ConversationBean
     */
    private ConversationBean cursor2Bean(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        ConversationBean conversationBean = new ConversationBean();
        conversationBean.setChatId(cursor.getString(0));
        conversationBean.setChatType(cursor.getInt(1));
        conversationBean.setConversationId(cursor.getString(2));
        conversationBean.setConversationAvatar(cursor.getString(3));
        conversationBean.setConversationName(cursor.getString(4));
        conversationBean.setMsgDigest(cursor.getString(5));
        conversationBean.setCreateTime(cursor.getLong(6));
        conversationBean.setUnReadCount(cursor.getInt(7));
        return conversationBean;
    }
}
