
 function callFromNative(message,messageId ){

 console.log('call from native');
  window.Jsbridge.notifyNative('callback ',messageId);
 }