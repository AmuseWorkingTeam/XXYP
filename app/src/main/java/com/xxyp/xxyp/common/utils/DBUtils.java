
package com.xxyp.xxyp.common.utils;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Description : 数据库操作工具类 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public class DBUtils {

    /**
     * 将字符串数据转为字符串
     *
     * @param arrays 数据源
     * @return String
     */
    public static String buildStringWithStrArray(String... arrays) {
        if (arrays == null || arrays.length == 0)
            return null;
        StringBuilder result = new StringBuilder("'");
        for (String array : arrays) {
            result.append(array).append("','");
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }

    /**
     * 将字符串数据转为字符串
     *
     * @param arrays 数据源
     * @return String
     */
    public static String buildStringWithArray(int... arrays) {
        if (arrays == null || arrays.length == 0)
            return null;
        StringBuilder result = new StringBuilder();
        for (int array : arrays) {
            result.append(array).append(",");

        }
        result.delete(result.length() - 1, result.length());
        return result.toString();
    }

    /**
     * 将字符串数据转为字符串
     *
     * @param list 数据源
     * @return String
     */
    public static String buildStringWithList(List<String> list) {
        if (list == null || list.size() == 0)
            return null;
        StringBuilder result = new StringBuilder("'");
        int size = list.size();
        for (int i = 0; i < size; i++) {
            result.append(list.get(i)).append(size == i + 1 ? "'" : "','");
        }
        return result.toString();
    }

    /**
     * 将long数据转为字符串
     *
     * @param list 数据源
     * @return String
     */
    public static String buildStringWithLongList(List<Long> list) {
        if (list == null || list.size() == 0)
            return null;
        StringBuilder result = new StringBuilder("");
        int size = list.size();
        for (int i = 0; i < size; i++) {
            result.append(list.get(i)).append(size == i + 1 ? "" : ",");
        }
        return result.toString();
    }

    /**
     * 将long数据转为字符串
     *
     * @param list 数据源
     * @return String
     */
    public static String buildStringWithIntegerList(List<Integer> list) {
        if (list == null || list.size() == 0)
            return null;
        StringBuilder result = new StringBuilder("");
        int size = list.size();
        for (int i = 0; i < size; i++) {
            result.append(list.get(i)).append(size == i + 1 ? "" : ",");
        }
        return result.toString();
    }

    /**
     * 字符串转为列表
     *
     * @param str 字符串
     * @return List<String>
     */
    public static List<String> buildListWithStr(String str) {
        if (str == null)
            return null;

        List<String> result;
        String[] temp;
        if (TextUtils.equals(str.trim(), "")) {
            temp = new String[] {
                    ""
            };
        } else {
            temp = str.split(",");
        }
        result = Arrays.asList(temp);
        return result;
    }

    /**
     * 构造查询sql
     *
     * @param tableName1 表名
     * @param where 条件
     * @param columns 要查询的列
     * @return String
     */
    public static StringBuilder buildSelectSql(String tableName1, String where, String... columns) {
        StringBuilder builder = new StringBuilder("select ");
        if (columns == null || columns.length == 0) {
            builder.append("*").append(",");
        } else {
            for (String columnName : columns) {
                builder.append(columnName).append(",");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(" from ").append(tableName1);
        builder.append(TextUtils.isEmpty(where) ? "" : where);
        return builder;
    }

    /**
     * 构造查询sql
     *
     * @param tableName1 表名
     * @param where 条件
     * @param columns 要查询的列
     * @return String
     */
    public static StringBuilder buildSelectSqlWithTwoTable(String tableName1, String tableName2,
                                                           String where, String... columns) {
        StringBuilder builder = new StringBuilder("select ");
        if (columns == null || columns.length == 0) {
            builder.append("*").append(",");
        } else {
            for (String columnName : columns) {
                builder.append(columnName).append(",");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(" from ").append(tableName1);
        if (!TextUtils.isEmpty(tableName2)) {
            builder.append(",").append(tableName2);
        }
        builder.append(TextUtils.isEmpty(where) ? "" : where);
        return builder;
    }

    /**
     * 构造插入SQLiteStatement
     *
     * @param tableName 表名
     * @param columns 需要插入数据的列
     * @return StringBuilder
     */
    public static StringBuilder buildInsertSql(String tableName, String... columns) {
        StringBuilder builder = new StringBuilder("insert into ");
        builder.append(tableName).append(" (");
        if (columns == null || columns.length == 0) {
            throw new IllegalStateException("no columns need insert");
        } else {
            for (String columnName : columns) {
                builder.append(columnName).append(",");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(") VALUES (");
        buildPlaceholders(builder, columns.length);
        builder.append(")");
        return builder;
    }

    /**
     * 构建升级SQLiteStatement
     *
     * @param tableName 表名
     * @param updateColumns 需要升级数据列名
     * @return StringBuilder
     */
    public static StringBuilder buildUpdateSql(String tableName, String[] whereColumns,
                                               String... updateColumns) {
        StringBuilder builder = new StringBuilder("UPDATE ");
        builder.append(tableName).append(" SET ");
        if (updateColumns == null || updateColumns.length == 0) {
            throw new IllegalStateException("no columns need update");
        } else {
            for (String columnName : updateColumns) {
                builder.append(columnName).append("=?").append(",");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        if (whereColumns != null && whereColumns.length > 0) {
            builder.append(" WHERE ");
            buildColumnsEqValue(builder, tableName, whereColumns);
        }
        return builder;
    }

    /**
     * 构造删除SQLiteStatement
     *
     * @param tableName 表名
     * @param columns 需要数据的列
     * @return StringBuilder
     */
    public static StringBuilder buildDeleteSql(String tableName, String... columns) {
        StringBuilder builder = new StringBuilder("DELETE FROM ");
        builder.append(tableName);
        if (columns != null && columns.length > 0) {
            builder.append(" WHERE ");
            buildColumnsEqValue(builder, tableName, columns);
        }
        return builder;
    }

    /**
     * 构造SQLiteStatement
     */
    private static StringBuilder buildPlaceholders(StringBuilder builder, int count) {
        for (int i = 0; i < count; ++i) {
            if (i < count - 1) {
                builder.append("?,");
            } else {
                builder.append('?');
            }
        }

        return builder;
    }

    /**
     * 构建等于条件
     *
     * @param tableAlias 表别名
     * @param columns 列
     * @return StringBuilder
     */
    private static StringBuilder buildColumnsEqValue(StringBuilder builder, String tableAlias,
                                                     String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            buildColumn(builder, tableAlias, columns[i]).append("=?");
            if (i < columns.length - 1) {
                builder.append(" AND ");
            }
        }
        return builder;
    }

    /**
     * 增加列
     *
     * @param tableAlias 表别名
     * @param column 列名
     * @return StringBuilder
     */
    public static StringBuilder buildColumn(StringBuilder builder, String tableAlias,
                                            String column) {
        builder.append(tableAlias).append(".").append(column);
        return builder;
    }

    /**
     * 构建where 条件
     *
     * @param column 列名
     * @param columnValue 字符串值
     * @return StringBuilder
     */
    public static StringBuilder buildWhereStrValue(StringBuilder builder, String column,
                                                   String columnValue) {
        if (builder == null) {
            return null;
        }
        builder.append(column).append("='").append(columnValue).append("'");
        return builder;
    }

    /**
     * 构建where 条件
     *
     * @param column 列名
     * @param columnValue 非字符串值
     * @return StringBuilder
     */
    public static StringBuilder buildWhereNoStrValue(StringBuilder builder, String column,
                                                     long columnValue) {
        if (builder == null) {
            return null;
        }
        builder.append(column).append("=").append(columnValue);
        return builder;
    }

    /**
     * 构建where 条件
     *
     * @param column 列名
     * @param columnValues in的值范围
     * @return StringBuilder
     */
    public static StringBuilder buildWhereInValue(StringBuilder builder, String column,
                                                  String columnValues) {
        if (builder == null) {
            return null;
        }
        if (TextUtils.isEmpty(columnValues)) {
            return builder;
        }
        builder.append(column).append(" IN (").append(columnValues).append(")");
        return builder;
    }

    /**
     * 构建where 条件
     *
     * @param column 列名
     * @param columnValues in的值范围
     * @return StringBuilder
     */
    public static StringBuilder buildWhereNotInValue(StringBuilder builder, String column,
                                                     String columnValues) {
        if (builder == null) {
            return null;
        }
        if (TextUtils.isEmpty(columnValues)) {
            return builder;
        }
        builder.append(column).append(" NOT IN (").append(columnValues).append(")");
        return builder;
    }
}
