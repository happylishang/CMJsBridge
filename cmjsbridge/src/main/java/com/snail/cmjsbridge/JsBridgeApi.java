package com.snail.cmjsbridge;

import android.webkit.WebView;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class JsBridgeApi {


    private JsMethodApi mJsCallMethod;
    private WebView mWebView;
    private static final String VERSION = "1.0";
    private static int sMessageId = 0;

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
     * 固定有js中的window.jsRPC.onMessage方法
     */
    public void notifyNativeTaskFinished(String jsonString, int id) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("jsRPC", VERSION);
            jsonObject.put("id", Integer.valueOf(id));
            jsonObject.put("result", jsonString);
        } catch (Exception e) {
            return;
        }
        callH5("window.jsRPC.onJsCallFinished(" + jsonObject.toString() + ")");
    }


    /**
     * 一般需要前端提供函数，自行处理，不统一封装也许更好
     * 如果需要处理回到 id自己加入到request.message的message中
     */
    public void callH5FromNative(NativeMessageBean request, NativeJSCallBack callBack) {
        mJsCallMethod.addCallBack(request.messageId, callBack);
        callH5(request.message);
    }

    private void callH5(String request) {
        mWebView.loadUrl("javascript:" + request);
    }
}
