
package com.xxyp.xxyp.user.view.wheel.area;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : 地区信息代表操作
 * Created by wxh on 2016/6/17 16:12.
 * Job number：138746
 * Phone ：15233620521
 * Email：wangxiaohui@syswin.com
 * Person in charge : 王晓辉
 * Leader：王晓辉
 */
public class AreaDBMgr {

    private Context context;

    private static SQLiteDatabase db;

    private static final String DB_NAME = "area2.db";

    private static volatile AreaDBMgr instance;

    private AreaDBMgr(Context context) {
        this.context = context;
        initDB();
    }

    public static AreaDBMgr getInstance(Context context) {
        if (instance == null) {
            synchronized (AreaDBMgr.class) {
                if (instance == null) {
                    instance = new AreaDBMgr(context);
                }
            }
        }
        return instance;
    }

    /**
     * 获取省份
     *
     * @return List<Area>
     */
    public List<Area> getProvinces() {
        String sql = "select name,code from region where superior=0 order by code";
        if (db == null)
            return null;
        Cursor cs = null;
        try {
            cs = db.rawQuery(sql, null);
            if (cs != null && cs.getCount() > 0) {
                List<Area> result = new ArrayList<>();
                int index = 0;
                while (cs.moveToNext()) {
                    Area area = new Area();
                    area.setIndex(index);
                    area.setName(cs.getString(0));
                    area.setCode(cs.getString(1));
                    index++;
                    result.add(area);
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return null;
    }

    /**
     * 根据省份code获取城市
     *
     * @param code 省code
     * @return List<Area>
     */
    public List<Area> getCityByCode(String code) {
        String sql = "select name,code from region where superior=" + code + " order by code";
        if (db == null)
            return null;
        Cursor cs = null;
        try {
            cs = db.rawQuery(sql, null);
            if (cs != null && cs.getCount() > 0) {
                List<Area> result = new ArrayList<>();
                int index = 0;
                while (cs.moveToNext()) {
                    Area area = new Area();
                    area.setIndex(index);
                    area.setName(cs.getString(0));
                    area.setCode(cs.getString(1));
                    index++;
                    result.add(area);
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return null;
    }

    /**
     * 根据城市code获取区域
     *
     * @param code 城市code
     * @return List<Area>
     */
    public List<Area> getDistrictsByCode(String code) {
        String sql = "select name,code from region where superior=" + code + " order by code";
        if (db == null)
            return null;
        Cursor cs = null;
        try {
            cs = db.rawQuery(sql, null);
            if (cs != null && cs.getCount() > 0) {
                List<Area> result = new ArrayList<>();
                int index = 0;
                while (cs.moveToNext()) {
                    Area area = new Area();
                    area.setIndex(index);
                    area.setName(cs.getString(0));
                    area.setCode(cs.getString(1));
                    index++;
                    result.add(area);
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return null;
    }

    /**
     * 根据地区码获取所有地区级别
     *
     * @param code 地区码
     * @return int
     */
    public int getRegionLevelByCode(String code) {
        String sql = "select regionLevel from region where code=" + code;
        if (db == null)
            return -1;
        Cursor cs = null;
        try {
            cs = db.rawQuery(sql, null);
            if (cs != null && cs.getCount() > 0) {
                if (cs.moveToFirst()) {
                    return cs.getInt(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return -1;
    }

    /**
     * 根据地区码获取所有地区详情
     *
     * @param code 地区码
     * @return Area
     */
    public Area getAreaByDistrictCode(String code) {
        int level = getRegionLevelByCode(code);
        String sql = "select ";
        switch (level) {
            case 3:
                sql = sql
                        + " c.name,b.name,a.name from region a,region b,region c where a.Superior=b.code and b.Superior=c.code and a.code="
                        + code;
                break;
            case 2:
                sql = sql
                        + " b.name,a.name from region a,region b where a.Superior=b.code and a.code="
                        + code;
                break;
            case 1:
                sql = sql + " name from region where code=" + code;
                break;
            default:
                return null;
        }
        if (db == null)
            return null;
        Cursor cs = null;
        try {
            cs = db.rawQuery(sql, null);
            if (cs != null && cs.getCount() > 0) {
                if (cs.moveToFirst()) {
                    Area area = new Area();
                    area.setProvince(cs.getString(0));
                    if (level > 1) {
                        area.setCity(cs.getString(1));
                    }
                    if (level > 2) {
                        area.setDistrict(cs.getString(2));
                    }
                    return area;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return null;
    }

    /**
     * 根据省或者直辖市获取所有地区
     *
     * @param code 城市code
     * @return List<Area>
     */
    public List<Area> getAllDistrictsByCode(String code) {
        if (!TextUtils.isEmpty(code) && code.length() > 2) {
            code = code.substring(0, 2);
        }
        String sql = "select name,code from region where code like '" + code
                + "%' and regionlevel=3 order by code";
        if (db == null)
            return null;
        Cursor cs = null;
        try {
            cs = db.rawQuery(sql, null);
            if (cs != null && cs.getCount() > 0) {
                List<Area> result = new ArrayList<>();
                int index = 0;
                while (cs.moveToNext()) {
                    Area area = new Area();
                    area.setIndex(index);
                    area.setName(cs.getString(0));
                    area.setCode(cs.getString(1));
                    index++;
                    result.add(area);
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return null;
    }

    // 初始化外部数据库
    private void initDB() {
        String dbPath = context.getDatabasePath(DB_NAME).getAbsolutePath();
        // 删除原有数据

        String oldDbPath = context.getDatabasePath("common.db").getAbsolutePath();
        String areaDbPath = context.getDatabasePath("area.db").getAbsolutePath();
        File file = new File(oldDbPath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        File file2 = new File(areaDbPath);
        if (file2.exists() && file2.isFile()) {
            file2.delete();
        }
        if (!new File(dbPath).exists()) {
            InputStream is = null;
            OutputStream os = null;
            try {
                is = context.getAssets().open(DB_NAME);
                os = new FileOutputStream(dbPath);
                // 文件写入
                byte[] buffer = new byte[1024];
                int length ;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        os.flush();
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
    }

    /**
     * 根据code获取地区信息
     * @param adCode 地区code
     * @return Area
     */
    public Area getAreaByAdCode(String adCode) {
        String sql = "select name from region where code=" + adCode;
        if (db == null)
            return null;
        Cursor cs = db.rawQuery(sql, null);
        try {
            if (cs != null && cs.getCount() > 0) {
                Area area = new Area();
                if (cs.moveToFirst()) {
                    area.setName(cs.getString(0));
                    area.setCode(adCode);
                }
                return area;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
        return null;
    }

}
