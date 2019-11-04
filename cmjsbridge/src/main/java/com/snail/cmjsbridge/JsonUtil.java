package com.snail.cmjsbridge;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonUtil {
    public static String toJsonString(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T parseObject(String jsonString, Class<T> tClass) {
        return new Gson().fromJson(jsonString, tClass);
    }

    public static JsonObject getJsonObject(){
        return new JsonObject();
    }
}
