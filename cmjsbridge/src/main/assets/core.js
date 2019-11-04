 var jsRPCTag = 'jsonrpc';
 var jsRPCResultTag = 'result';
 var jsRPCErrorTag = 'error';
 var jsRPCIdTag = 'id';
 var jsRPCVer = '1.0';


 var _current_id = 0;

 var _callbacks = {};

 var jsonRPC = {};


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
     
    JsMethodApi.callNative(JSON.stringify(request));

 };


 jsonRPC.onMessage = function(message) {
     var response = message;

     if (typeof response === 'object' &&
         jsRPCTag in response &&
         response.jsonrpc === jsRPCVer) {
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


 window.NEJsbridge = {};
 window.NEJsbridge.invoke = callNative;
 window.jsonRPC = jsonRPC;