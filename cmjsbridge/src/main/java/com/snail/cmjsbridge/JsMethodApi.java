package com.snail.cmjsbridge;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;

import com.google.gson.Gson;


public class JsMethodApi {
    private IJsCallBack mIJsCallBack;
    private static final int JS_CALL = 1000;
    private static final int NATIVE_JS_CALLBACK = 1001;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case JS_CALL:
                case NATIVE_JS_CALLBACK:
                    if (mIJsCallBack != null) {
                        // 这里是否需要同步回调呢？还是等消息执行完，用户灵活回调
                        //   把口子留给外层，如果有耗时操作，更灵活
                        mIJsCallBack.onJsCall((JsMessageBean) msg.obj);
                    }
                    break;
            }

        }
    };

    public JsMethodApi(IJsCallBack callBack) {
        mIJsCallBack = callBack;
    }

    /**
     * js调用native，可能需要回调
     */
    @JavascriptInterface
    public void callNative(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return;
        }

        JsMessageBean bean = JsonUtil.parseObject(jsonString, JsMessageBean.class);

        if (bean == null) {
            return;
        }
        mHandler.obtainMessage(JS_CALL, bean).sendToTarget();

    }


    /**
     * natvice调用js，并且需要js回调，这里就是js回调入口
     */
    @JavascriptInterface
    public void notifyNativeCallBack(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return;
        }

        JsMessageBean bean = JsonUtil.parseObject(jsonString, JsMessageBean.class);

        if (bean == null) {
            return;
        }
        mHandler.obtainMessage(NATIVE_JS_CALLBACK, bean);
    }

}
