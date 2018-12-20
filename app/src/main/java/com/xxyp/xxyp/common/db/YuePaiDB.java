
package com.xxyp.xxyp.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xxyp.xxyp.common.utils.AppContextUtils;
import com.xxyp.xxyp.dao.DaoMaster;
import com.xxyp.xxyp.dao.DaoSession;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

/**
 * Description : 约拍数据库 Person in charge : sunpengfei
 */

public class YuePaiDB {

    public static final String DATABASE_NAME_SUFFIX = "_YuePai.db";

    private static final int DEFAULT_VERSION = 1;

    public static final int VERSION = 1;

    private static YuePaiDB instance;

    private YuePaiSQLiteOpenHelper mHelper;

    private DaoSession mSession;

    private String mUserId;

    private SQLiteDatabase sdb;

    public static YuePaiDB getInstance() {
        if (instance == null) {
            synchronized (YuePaiDB.class){
                if(instance == null){
                    instance = new YuePaiDB();
                }
            }
        }
        return instance;
    }

    public void create(String userId) {
        mUserId = userId;
        open(userId);
    }

    /**
     * 打开数据库
     * @param userId  用户id
     */
    private void open(String userId) {
        String name = userId + "_YuePai.db";
        mHelper = new YuePaiSQLiteOpenHelper(AppContextUtils.getAppContext(), name, null);
        sdb = mHelper.getWritableDatabase();
        mSession = new DaoMaster(sdb).newSession();
    }

    /**
     * 关闭db
     */
    public void close() {
        if (sdb != null) {
            sdb.close();
        }
        if (mHelper != null) {
            mHelper.close();
        }
        mHelper = null;
        mSession = null;
    }

    public String getDBName() {
        if (mHelper != null) {
            return mHelper.getDatabaseName();
        }
        return "";
    }

    public SQLiteDatabase getDatabase() {
        if (sdb == null || !sdb.isOpen()) {
            create(mUserId);
        }
        return sdb;
    }

    public DaoSession getSession() {
        return mSession;
    }

    static class YuePaiSQLiteOpenHelper extends DatabaseOpenHelper {

        public YuePaiSQLiteOpenHelper(Context context, String name,
                                      SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, 1);
        }

        @Override
        public void onCreate(Database db) {
            DaoMaster.createAllTables(db, false);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            super.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
