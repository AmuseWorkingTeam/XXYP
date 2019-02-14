
package com.xxyp.xxyp.common.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : json转化utils
 */
public class GsonUtils {

    /**
     * 将对象转为json字符串
     */
    public static String objectToJson(Object entity) {
        try {
            if (entity == null) {
                return null;
            }
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd");
            Gson gson = builder.create();
            return gson.toJson(entity);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 将json转为Object
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObject(String json, Class clazzT) {
        try {
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd");
            Gson gson = builder.create();
            return (T)gson.fromJson(json, clazzT);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 将json转为List
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazzT) {
        try {
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            List<T> result = new ArrayList<>();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                result.add(new Gson().fromJson(elem, clazzT));
            }
            return result;
        } catch (Exception ex) {
            return null;
        }
    }
}
