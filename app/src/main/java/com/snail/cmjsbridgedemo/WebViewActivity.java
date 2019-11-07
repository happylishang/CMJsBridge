package com.snail.cmjsbridgedemo;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.snail.cmjsbridge.IJsCallBack;
import com.snail.cmjsbridge.JsBridgeApi;
import com.snail.cmjsbridge.JsMessageBean;
import com.snail.cmjsbridge.JsWebChromeClient;
import com.snail.cmjsbridge.JsonUtil;
import com.snail.cmjsbridge.NativeJSCallBack;
import com.snail.cmjsbridge.NativeMessageBean;


/**
 * Author: hzlishang
 * Data: 16/7/20 下午7:15
 * Des:
 * version:
 */
public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private JsBridgeApi mJsBridgeApi;
    static long id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);
        Button button = new Button(this);
        button.setText("Native Call h5 need callback" + id++);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mJsBridgeApi.callH5FromNative(new NativeMessageBean() {
                    {
                        message = "callFromNative('1',1)";
                        messageId = 1;
                    }
                }, new NativeJSCallBack() {
                    @Override
                    public void onResult(String result) {
//                        Log.v("callH5FromNative", "h5 notify native callback");
//                        Toast.makeText(getApplicationContext(),"callH5FromNative  h5 notify native callback" ,Toast.LENGTH_SHORT).show();

                    }
                });
                startActivity(new Intent(WebViewActivity.this, WebViewActivity.class));
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.topMargin = 200;
        FrameLayout viewGroup = (FrameLayout) findViewById(android.R.id.content);
        viewGroup.addView(button, layoutParams);


        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        mJsBridgeApi = new JsBridgeApi(webView, new IJsCallBack() {
            @Override
            public void onJsCall(JsMessageBean jsMessageBean) {
//                Log.v("onJsCall", JsonUtil.toJsonString(jsMessageBean));
//                Toast.makeText(getApplicationContext(),"js call native "+JsonUtil.toJsonString(jsMessageBean),Toast.LENGTH_SHORT).show();
                mJsBridgeApi.notifyNativeTaskFinished("sf", jsMessageBean.id);
            }
        });

        webView.setWebContentsDebuggingEnabled(true);
        webView.setWebChromeClient(new JsWebChromeClient(mJsBridgeApi));
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/main.html");


    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mJsBridgeApi.destroy();
        if (webView != null) {
            if (webView.getParent() instanceof ViewGroup) {
                ((ViewGroup) webView.getParent()).removeView(webView);
            }
//            webView.destroy();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        webView.onResume();
    }
}
