package com.xxyp.xxyp.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xxyp.xxyp.dao.entity.UserEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_ENTITY".
*/
public class UserEntityDao extends AbstractDao<UserEntity, Long> {

    public static final String TABLENAME = "USER_ENTITY";

    /**
     * Properties of entity UserEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property UserIdentity = new Property(2, Integer.class, "userIdentity", false, "USER_IDENTITY");
        public final static Property UserName = new Property(3, String.class, "userName", false, "USER_NAME");
        public final static Property UserIntroduction = new Property(4, String.class, "userIntroduction", false, "USER_INTRODUCTION");
        public final static Property UserImage = new Property(5, String.class, "userImage", false, "USER_IMAGE");
        public final static Property UserSource = new Property(6, Integer.class, "userSource", false, "USER_SOURCE");
        public final static Property UserSourceId = new Property(7, String.class, "userSourceId", false, "USER_SOURCE_ID");
        public final static Property Email = new Property(8, String.class, "email", false, "EMAIL");
        public final static Property Mobile = new Property(9, String.class, "mobile", false, "MOBILE");
        public final static Property Status = new Property(10, Integer.class, "status", false, "STATUS");
    }


    public UserEntityDao(DaoConfig config) {
        super(config);
    }
    
    public UserEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USER_ID\" TEXT NOT NULL ," + // 1: userId
                "\"USER_IDENTITY\" INTEGER," + // 2: userIdentity
                "\"USER_NAME\" TEXT," + // 3: userName
                "\"USER_INTRODUCTION\" TEXT," + // 4: userIntroduction
                "\"USER_IMAGE\" TEXT," + // 5: userImage
                "\"USER_SOURCE\" INTEGER," + // 6: userSource
                "\"USER_SOURCE_ID\" TEXT," + // 7: userSourceId
                "\"EMAIL\" TEXT," + // 8: email
                "\"MOBILE\" TEXT," + // 9: mobile
                "\"STATUS\" INTEGER);"); // 10: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUserId());
 
        Integer userIdentity = entity.getUserIdentity();
        if (userIdentity != null) {
            stmt.bindLong(3, userIdentity);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(4, userName);
        }
 
        String userIntroduction = entity.getUserIntroduction();
        if (userIntroduction != null) {
            stmt.bindString(5, userIntroduction);
        }
 
        String userImage = entity.getUserImage();
        if (userImage != null) {
            stmt.bindString(6, userImage);
        }
 
        Integer userSource = entity.getUserSource();
        if (userSource != null) {
            stmt.bindLong(7, userSource);
        }
 
        String userSourceId = entity.getUserSourceId();
        if (userSourceId != null) {
            stmt.bindString(8, userSourceId);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(9, email);
        }
 
        String mobile = entity.getMobile();
        if (mobile != null) {
            stmt.bindString(10, mobile);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(11, status);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUserId());
 
        Integer userIdentity = entity.getUserIdentity();
        if (userIdentity != null) {
            stmt.bindLong(3, userIdentity);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(4, userName);
        }
 
        String userIntroduction = entity.getUserIntroduction();
        if (userIntroduction != null) {
            stmt.bindString(5, userIntroduction);
        }
 
        String userImage = entity.getUserImage();
        if (userImage != null) {
            stmt.bindString(6, userImage);
        }
 
        Integer userSource = entity.getUserSource();
        if (userSource != null) {
            stmt.bindLong(7, userSource);
        }
 
        String userSourceId = entity.getUserSourceId();
        if (userSourceId != null) {
            stmt.bindString(8, userSourceId);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(9, email);
        }
 
        String mobile = entity.getMobile();
        if (mobile != null) {
            stmt.bindString(10, mobile);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(11, status);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UserEntity readEntity(Cursor cursor, int offset) {
        UserEntity entity = new UserEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // userIdentity
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // userName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // userIntroduction
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // userImage
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // userSource
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // userSourceId
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // email
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // mobile
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10) // status
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.getString(offset + 1));
        entity.setUserIdentity(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setUserName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUserIntroduction(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUserImage(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUserSource(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setUserSourceId(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setEmail(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setMobile(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setStatus(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UserEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UserEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
