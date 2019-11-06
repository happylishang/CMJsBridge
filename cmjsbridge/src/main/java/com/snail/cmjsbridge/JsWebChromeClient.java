package com.snail.cmjsbridge;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class JsWebChromeClient extends WebChromeClient {

    JsBridgeApi mJsBridgeApi;

    public JsWebChromeClient(JsBridgeApi jsBridgeApi) {
        mJsBridgeApi = jsBridgeApi;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {

        try {
            if (mJsBridgeApi.handleJsCall(message)) {

                result.confirm("result");
//                Thread.sleep(5000);
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        //   未处理走默认逻辑
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}
