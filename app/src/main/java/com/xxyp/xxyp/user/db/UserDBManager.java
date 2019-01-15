
package com.xxyp.xxyp.user.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.DBUtils;
import com.xxyp.xxyp.dao.BaseDao;
import com.xxyp.xxyp.dao.UserEntityDao;
import com.xxyp.xxyp.dao.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : user数据管理类 Created by sunpengfei on 2017/8/4. Person in charge :
 * sunpengfei
 */
public class UserDBManager extends BaseDao {

    private static UserDBManager mInstance;

    public static UserDBManager getInstance() {
        if (mInstance == null) {
            synchronized (UserDBManager.class) {
                if (mInstance == null) {
                    mInstance = new UserDBManager();
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
     * 插入用户数据到数据库
     *
     * @param db       db
     * @param userInfo 用户信息
     * @return long
     */
    public long addUserInfo(SQLiteDatabase db, UserInfo userInfo) {
        if (userInfo == null) {
            return -1;
        }
        SQLiteStatement statement = null;
        try {
            if (db == null) {
                db = getDatabase();
            }
            String insertSql = DBUtils.buildInsertSql(UserEntityDao.TABLENAME,
                    UserEntityDao.Properties.Status.columnName,
                    UserEntityDao.Properties.UserId.columnName,
                    UserEntityDao.Properties.UserIdentity.columnName,
                    UserEntityDao.Properties.UserSource.columnName,
                    UserEntityDao.Properties.UserSourceId.columnName,
                    UserEntityDao.Properties.UserName.columnName,
                    UserEntityDao.Properties.UserImage.columnName,
                    UserEntityDao.Properties.UserIntroduction.columnName,
                    UserEntityDao.Properties.Email.columnName,
                    UserEntityDao.Properties.Mobile.columnName,
                    UserEntityDao.Properties.Gender.columnName).toString();
            statement = db.compileStatement(insertSql);
            return bindValues(statement, userInfo).executeInsert();
        } catch (Exception e) {
            XXLog.log_e("UserDBManager", "addUserInfo is failed" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 插入用户数据到数据库
     *
     * @param userInfos 用户信息列表
     */
    public void addOrUpdateUserInfos(List<UserInfo> userInfos) {
        if (userInfos == null || userInfos.size() == 0) {
            return;
        }
        SQLiteDatabase db = getDatabase();
        try {
            db.beginTransaction();
            for (UserInfo userInfo : userInfos) {
                if (isExistUserInfo(userInfo.getUserId())) {
                    updateUserInfo(userInfo);
                } else {
                    addUserInfo(db, userInfo);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            XXLog.log_e("UserDBManager", "addOrUpdateUserInfos is failed" + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public UserInfo getUserById(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        StringBuilder where = new StringBuilder(" where ");
        DBUtils.buildColumn(where, UserEntityDao.TABLENAME,
                UserEntityDao.Properties.UserId.columnName);
        where.append(" = '").append(userId).append("'");
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getDatabase();
            String selectSql = DBUtils.buildSelectSql(UserEntityDao.TABLENAME, where.toString(),
                    UserEntityDao.Properties.Status.columnName,
                    UserEntityDao.Properties.UserId.columnName,
                    UserEntityDao.Properties.UserIdentity.columnName,
                    UserEntityDao.Properties.UserSource.columnName,
                    UserEntityDao.Properties.UserSourceId.columnName,
                    UserEntityDao.Properties.UserName.columnName,
                    UserEntityDao.Properties.UserImage.columnName,
                    UserEntityDao.Properties.UserIntroduction.columnName,
                    UserEntityDao.Properties.Email.columnName,
                    UserEntityDao.Properties.Mobile.columnName,
                    UserEntityDao.Properties.Gender.columnName).toString();
            cursor = db.rawQuery(selectSql, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor2Bean(cursor);
            }
        } catch (Exception e) {
            XXLog.log_e("UserDBManager",
                    "getUserById is failed" + e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 批量获取本地用户数据
     *
     * @param userIds 用户id集合
     * @return List<UserInfo>
     */
    public List<UserInfo> getUsersByIds(List<String> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return null;
        }
        StringBuilder where = new StringBuilder(" where ");
        DBUtils.buildColumn(where, UserEntityDao.TABLENAME,
                UserEntityDao.Properties.UserId.columnName);
        where.append(" in (").append(DBUtils.buildStringWithList(userIds)).append(")");
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getDatabase();
            String selectSql = DBUtils.buildSelectSql(UserEntityDao.TABLENAME, where.toString(),
                    UserEntityDao.Properties.Status.columnName,
                    UserEntityDao.Properties.UserId.columnName,
                    UserEntityDao.Properties.UserIdentity.columnName,
                    UserEntityDao.Properties.UserSource.columnName,
                    UserEntityDao.Properties.UserSourceId.columnName,
                    UserEntityDao.Properties.UserName.columnName,
                    UserEntityDao.Properties.UserImage.columnName,
                    UserEntityDao.Properties.UserIntroduction.columnName,
                    UserEntityDao.Properties.Email.columnName,
                    UserEntityDao.Properties.Mobile.columnName,
                    UserEntityDao.Properties.Gender.columnName).toString();
            cursor = db.rawQuery(selectSql, null);
            if (cursor != null) {
                List<UserInfo> userInfos = new ArrayList<>();
                while (cursor.moveToNext()) {
                    userInfos.add(cursor2Bean(cursor));
                }
                return userInfos;
            }
        } catch (Exception e) {
            XXLog.log_e("UserDBManager",
                    "getUsersByIds is failed" + e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     */
    public long updateUserInfo(UserInfo userInfo) {
        String updateSql = UserEntityDao.Properties.UserId.columnName + "='" + userInfo.getUserId()
                + "'";
        try {
            ContentValues values = new ContentValues();
            values.put(UserEntityDao.Properties.Status.columnName, userInfo.getStatus());
            if (!TextUtils.isEmpty(userInfo.getUserId())) {
                values.put(UserEntityDao.Properties.UserId.columnName, userInfo.getUserId());
            }
            values.put(UserEntityDao.Properties.UserIdentity.columnName,
                    userInfo.getUserIdentity());
            values.put(UserEntityDao.Properties.UserSource.columnName, userInfo.getUserSource());
            if (!TextUtils.isEmpty(userInfo.getUserSourceId())) {
                values.put(UserEntityDao.Properties.UserSourceId.columnName,
                        userInfo.getUserSourceId());
            }
            if (!TextUtils.isEmpty(userInfo.getUserName())) {
                values.put(UserEntityDao.Properties.UserName.columnName, userInfo.getUserName());
            }
            if (!TextUtils.isEmpty(userInfo.getUserImage())) {
                values.put(UserEntityDao.Properties.UserImage.columnName, userInfo.getUserImage());
            }
            if (!TextUtils.isEmpty(userInfo.getUserIntroduction())) {
                values.put(UserEntityDao.Properties.UserIntroduction.columnName,
                        userInfo.getUserIntroduction());
            }
            if (!TextUtils.isEmpty(userInfo.getEmail())) {
                values.put(UserEntityDao.Properties.Email.columnName, userInfo.getEmail());
            }
            if (!TextUtils.isEmpty(userInfo.getMobile())) {
                values.put(UserEntityDao.Properties.Mobile.columnName, userInfo.getMobile());
            }
            if (!TextUtils.isEmpty(userInfo.getGender())) {
                values.put(UserEntityDao.Properties.Gender.columnName, userInfo.getGender());
            }
            return getDatabase().update(UserEntityDao.TABLENAME, values, updateSql, null);
        } catch (Exception e) {
            XXLog.log_e("UserDBManager", "updateUserInfo is failed:" + e.getMessage());
        }
        return -1;
    }

    /**
     * 是否存在userInfo
     *
     * @param userId 会话id
     * @return boolean
     */
    public boolean isExistUserInfo(String userId) {
        String tableName = UserEntityDao.TABLENAME;
        StringBuilder where = new StringBuilder(" where ");
        DBUtils.buildColumn(where, tableName, UserEntityDao.Properties.UserId.columnName)
                .append("='").append(userId).append("'");
        Cursor cursor = null;
        try {
            cursor = getDatabase()
                    .rawQuery(
                            DBUtils.buildSelectSql(tableName, where.toString(),
                                    UserEntityDao.Properties.UserId.columnName).toString(),
                            null);
            if (cursor != null && cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            XXLog.log_e("UserDBManager", "isExistUserInfo is failed:" + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    /**
     * 为SQLiteStatement绑定值
     *
     * @param statement statement
     * @param userInfo  用户信息
     * @return SQLiteStatement
     */
    private SQLiteStatement bindValues(SQLiteStatement statement, UserInfo userInfo) {
        if (statement == null || userInfo == null) {
            return null;
        }
        statement.clearBindings();
        statement.bindLong(1, userInfo.getStatus());
        if (!TextUtils.isEmpty(userInfo.getUserId())) {
            statement.bindString(2, userInfo.getUserId());
        }
        statement.bindLong(3, userInfo.getUserIdentity());
        statement.bindLong(4, userInfo.getUserSource());
        if (!TextUtils.isEmpty(userInfo.getUserSourceId())) {
            statement.bindString(5, userInfo.getUserSourceId());
        }
        if (!TextUtils.isEmpty(userInfo.getUserName())) {
            statement.bindString(6, userInfo.getUserName());
        }
        if (!TextUtils.isEmpty(userInfo.getUserImage())) {
            statement.bindString(7, userInfo.getUserImage());
        }
        if (!TextUtils.isEmpty(userInfo.getUserIntroduction())) {
            statement.bindString(8, userInfo.getUserIntroduction());
        }
        if (!TextUtils.isEmpty(userInfo.getEmail())) {
            statement.bindString(9, userInfo.getEmail());
        }
        if (!TextUtils.isEmpty(userInfo.getMobile())) {
            statement.bindString(10, userInfo.getMobile());
        }
        if (!TextUtils.isEmpty(userInfo.getGender())) {
            statement.bindString(11, userInfo.getGender());
        }
        return statement;
    }

    /**
     * cursor转换数据
     *
     * @param cursor cursor
     * @return UserInfo
     */
    private UserInfo cursor2Bean(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setStatus(cursor.getInt(0));
        userInfo.setUserId(cursor.getString(1));
        userInfo.setUserIdentity(cursor.getInt(2));
        userInfo.setUserSource(cursor.getInt(3));
        userInfo.setUserSourceId(cursor.getString(4));
        userInfo.setUserName(cursor.getString(5));
        userInfo.setUserImage(cursor.getString(6));
        userInfo.setUserIntroduction(cursor.getString(7));
        userInfo.setEmail(cursor.getString(8));
        userInfo.setMobile(cursor.getString(9));
        userInfo.setGender(cursor.getString(10));
        return userInfo;
    }

}
