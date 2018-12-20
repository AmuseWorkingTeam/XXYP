package com.xxyp.xxyp.publish.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.DBUtils;
import com.xxyp.xxyp.common.utils.GsonUtils;
import com.xxyp.xxyp.dao.BaseDao;
import com.xxyp.xxyp.dao.ShotEntityDao;
import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.main.bean.ShotPhotoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 发布数据库管理类
 * Created by sunpengfei on 2017/8/11.
 * Person in charge : sunpengfei
 */
public class PublishDBManager extends BaseDao {

    private static PublishDBManager mInstance;

    public static PublishDBManager getInstance() {
        if (mInstance == null) {
            synchronized (PublishDBManager.class){
                if(mInstance == null){
                    mInstance = new PublishDBManager();
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
     * 插入约拍数据
     * @param shotBean  约拍信息
     * @return long
     */
    public long addShotInfo(SQLiteDatabase db, ShotBean shotBean){
        if (shotBean == null) {
            return -1;
        }
        SQLiteStatement statement = null;
        try {
            if (db == null) {
                db = getDatabase();
            }
            String insertSql = DBUtils.buildInsertSql(ShotEntityDao.TABLENAME,
                    ShotEntityDao.Properties.Status.columnName,
                    ShotEntityDao.Properties.UserId.columnName,
                    ShotEntityDao.Properties.DatingUserId.columnName,
                    ShotEntityDao.Properties.DatingShotId.columnName,
                    ShotEntityDao.Properties.Purpose.columnName,
                    ShotEntityDao.Properties.PaymentMethod.columnName,
                    ShotEntityDao.Properties.DatingShotAddress.columnName,
                    ShotEntityDao.Properties.DatingShotIntroduction.columnName,
                    ShotEntityDao.Properties.Description.columnName,
                    ShotEntityDao.Properties.DatingShotImages.columnName,
                    ShotEntityDao.Properties.ReleaseTime.columnName).toString();
            statement = db.compileStatement(insertSql);
            return bindShotValues(statement, shotBean).executeInsert();
        } catch (Exception e) {
            XXLog.log_e("PublishDBManager", "addShotInfo is failed" + e.getMessage());
            return -1;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 获取所有约拍数据
     * @return ShotBean
     */
    public List<ShotBean> getShotInfo(){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getDatabase();
            String selectSql = DBUtils.buildSelectSql(ShotEntityDao.TABLENAME, null,
                    ShotEntityDao.Properties.Status.columnName,
                    ShotEntityDao.Properties.UserId.columnName,
                    ShotEntityDao.Properties.DatingUserId.columnName,
                    ShotEntityDao.Properties.DatingShotId.columnName,
                    ShotEntityDao.Properties.Purpose.columnName,
                    ShotEntityDao.Properties.PaymentMethod.columnName,
                    ShotEntityDao.Properties.DatingShotAddress.columnName,
                    ShotEntityDao.Properties.DatingShotIntroduction.columnName,
                    ShotEntityDao.Properties.Description.columnName,
                    ShotEntityDao.Properties.DatingShotImages.columnName,
                    ShotEntityDao.Properties.ReleaseTime.columnName).toString();
            cursor = db.rawQuery(selectSql, null);
            if (cursor != null) {
                List<ShotBean> shotBeans = new ArrayList<>();
                while (cursor.moveToNext()){
                    ShotBean bean = cursor2ShotBean(cursor);
                    if(bean != null){
                        shotBeans.add(bean);
                    }
                }
                return shotBeans;
            }
        } catch (Exception e) {
            XXLog.log_e("UserDBManager",
                    "getUserInfoByDB is failed" + e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 为SQLiteStatement绑定值
     * @param statement statement
     * @param shotBean  约拍信息
     * @return SQLiteStatement
     */
    private SQLiteStatement bindShotValues(SQLiteStatement statement, ShotBean shotBean) {
        if (statement == null || shotBean == null) {
            return null;
        }
        statement.clearBindings();
        statement.bindLong(1, shotBean.getStatus());
        if (!TextUtils.isEmpty(shotBean.getUserId())) {
            statement.bindString(2, shotBean.getUserId());
        }
        if (!TextUtils.isEmpty(shotBean.getDatingUserId())) {
            statement.bindString(3, shotBean.getDatingUserId());
        }
        if (!TextUtils.isEmpty(shotBean.getDatingShotId())) {
            statement.bindString(4, shotBean.getDatingShotId());
        }
        if (!TextUtils.isEmpty(shotBean.getPurpose())) {
            statement.bindString(5, shotBean.getPurpose());
        }
        if (!TextUtils.isEmpty(shotBean.getPaymentMethod())) {
            statement.bindString(6, shotBean.getPaymentMethod());
        }
        if (!TextUtils.isEmpty(shotBean.getDatingShotAddress())) {
            statement.bindString(7, shotBean.getDatingShotAddress());
        }
        if (!TextUtils.isEmpty(shotBean.getDatingShotIntroduction())) {
            statement.bindString(8, shotBean.getDatingShotIntroduction());
        }
        if (!TextUtils.isEmpty(shotBean.getDescription())) {
            statement.bindString(9, shotBean.getDescription());
        }
        if (shotBean.getDatingShotImages() != null && shotBean.getDatingShotImages().size() > 0) {
            statement.bindString(10, GsonUtils.objectToJson(shotBean.getDatingShotImages()));
        }
        statement.bindLong(11, shotBean.getReleaseTime());
        return statement;
    }

    /**
     * cursor转换数据
     * @param cursor cursor
     * @return ShotBean
     */
    private ShotBean cursor2ShotBean(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        ShotBean shotBean = new ShotBean();
        shotBean.setStatus(cursor.getInt(0));
        shotBean.setUserId(cursor.getString(1));
        shotBean.setDatingUserId(cursor.getString(2));
        shotBean.setDatingShotId(cursor.getString(3));
        shotBean.setPurpose(cursor.getString(4));
        shotBean.setPaymentMethod(cursor.getString(5));
        shotBean.setDatingShotAddress(cursor.getString(6));
        shotBean.setDatingShotIntroduction(cursor.getString(7));
        shotBean.setDescription(cursor.getString(8));
        shotBean.setDatingShotImages(
                GsonUtils.jsonToList(cursor.getString(9), ShotPhotoBean.class));
        shotBean.setReleaseTime(cursor.getLong(10));
        return shotBean;
    }
}
