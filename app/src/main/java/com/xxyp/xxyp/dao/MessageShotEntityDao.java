package com.xxyp.xxyp.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xxyp.xxyp.dao.entity.MessageShotEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MESSAGE_SHOT_ENTITY".
*/
public class MessageShotEntityDao extends AbstractDao<MessageShotEntity, Long> {

    public static final String TABLENAME = "MESSAGE_SHOT_ENTITY";

    /**
     * Properties of entity MessageShotEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ShotId = new Property(0, Long.class, "shotId", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property DatingShotAddress = new Property(2, String.class, "datingShotAddress", false, "DATING_SHOT_ADDRESS");
        public final static Property Purpose = new Property(3, String.class, "purpose", false, "PURPOSE");
        public final static Property PaymentMethod = new Property(4, String.class, "paymentMethod", false, "PAYMENT_METHOD");
        public final static Property DatingShotIntroduction = new Property(5, String.class, "datingShotIntroduction", false, "DATING_SHOT_INTRODUCTION");
        public final static Property Description = new Property(6, String.class, "description", false, "DESCRIPTION");
        public final static Property DatingUserId = new Property(7, String.class, "datingUserId", false, "DATING_USER_ID");
        public final static Property DatingShotImage = new Property(8, String.class, "datingShotImage", false, "DATING_SHOT_IMAGE");
        public final static Property Status = new Property(9, Integer.class, "status", false, "STATUS");
        public final static Property BelongTo = new Property(10, String.class, "belongTo", false, "BELONG_TO");
    }


    public MessageShotEntityDao(DaoConfig config) {
        super(config);
    }
    
    public MessageShotEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MESSAGE_SHOT_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: shotId
                "\"USER_ID\" TEXT," + // 1: userId
                "\"DATING_SHOT_ADDRESS\" TEXT," + // 2: datingShotAddress
                "\"PURPOSE\" TEXT," + // 3: purpose
                "\"PAYMENT_METHOD\" TEXT," + // 4: paymentMethod
                "\"DATING_SHOT_INTRODUCTION\" TEXT," + // 5: datingShotIntroduction
                "\"DESCRIPTION\" TEXT," + // 6: description
                "\"DATING_USER_ID\" TEXT," + // 7: datingUserId
                "\"DATING_SHOT_IMAGE\" TEXT," + // 8: datingShotImage
                "\"STATUS\" INTEGER," + // 9: status
                "\"BELONG_TO\" TEXT NOT NULL );"); // 10: belongTo
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MESSAGE_SHOT_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MessageShotEntity entity) {
        stmt.clearBindings();
 
        Long shotId = entity.getShotId();
        if (shotId != null) {
            stmt.bindLong(1, shotId);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String datingShotAddress = entity.getDatingShotAddress();
        if (datingShotAddress != null) {
            stmt.bindString(3, datingShotAddress);
        }
 
        String purpose = entity.getPurpose();
        if (purpose != null) {
            stmt.bindString(4, purpose);
        }
 
        String paymentMethod = entity.getPaymentMethod();
        if (paymentMethod != null) {
            stmt.bindString(5, paymentMethod);
        }
 
        String datingShotIntroduction = entity.getDatingShotIntroduction();
        if (datingShotIntroduction != null) {
            stmt.bindString(6, datingShotIntroduction);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(7, description);
        }
 
        String datingUserId = entity.getDatingUserId();
        if (datingUserId != null) {
            stmt.bindString(8, datingUserId);
        }
 
        String datingShotImage = entity.getDatingShotImage();
        if (datingShotImage != null) {
            stmt.bindString(9, datingShotImage);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(10, status);
        }
        stmt.bindString(11, entity.getBelongTo());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MessageShotEntity entity) {
        stmt.clearBindings();
 
        Long shotId = entity.getShotId();
        if (shotId != null) {
            stmt.bindLong(1, shotId);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String datingShotAddress = entity.getDatingShotAddress();
        if (datingShotAddress != null) {
            stmt.bindString(3, datingShotAddress);
        }
 
        String purpose = entity.getPurpose();
        if (purpose != null) {
            stmt.bindString(4, purpose);
        }
 
        String paymentMethod = entity.getPaymentMethod();
        if (paymentMethod != null) {
            stmt.bindString(5, paymentMethod);
        }
 
        String datingShotIntroduction = entity.getDatingShotIntroduction();
        if (datingShotIntroduction != null) {
            stmt.bindString(6, datingShotIntroduction);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(7, description);
        }
 
        String datingUserId = entity.getDatingUserId();
        if (datingUserId != null) {
            stmt.bindString(8, datingUserId);
        }
 
        String datingShotImage = entity.getDatingShotImage();
        if (datingShotImage != null) {
            stmt.bindString(9, datingShotImage);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(10, status);
        }
        stmt.bindString(11, entity.getBelongTo());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MessageShotEntity readEntity(Cursor cursor, int offset) {
        MessageShotEntity entity = new MessageShotEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // shotId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // datingShotAddress
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // purpose
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // paymentMethod
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // datingShotIntroduction
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // description
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // datingUserId
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // datingShotImage
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // status
            cursor.getString(offset + 10) // belongTo
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MessageShotEntity entity, int offset) {
        entity.setShotId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDatingShotAddress(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPurpose(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPaymentMethod(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDatingShotIntroduction(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDescription(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDatingUserId(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDatingShotImage(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setStatus(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setBelongTo(cursor.getString(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MessageShotEntity entity, long rowId) {
        entity.setShotId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MessageShotEntity entity) {
        if(entity != null) {
            return entity.getShotId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MessageShotEntity entity) {
        return entity.getShotId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
