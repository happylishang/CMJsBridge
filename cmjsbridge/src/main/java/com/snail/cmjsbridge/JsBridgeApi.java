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

    public void openJsBridgeChannal(@NonNull WebView webView) {
        webView.addJavascriptInterface(mJsCallMethod, "JsMethodApi");
    }

    public void notifyH5(String jsonString, int id) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("jsonrpc", VERSION);
            jsonObject.put("id", Integer.valueOf(id));
            jsonObject.put("result", jsonString);
        } catch (JSONException e) {
            return;
        }
        mWebView.loadUrl("javascript:" + "window.jsonRPC.onMessage(" + jsonObject.toString() + ")");
    }
}
