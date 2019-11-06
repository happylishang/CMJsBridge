package com.snail.cmjsbridge;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class JsWebChromClient extends WebChromeClient {

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {


        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}
