package com.snail.cmjsbridgedemo;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.snail.cmjsbridge.IJsCallBack;
import com.snail.cmjsbridge.JsBridgeApi;
import com.snail.cmjsbridge.JsMessageBean;

/**
 * Author: snail
 * Data: 2021/11/14.
 * Des:
 * version:
 */

class JsBridgeWebView extends WebView {

    private JsBridgeApi mJsBridgeApi;

    public JsBridgeWebView(Context context) {
        this(context, null);
    }

    public JsBridgeWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JsBridgeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mJsBridgeApi = new JsBridgeApi(this, new IJsCallBack() {
            @Override
            public void onJsCall(JsMessageBean jsMessageBean) {

            }
        });
    }
}
