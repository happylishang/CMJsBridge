package com.snail.cmjsbridge;

import org.json.JSONObject;

/**
 * js回调native需要封装的数据格式
 */
public class JsMessageBean {

    public String version;
    public String method;
    public JSONObject params;
    public int id;
}
