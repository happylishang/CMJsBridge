package com.snail.cmjsbridge;

import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * js回调native需要封装的数据格式
 */
public class JsMessageBean {

    public String version;
    public String method;
    public String params;//json参数
    public int id;
}
