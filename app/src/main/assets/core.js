
 var jsRPCTag = 'jsRPC';
 var jsRPCResultTag = 'result';
 var jsRPCErrorTag = 'error';
 var jsRPCIdTag = 'id';
 var jsRPCVer = '1.0';


 var _current_id = 0;

 var _callbacks = {};

 var jsRPC = {};


 function doClose() {
     delete window.jsbridge;
 }

 function callNative(method, params, success_cb, error_cb) {

     var request = {
         version: jsRPCVer,
         method: method,
         params: params,
         id: _current_id++
     };

     if (typeof success_cb !== 'undefined') {
         _callbacks[request.id] = {
             success_cb: success_cb,
             error_cb: error_cb
         };
     }
    prompt(JSON.stringify(request));
 };


 jsRPC.onJsCallFinished = function(message) {
     var response = message;

     if (typeof response === 'object' &&
         jsRPCTag in response &&
         response.jsRPC === jsRPCVer) {
         if (jsRPCResultTag in response && _callbacks[response.id]) {
             var success_cb = _callbacks[response.id].success_cb;
             delete _callbacks[response.id];
             success_cb(response.result);
             return;
         } else if (jsRPCErrorTag in response && _callbacks[response.id]) {

             var error_cb = _callbacks[response.id].error_cb;
             delete _callbacks[response.id];
             error_cb(response.error);
             return;
         }
     }
 };

function notifyNativeCallBack(message,messageId ){

}

  <!--可以抽离一个进行封装-->
 window.NEJsbridge = {};
 window.NEJsbridge.invoke = callNative;
 window.NEJsbridge.notifyNative =  notifyNativeCallBack;
 window.jsRPC = jsRPC;