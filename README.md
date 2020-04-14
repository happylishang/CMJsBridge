# CMJsBridge
CMJsBridge

使用方法：前端注入core.js，这样前端什么时候用都可以，客户端对于想用的用的webview自己open就可以，不使用的时候需要主动释放destroy

>前端
	<head>
	    <link rel="stylesheet" href="main.css">
	    <script src="jquery-3.1.0.js"></script>
	    <script type="text/javascript" src="core.js"></script>
	    <script type="text/javascript" src="jsInterface.js"></script>
	
	    <script type="text/javascript"  >
	            window.Jsbridge.invoke('头部就可以回调');
	
	    </script>
	
	
	</head>


>Android客户端

1、build.gradle
    
    implementation 'com.snail:cmjsbridge:1.0'


2、代码中

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);
       
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mJsBridgeApi = new JsBridgeApi(webView, new IJsCallBack() {
            @Override
            public void onJsCall(JsMessageBean jsMessageBean) {
 
                mJsBridgeApi.notifyNativeTaskFinished("sf", jsMessageBean.id);
            }
        });
        mJsBridgeApi.openJsBridgeChannel(webView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mJsBridgeApi.destroy();
        if (webView != null) {
            if (webView.getParent() instanceof ViewGroup) {
                ((ViewGroup) (webView.getParent())).removeView(webView);
            }
            webView.destroy();
        }
    }
    
