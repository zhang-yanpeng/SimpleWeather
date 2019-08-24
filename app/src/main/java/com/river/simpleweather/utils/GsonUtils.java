package com.river.simpleweather.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangYanPeng on 2019/8/24.
 */
public class GsonUtils {
    private static Gson gson = new Gson();

    private GsonUtils(){}

    public Gson getInstabce(){
        return gson;
    }

    /**
     * 对象转json
     * @param obj
     * @return
     */
    public String objectToJson(Object obj){
        return gson.toJson(obj);
    }

    /**
     * 将gsonString转成泛型bean
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * 将json字符串转换为bean对象的集合
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> parseJsonStr2ObjectList(String jsonString, Class<T> cls) {
        List<T> objList = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(jsonString);
            for (int i = 0; i < ja.length(); i++) {
                T t = gson.fromJson(ja.getJSONObject(i).toString(), cls);
                objList.add(t);
            }
        } catch (Exception e) {

        }

        return objList;
    }


    /**
     * 转成list
     * 泛型在编译期类型被擦除导致报错
     * @param gsonString
     * @param cls
     * @return
     */
//    public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
//        List<T> list = null;
//        if (gson != null) {
//            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
//            }.getType());
//        }
//        return list;
//    }
    /**
     * 转成list
     * 解决泛型在编译期类型被擦除导致报错
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }




    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }


    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}
