package com.snail.cmjsbridge;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;

class JsMethodApi {
    private IJsCallBack mIJsCallBack;
    private static final int JS_CALL = 1000;
    private static final int NATIVE_JS_CALLBACK = 1001;
    private SparseArray<NativeJSCallBack> mCallNativeBack = new SparseArray<>();
    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case JS_CALL:
                    if (mIJsCallBack != null && msg.obj instanceof JsMessageBean) {
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
        mHandler=null;
    }

    JsMethodApi(IJsCallBack callBack) {
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
        if(mHandler!=null){
            mHandler.obtainMessage(JS_CALL, bean).sendToTarget();
        }

    }


    /**
     * js调用native，可能需要回调
     */
    @JavascriptInterface
    public String syncCallNativeWithReturn(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }


//        需要返回值的同步处理
        return "asdfasdfasdf";
    }


    /**
     * natvice调用js，并且需要js回调，这里就是js回调入口
     */
    @JavascriptInterface
    public void notifyNativeCallBack(String jsonString, int messageId) {
        if (TextUtils.isEmpty(jsonString)) {
            return;
        }
        NativeJSCallBack callBack = mCallNativeBack.get(messageId);
        if (callBack != null) {
            synchronized (JsMethodApi.class) {
                mCallNativeBack.remove(messageId);
            }
            JsResultBean jsResultBean = new JsResultBean();
            jsResultBean.jsonString = jsonString;
            jsResultBean.messageId = messageId;
            if (mHandler != null) {
                mHandler.post(new InnerRunnable(callBack, jsResultBean));
            }
        }
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