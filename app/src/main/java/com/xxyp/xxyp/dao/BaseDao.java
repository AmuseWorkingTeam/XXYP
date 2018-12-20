
package com.xxyp.xxyp.dao;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.xxyp.xxyp.common.db.YuePaiDB;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;

/**
 * Description : 数据库基础dao Created by sunpengfei on 2017/8/4. Person in charge :
 * sunpengfei
 */
public abstract class BaseDao {

    private YuePaiDB mDB;

    public abstract void initAccess();

    public void connectionDB() {
        String userId = SharePreferenceUtils.getInstance().getUserId();
        if (!TextUtils.isEmpty(userId)) {
            mDB = YuePaiDB.getInstance();
            mDB.create(userId);
            if (mDB != null && mDB.getDatabase() != null) {
                initAccess();
            }else{
                XXLog.log_d("BaseDao->connectionDB:", "database is null");
            }
        }else{
            XXLog.log_d("BaseDao->connectionDB:", "UserId is null from SharedPreferences");
        }
    }

    public SQLiteDatabase getDatabase() {
        try {
            SQLiteDatabase database = mDB.getDatabase();
            return database;
        } catch (Exception e) {
            XXLog.log_e("database", e.getMessage());
        }
        return null;
    }

    public DaoSession getSession() {
        return mDB.getSession();
    }

}
