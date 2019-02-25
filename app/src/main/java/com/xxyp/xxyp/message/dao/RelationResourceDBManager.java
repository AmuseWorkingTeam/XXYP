
package com.xxyp.xxyp.message.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.DBUtils;
import com.xxyp.xxyp.dao.BaseDao;
import com.xxyp.xxyp.dao.ChatMsgEntityDao;
import com.xxyp.xxyp.dao.MessageImageEntityDao;
import com.xxyp.xxyp.dao.MessageShotEntityDao;
import com.xxyp.xxyp.dao.MessageVoiceEntityDao;
import com.xxyp.xxyp.message.bean.MessageImageBean;
import com.xxyp.xxyp.message.bean.MessageShotBean;
import com.xxyp.xxyp.message.bean.MessageVoiceBean;
import com.xxyp.xxyp.message.utils.MessageConfig;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Description : 聊天资源数据 Created by sunpengfei on 2017/8/22. Person in charge :
 * sunpengfei
 */
public class RelationResourceDBManager extends BaseDao {

    private static RelationResourceDBManager mInstance;

    public static RelationResourceDBManager getInstance() {
        if (mInstance == null) {
            synchronized (RelationResourceDBManager.class) {
                if (mInstance == null) {
                    mInstance = new RelationResourceDBManager();
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
     * 添加图片
     * 
     * @param imageBean 图片
     * @return long
     */
    public long addMessageImage(MessageImageBean imageBean) {
        if (imageBean == null) {
            return -1;
        }
        SQLiteStatement statement = null;
        try {
            String insertSql = DBUtils.buildInsertSql(MessageImageEntityDao.TABLENAME,
                    MessageImageEntityDao.Properties.ImageUrl.columnName,
                    MessageImageEntityDao.Properties.LocalImagePath.columnName,
                    MessageImageEntityDao.Properties.BigImagePath.columnName,
                    MessageImageEntityDao.Properties.Format.columnName,
                    MessageImageEntityDao.Properties.ImageWidth.columnName,
                    MessageImageEntityDao.Properties.ImageHeight.columnName,
                    MessageImageEntityDao.Properties.Digest.columnName,
                    MessageImageEntityDao.Properties.BelongTo.columnName,
                    MessageImageEntityDao.Properties.Status.columnName,
                    MessageImageEntityDao.Properties.LastModifyTime.columnName,
                    MessageImageEntityDao.Properties.ImgId.columnName).toString();
            statement = getDatabase().compileStatement(insertSql);
            return bindImageValues(statement, imageBean).executeInsert();
        } catch (Exception e) {
            XXLog.log_e("RelationResourceDBManager", "addMessageImage is failed" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 更新图片
     * 
     * @param imageBean 图片
     * @return long
     */
    public long updateMessageImage(MessageImageBean imageBean) {
        if (imageBean == null) {
            return -1;
        }
        SQLiteStatement statement = null;
        try {
            String updateSql = DBUtils
                    .buildUpdateSql(MessageImageEntityDao.TABLENAME, new String[] {
                            MessageImageEntityDao.Properties.ImgId.columnName
                    }, MessageImageEntityDao.Properties.ImageUrl.columnName,
                            MessageImageEntityDao.Properties.LocalImagePath.columnName,
                            MessageImageEntityDao.Properties.BigImagePath.columnName,
                            MessageImageEntityDao.Properties.Format.columnName,
                            MessageImageEntityDao.Properties.ImageWidth.columnName,
                            MessageImageEntityDao.Properties.ImageHeight.columnName,
                            MessageImageEntityDao.Properties.Digest.columnName,
                            MessageImageEntityDao.Properties.BelongTo.columnName,
                            MessageImageEntityDao.Properties.Status.columnName,
                            MessageImageEntityDao.Properties.LastModifyTime.columnName)
                    .toString();
            statement = getDatabase().compileStatement(updateSql);
            return bindImageValues(statement, imageBean).executeInsert();
        } catch (Exception e) {
            XXLog.log_e("RelationResourceDBManager",
                    "updateMessageImage is failed" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 根据belongTo获取所有图片
     * 
     * @param belongTo 所属会话id
     * @return List<MessageImageBean>
     */
    public List<MessageImageBean> getMessageImages(String belongTo) {
        String where = " where " + MessageImageEntityDao.Properties.BelongTo.columnName + "='"
                + belongTo;
        // relateTable 图片关联的聊天表 单聊表或群聊表 orderBy 单聊表或群聊表的时间排序
        String relateTable = " left join ", orderByTable = " ORDER BY ";
        where = where + "' AND " + ChatMsgEntityDao.Properties.MsgType.columnName + " = "
                + MessageConfig.MessageType.MSG_IMAGE;
        relateTable = relateTable + ChatMsgEntityDao.TABLENAME + " on "
                + ChatMsgEntityDao.Properties.RelationSourceId.columnName + "=";
        orderByTable = orderByTable + ChatMsgEntityDao.Properties.CreateTime.columnName;
        relateTable = relateTable + DBUtils.buildColumn(new StringBuilder(),
                MessageImageEntityDao.TABLENAME, MessageImageEntityDao.Properties.ImgId.columnName);
        StringBuilder selectSql = DBUtils.buildSelectSql(MessageImageEntityDao.TABLENAME, null,
                MessageImageEntityDao.Properties.ImageUrl.columnName,
                MessageImageEntityDao.Properties.LocalImagePath.columnName,
                MessageImageEntityDao.Properties.BigImagePath.columnName,
                MessageImageEntityDao.Properties.Format.columnName,
                MessageImageEntityDao.Properties.ImageWidth.columnName,
                MessageImageEntityDao.Properties.ImageHeight.columnName,
                MessageImageEntityDao.Properties.Digest.columnName,
                MessageImageEntityDao.Properties.BelongTo.columnName,
                DBUtils.buildColumn(new StringBuilder(), MessageImageEntityDao.TABLENAME,
                        MessageImageEntityDao.Properties.Status.columnName).toString(),
                DBUtils.buildColumn(new StringBuilder(), MessageImageEntityDao.TABLENAME,
                        MessageImageEntityDao.Properties.LastModifyTime.columnName).toString(),
                DBUtils.buildColumn(new StringBuilder(), MessageImageEntityDao.TABLENAME,
                        MessageImageEntityDao.Properties.ImgId.columnName).toString());
        selectSql.append(relateTable).append(where).append(orderByTable);
        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(selectSql.toString(), null);
            if (cursor != null) {
                List<MessageImageBean> result = new ArrayList<>();
                while (cursor.moveToNext()) {
                    result.add(cursor2Image(cursor));
                }
                return result;
            }
        } catch (Exception e) {
            XXLog.log_e("database", "getImgInfoByBelongTo is failed:" + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 更新语音
     * 
     * @param voiceBean 语音
     * @return long
     */
    public long updateMessageVoice(MessageVoiceBean voiceBean) {
        if (voiceBean == null) {
            return -1;
        }
        SQLiteStatement statement = null;
        try {
            String updateSql = DBUtils
                    .buildUpdateSql(MessageVoiceEntityDao.TABLENAME, new String[] {
                            MessageVoiceEntityDao.Properties.VoiceId.columnName
                    }, MessageVoiceEntityDao.Properties.VoiceUrl.columnName,
                            MessageVoiceEntityDao.Properties.VoiceLocalPath.columnName,
                            MessageVoiceEntityDao.Properties.VoiceLen.columnName,
                            MessageVoiceEntityDao.Properties.BelongTo.columnName,
                            MessageVoiceEntityDao.Properties.Status.columnName,
                            MessageVoiceEntityDao.Properties.LastModifyTime.columnName)
                    .toString();
            statement = getDatabase().compileStatement(updateSql);
            return bindVoiceValues(statement, voiceBean).executeInsert();
        } catch (Exception e) {
            XXLog.log_e("RelationResourceDBManager",
                    "updateMessageVoice is failed" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 更新语音状态
     *
     * @param status 0未读，1已读
     * @param voiceId 语音id
     * @return long
     */
    public long updateVoiceMessageStatus(int status, long voiceId) {
        String updateSql = "update " + MessageVoiceEntityDao.TABLENAME + " set "
                + MessageVoiceEntityDao.Properties.Status.columnName + "=" + status + " where "
                + MessageVoiceEntityDao.Properties.VoiceId.columnName + "=" + voiceId;
        try {
            getDatabase().execSQL(updateSql);
            return 1;
        } catch (Exception e) {
            XXLog.log_e(TAG, "updateVoiceMessageStatus is failed:" + e);
            return 0;
        }
    }

    /**
     * 添加语音
     * 
     * @param voiceBean 语音
     * @return long
     */
    public long addMessageVoice(MessageVoiceBean voiceBean) {
        if (voiceBean == null) {
            return -1;
        }
        SQLiteStatement statement = null;
        try {
            String insertSql = DBUtils.buildInsertSql(MessageVoiceEntityDao.TABLENAME,
                    MessageVoiceEntityDao.Properties.VoiceUrl.columnName,
                    MessageVoiceEntityDao.Properties.VoiceLocalPath.columnName,
                    MessageVoiceEntityDao.Properties.VoiceLen.columnName,
                    MessageVoiceEntityDao.Properties.BelongTo.columnName,
                    MessageVoiceEntityDao.Properties.Status.columnName,
                    MessageVoiceEntityDao.Properties.LastModifyTime.columnName,
                    MessageVoiceEntityDao.Properties.VoiceId.columnName).toString();
            statement = getDatabase().compileStatement(insertSql);
            return bindVoiceValues(statement, voiceBean).executeInsert();
        } catch (Exception e) {
            XXLog.log_e("RelationResourceDBManager", "addMessageVoice is failed" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 获取图片数据
     * 
     * @param imgId 图片id
     * @return MessageImageBean
     */
    public MessageImageBean getMessageImage(long imgId) {
        if (imgId < 0) {
            return null;
        }
        StringBuilder where = new StringBuilder(" where ");
        DBUtils.buildColumn(where, MessageImageEntityDao.TABLENAME,
                MessageImageEntityDao.Properties.ImgId.columnName);
        where.append("=").append(imgId);
        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(DBUtils.buildSelectSql(MessageImageEntityDao.TABLENAME,
                    where.toString(), MessageImageEntityDao.Properties.ImageUrl.columnName,
                    MessageImageEntityDao.Properties.LocalImagePath.columnName,
                    MessageImageEntityDao.Properties.BigImagePath.columnName,
                    MessageImageEntityDao.Properties.Format.columnName,
                    MessageImageEntityDao.Properties.ImageWidth.columnName,
                    MessageImageEntityDao.Properties.ImageHeight.columnName,
                    MessageImageEntityDao.Properties.Digest.columnName,
                    MessageImageEntityDao.Properties.BelongTo.columnName,
                    MessageImageEntityDao.Properties.Status.columnName,
                    MessageImageEntityDao.Properties.LastModifyTime.columnName,
                    MessageImageEntityDao.Properties.ImgId.columnName).toString(), null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor2Image(cursor);
            }
            return null;
        } catch (Exception e) {
            XXLog.log_e("RelationResourceDBManager", "getMessageImage is failed:" + e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 获取语音数据
     * 
     * @param voiceId 语音id
     * @return MessageVoiceBean
     */
    public MessageVoiceBean getMessageVoice(long voiceId) {
        if (voiceId < 0) {
            return null;
        }
        StringBuilder where = new StringBuilder(" where ");
        DBUtils.buildColumn(where, MessageVoiceEntityDao.TABLENAME,
                MessageVoiceEntityDao.Properties.VoiceId.columnName);
        where.append("=").append(voiceId);
        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(DBUtils.buildSelectSql(MessageVoiceEntityDao.TABLENAME,
                    where.toString(), MessageVoiceEntityDao.Properties.VoiceUrl.columnName,
                    MessageVoiceEntityDao.Properties.VoiceLocalPath.columnName,
                    MessageVoiceEntityDao.Properties.VoiceLen.columnName,
                    MessageVoiceEntityDao.Properties.BelongTo.columnName,
                    MessageVoiceEntityDao.Properties.Status.columnName,
                    MessageVoiceEntityDao.Properties.LastModifyTime.columnName,
                    MessageVoiceEntityDao.Properties.VoiceId.columnName).toString(), null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor2Voice(cursor);
            }
            return null;
        } catch (Exception e) {
            XXLog.log_e("RelationResourceDBManager", "getMessageVoice is failed:" + e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 移除会话下图片信息
     *
     * @param belongTo 所属会话
     */
    public void removeMessageImageByBelongTo(String belongTo) {
        String deleteSql;
        SQLiteStatement statement = null;
        try {
            deleteSql = DBUtils.buildDeleteSql(MessageImageEntityDao.TABLENAME,
                    MessageImageEntityDao.Properties.BelongTo.columnName).toString();
            statement = getDatabase().compileStatement(deleteSql);
            statement.bindString(1, belongTo);
            statement.executeUpdateDelete();
        } catch (Exception e) {
            XXLog.log_e(TAG, "MessageImageEntityDao is failed:" + e);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
    /**
     * 移除会话下图片信息
     *
     * @param belongTo 所属会话
     */
    public void removeMessageImage(long imgId, String belongTo) {
        String deleteSql;
        SQLiteStatement statement = null;
        try {
            deleteSql = DBUtils.buildDeleteSql(MessageImageEntityDao.TABLENAME,
                    MessageImageEntityDao.Properties.BelongTo.columnName,
                    MessageImageEntityDao.Properties.ImgId.columnName).toString();
            statement = getDatabase().compileStatement(deleteSql);
            statement.bindString(1, belongTo);
            statement.bindLong(2, imgId);
            statement.executeUpdateDelete();
        } catch (Exception e) {
            XXLog.log_e(TAG, "MessageImageEntityDao is failed:" + e);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 移除会话下语音信息
     *
     * @param belongTo 所属会话
     */
    public void removeMessageVoiceByBelongTo(String belongTo) {
        String deleteSql;
        SQLiteStatement statement = null;
        try {
            deleteSql = DBUtils.buildDeleteSql(MessageVoiceEntityDao.TABLENAME,
                    MessageVoiceEntityDao.Properties.BelongTo.columnName).toString();
            statement = getDatabase().compileStatement(deleteSql);
            statement.bindString(1, belongTo);
            statement.executeUpdateDelete();
        } catch (Exception e) {
            XXLog.log_e(TAG, "removeMessageVoiceByBelongTo is failed:" + e);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 移除会话下语音信息
     *
     * @param belongTo 所属会话
     */
    public void removeMessageVoice(long voiceId, String belongTo) {
        String deleteSql;
        SQLiteStatement statement = null;
        try {
            deleteSql = DBUtils.buildDeleteSql(MessageVoiceEntityDao.TABLENAME,
                    MessageVoiceEntityDao.Properties.BelongTo.columnName,
                    MessageVoiceEntityDao.Properties.VoiceId.columnName).toString();
            statement = getDatabase().compileStatement(deleteSql);
            statement.bindString(1, belongTo);
            statement.bindLong(2, voiceId);
            statement.executeUpdateDelete();
        } catch (Exception e) {
            XXLog.log_e(TAG, "removeMessageVoiceByBelongTo is failed:" + e);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 获取约拍数据
     *
     * @param shotId 约拍idid
     * @return MessageShotBean
     */
    public MessageShotBean getMessageShot(long shotId) {
        if (shotId < 0) {
            return null;
        }
        StringBuilder where = new StringBuilder(" where ");
        DBUtils.buildColumn(where, MessageShotEntityDao.TABLENAME,
                MessageShotEntityDao.Properties.ShotId.columnName);
        where.append("=").append(shotId);
        Cursor cursor = null;
        try {
            cursor = getDatabase().rawQuery(
                    DBUtils.buildSelectSql(MessageShotEntityDao.TABLENAME, where.toString(),
                            MessageShotEntityDao.Properties.UserId.columnName,
                            MessageShotEntityDao.Properties.DatingShotAddress.columnName,
                            MessageShotEntityDao.Properties.DatingShotTime.columnName,
                            MessageShotEntityDao.Properties.Purpose.columnName,
                            MessageShotEntityDao.Properties.PaymentMethod.columnName,
                            MessageShotEntityDao.Properties.DatingShotIntroduction.columnName,
                            MessageShotEntityDao.Properties.Description.columnName,
                            MessageShotEntityDao.Properties.DatingUserId.columnName,
                            MessageShotEntityDao.Properties.Status.columnName,
                            MessageShotEntityDao.Properties.ShotId.columnName,
                            MessageShotEntityDao.Properties.BelongTo.columnName,
                            MessageShotEntityDao.Properties.DatingShotImage.columnName,
                            MessageShotEntityDao.Properties.DatingShotId.columnName,
                            MessageShotEntityDao.Properties.ShotId.columnName).toString(),
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor2Shot(cursor);
            }
            return null;
        } catch (Exception e) {
            XXLog.log_e("RelationResourceDBManager", "getMessageImage is failed:" + e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 添加约拍
     *
     * @param shotBean 约拍
     * @return long
     */
    public long addMessageShot(MessageShotBean shotBean) {
        if (shotBean == null) {
            return -1;
        }
        SQLiteStatement statement = null;
        try {
            String insertSql = DBUtils.buildInsertSql(MessageShotEntityDao.TABLENAME,
                    MessageShotEntityDao.Properties.UserId.columnName,
                    MessageShotEntityDao.Properties.DatingShotAddress.columnName,
                    MessageShotEntityDao.Properties.DatingShotTime.columnName,
                    MessageShotEntityDao.Properties.Purpose.columnName,
                    MessageShotEntityDao.Properties.PaymentMethod.columnName,
                    MessageShotEntityDao.Properties.DatingShotIntroduction.columnName,
                    MessageShotEntityDao.Properties.Description.columnName,
                    MessageShotEntityDao.Properties.DatingUserId.columnName,
                    MessageShotEntityDao.Properties.Status.columnName,
                    MessageShotEntityDao.Properties.ShotId.columnName,
                    MessageShotEntityDao.Properties.BelongTo.columnName,
                    MessageShotEntityDao.Properties.DatingShotImage.columnName,
                    MessageShotEntityDao.Properties.DatingShotId.columnName,
                    MessageShotEntityDao.Properties.ShotId.columnName).toString();
            statement = getDatabase().compileStatement(insertSql);
            return bindShotValues(statement, shotBean).executeInsert();
        } catch (Exception e) {
            XXLog.log_e("RelationResourceDBManager", "addMessageShot is failed" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 更新约拍
     *
     * @param shotBean 约拍
     * @return long
     */
    public long updateMessageShot(MessageShotBean shotBean) {
        if (shotBean == null) {
            return -1;
        }
        SQLiteStatement statement = null;
        try {
            String updateSql = DBUtils.buildUpdateSql(MessageShotEntityDao.TABLENAME, new String[] {
                    MessageShotEntityDao.Properties.ShotId.columnName
            }, MessageShotEntityDao.Properties.UserId.columnName,
                    MessageShotEntityDao.Properties.DatingShotAddress.columnName,
                    MessageShotEntityDao.Properties.DatingShotTime.columnName,
                    MessageShotEntityDao.Properties.Purpose.columnName,
                    MessageShotEntityDao.Properties.PaymentMethod.columnName,
                    MessageShotEntityDao.Properties.DatingShotIntroduction.columnName,
                    MessageShotEntityDao.Properties.Description.columnName,
                    MessageShotEntityDao.Properties.DatingUserId.columnName,
                    MessageShotEntityDao.Properties.Status.columnName,
                    MessageShotEntityDao.Properties.BelongTo.columnName,
                    MessageShotEntityDao.Properties.DatingShotImage.columnName,
                    MessageShotEntityDao.Properties.DatingShotId.columnName,
                    MessageShotEntityDao.Properties.ShotId.columnName).toString();
            statement = getDatabase().compileStatement(updateSql);
            return bindShotValues(statement, shotBean).executeInsert();
        } catch (Exception e) {
            XXLog.log_e("RelationResourceDBManager",
                    "updateMessageShot is failed" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 移除会话下约拍信息
     *
     * @param belongTo 所属会话
     */
    public void removeMessageShot(long shotId, String belongTo) {
        String deleteSql;
        SQLiteStatement statement = null;
        try {
            deleteSql = DBUtils.buildDeleteSql(MessageShotEntityDao.TABLENAME,
                    MessageShotEntityDao.Properties.BelongTo.columnName,
                    MessageShotEntityDao.Properties.ShotId.columnName).toString();
            statement = getDatabase().compileStatement(deleteSql);
            statement.bindString(1, belongTo);
            statement.bindLong(2, shotId);
            statement.executeUpdateDelete();
        } catch (Exception e) {
            XXLog.log_e(TAG, "removeMessageShotByBelongTo is failed:" + e);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 为SQLiteStatement绑定语音数据
     * 
     * @param statement statement
     * @param voiceBean 语音数据
     * @return SQLiteStatement
     */
    private SQLiteStatement bindVoiceValues(SQLiteStatement statement, MessageVoiceBean voiceBean) {
        if (statement == null || voiceBean == null) {
            return null;
        }
        statement.clearBindings();
        if (!TextUtils.isEmpty(voiceBean.getVoiceUrl())) {
            statement.bindString(1, voiceBean.getVoiceUrl());
        }
        if (!TextUtils.isEmpty(voiceBean.getVoiceLocalPath())) {
            statement.bindString(2, voiceBean.getVoiceLocalPath());
        }
        statement.bindLong(3, voiceBean.getVoiceLen());
        if (!TextUtils.isEmpty(voiceBean.getBelongTo())) {
            statement.bindString(4, voiceBean.getBelongTo());
        }
        statement.bindLong(5, voiceBean.getStatus());
        statement.bindLong(6, voiceBean.getLastModifyTime());
        if (voiceBean.getVoiceId() != -1L) {
            statement.bindLong(7, voiceBean.getVoiceId());
        }
        return statement;
    }

    /**
     * cursor转语音数据
     *
     * @param cursor cursor
     * @return MessageVoiceBean
     */
    private MessageVoiceBean cursor2Voice(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        MessageVoiceBean voiceBean = new MessageVoiceBean();
        voiceBean.setVoiceUrl(cursor.getString(0));
        voiceBean.setVoiceLocalPath(cursor.getString(1));
        voiceBean.setVoiceLen(cursor.getInt(2));
        voiceBean.setBelongTo(cursor.getString(3));
        voiceBean.setStatus(cursor.getInt(4));
        voiceBean.setLastModifyTime(cursor.getLong(5));
        voiceBean.setVoiceId(cursor.getLong(6));
        return voiceBean;
    }

    /**
     * 为SQLiteStatement绑定图片数据
     * 
     * @param statement statement
     * @param imageBean 图片数据
     * @return SQLiteStatement
     */
    private SQLiteStatement bindImageValues(SQLiteStatement statement, MessageImageBean imageBean) {
        if (statement == null || imageBean == null) {
            return null;
        }
        statement.clearBindings();
        if (!TextUtils.isEmpty(imageBean.getImageUrl())) {
            statement.bindString(1, imageBean.getImageUrl());
        }
        if (!TextUtils.isEmpty(imageBean.getLocalImagePath())) {
            statement.bindString(2, imageBean.getLocalImagePath());
        }
        if (!TextUtils.isEmpty(imageBean.getBigImagePath())) {
            statement.bindString(3, imageBean.getBigImagePath());
        }
        if (!TextUtils.isEmpty(imageBean.getFormat())) {
            statement.bindString(4, imageBean.getFormat());
        }
        statement.bindLong(5, imageBean.getImageWidth());
        statement.bindLong(6, imageBean.getImageHeight());
        if (!TextUtils.isEmpty(imageBean.getDigest())) {
            statement.bindString(7, imageBean.getDigest());
        }
        if (!TextUtils.isEmpty(imageBean.getBelongTo())) {
            statement.bindString(8, imageBean.getBelongTo());
        }
        statement.bindLong(9, imageBean.getStatus());
        statement.bindLong(10, imageBean.getLastModifyTime());
        if (imageBean.getImgId() != -1) {
            statement.bindLong(11, imageBean.getImgId());
        }
        return statement;
    }

    /**
     * cursor转图片数据
     * 
     * @param cursor cursor
     * @return MessageImageBean
     */
    private MessageImageBean cursor2Image(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        MessageImageBean messageImageBean = new MessageImageBean();
        messageImageBean.setImageUrl(cursor.getString(0));
        messageImageBean.setLocalImagePath(cursor.getString(1));
        messageImageBean.setBigImagePath(cursor.getString(2));
        messageImageBean.setFormat(cursor.getString(3));
        messageImageBean.setImageWidth(cursor.getInt(4));
        messageImageBean.setImageHeight(cursor.getInt(5));
        messageImageBean.setDigest(cursor.getString(6));
        messageImageBean.setBelongTo(cursor.getString(7));
        messageImageBean.setStatus(cursor.getInt(8));
        messageImageBean.setLastModifyTime(cursor.getLong(9));
        messageImageBean.setImgId(cursor.getLong(10));
        return messageImageBean;
    }

    /**
     * 为SQLiteStatement绑定约拍数据
     * 
     * @param statement statement
     * @param shotBean 约拍数据
     * @return SQLiteStatement
     */
    private SQLiteStatement bindShotValues(SQLiteStatement statement, MessageShotBean shotBean) {
        if (statement == null || shotBean == null) {
            return null;
        }
        statement.clearBindings();
        if (!TextUtils.isEmpty(shotBean.getUserId())) {
            statement.bindString(1, shotBean.getUserId());
        }
        if (!TextUtils.isEmpty(shotBean.getDatingShotAddress())) {
            statement.bindString(2, shotBean.getDatingShotAddress());
        }
        statement.bindLong(3, shotBean.getDatingShotTime());
        if (!TextUtils.isEmpty(shotBean.getPurpose())) {
            statement.bindString(4, shotBean.getPurpose());
        }
        if (!TextUtils.isEmpty(shotBean.getPaymentMethod())) {
            statement.bindString(5, shotBean.getPaymentMethod());
        }
        if (!TextUtils.isEmpty(shotBean.getDatingShotIntroduction())) {
            statement.bindString(6, shotBean.getDatingShotIntroduction());
        }
        if (!TextUtils.isEmpty(shotBean.getDescription())) {
            statement.bindString(7, shotBean.getDescription());
        }
        if (!TextUtils.isEmpty(shotBean.getDatingUserId())) {
            statement.bindString(8, shotBean.getDatingUserId());
        }
        statement.bindLong(9, shotBean.getStatus());
        if (!TextUtils.isEmpty(shotBean.getBelongTo())) {
            statement.bindString(10, shotBean.getBelongTo());
        }
        if (!TextUtils.isEmpty(shotBean.getDatingShotImage())) {
            statement.bindString(11, shotBean.getDatingShotImage());
        }
        if (shotBean.getDatingShotId() != -1) {
            statement.bindLong(12, shotBean.getDatingShotId());
        }
        if (shotBean.getShotId() != -1) {
            statement.bindLong(13, shotBean.getShotId());
        }
        return statement;
    }

    /**
     * cursor转图片数据
     *
     * @param cursor cursor
     * @return MessageImageBean
     */
    private MessageShotBean cursor2Shot(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        MessageShotBean shotBean = new MessageShotBean();
        shotBean.setUserId(cursor.getString(0));
        shotBean.setDatingShotAddress(cursor.getString(1));
        shotBean.setDatingShotTime(cursor.getLong(2));
        shotBean.setPurpose(cursor.getString(3));
        shotBean.setPaymentMethod(cursor.getString(4));
        shotBean.setDatingShotIntroduction(cursor.getString(5));
        shotBean.setDescription(cursor.getString(6));
        shotBean.setDatingUserId(cursor.getString(7));
        shotBean.setStatus(cursor.getInt(8));
        shotBean.setBelongTo(cursor.getString(9));
        shotBean.setDatingShotImage(cursor.getString(10));
        shotBean.setDatingShotId(cursor.getLong(11));
        shotBean.setShotId(cursor.getLong(12));
        return shotBean;
    }
}
