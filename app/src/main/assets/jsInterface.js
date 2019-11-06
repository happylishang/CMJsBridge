
 function callFromNative(message,messageId ){

 console.log('call from native');
  window.NEJsbridge.notifyNative('callback ',messageId);
 }