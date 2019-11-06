package com.snail.cmjsbridge;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseArray;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;

class JsMethodApi {
    private IJsCallBack mIJsCallBack;
    private static final int JS_CALL = 1000;
    private static final int NATIVE_JS_CALLBACK = 1001;
    private static SparseArray<NativeJSCallBack> mCallNativeBack = new SparseArray<>();


    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case JS_CALL:
                    if (mIJsCallBack != null) {
                        // 这里是否需要同步回调呢？还是等消息执行完，用户灵活回调
                        //   把口子留给外层，如果有耗时操作，更灵活
                        mIJsCallBack.onJsCall((JsMessageBean) msg.obj);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    void destroy() {
        mHandler.removeCallbacksAndMessages(null);
    }

    JsMethodApi(IJsCallBack callBack) {
        mIJsCallBack = callBack;
    }


    boolean callNative(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return true;
        }

        JsMessageBean bean = JsonUtil.parseObject(jsonString, JsMessageBean.class);

        if (bean == null) {
            return false;
        }
        mHandler.obtainMessage(JS_CALL, bean).sendToTarget();
        return true;
    }


    void addCallBack(int messageId, NativeJSCallBack runnable) {
        if (runnable != null) {
            synchronized (JsMethodApi.class) {
                mCallNativeBack.put(messageId, runnable);
            }
        }
    }


    private static class InnerRunnable implements Runnable {
        private NativeJSCallBack mNativeJSCallBack;
        private JsResultBean mJsResultBean;

        InnerRunnable(NativeJSCallBack callBack, JsResultBean jsResultBean) {
            mNativeJSCallBack = callBack;
            mJsResultBean = jsResultBean;
        }

        @Override
        public void run() {
            if (mNativeJSCallBack != null) {
                mNativeJSCallBack.onResult(mJsResultBean.jsonString);
            }
        }
    }

    private static class JsResultBean {
        String jsonString;
        int messageId;
    }
}