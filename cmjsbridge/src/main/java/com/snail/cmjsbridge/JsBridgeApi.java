package com.snail.cmjsbridge;

import android.webkit.WebView;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class JsBridgeApi {


    private JsMethodApi mJsCallMethod;
    private WebView mWebView;
    private static final String VERSION = "1.0";


    public JsBridgeApi(WebView webView, IJsCallBack callBack) {
        mWebView = webView;
        mJsCallMethod = new JsMethodApi(callBack);
    }

    public void openJsBridgeChannel(@NonNull WebView webView) {
        webView.addJavascriptInterface(mJsCallMethod, "JsMethodApi");
    }

    //    清理未处理的消息
    public void destroy() {
        mJsCallMethod.destroy();
    }

    /**
     * native通知前端Js任务执行完毕，并回传结果
     * 固定有js中的window.jsonRPC.onMessage方法
     */
    public void notifyNativeTaskFinished(String jsonString, int id) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("jsonrpc", VERSION);
            jsonObject.put("id", Integer.valueOf(id));
            jsonObject.put("result", jsonString);
        } catch (JSONException e) {
            return;
        }
        callH5FromeNative("window.jsonRPC.onMessage(" + jsonObject.toString() + ")");
    }

    public void callH5FromeNative(String request) {
        mWebView.loadUrl("javascript:" + request);
    }
}
