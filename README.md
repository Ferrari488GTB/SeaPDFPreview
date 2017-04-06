# SeaPDFPreview：预览PDF文件。
## 简介
   这个插件定义了一个全局对象cordova.plugins.seaPDFPreview，可以用来预览PDF文件。尽管这个对象在全局作用域中，但直到deviceready事件触发后，这个对象才能使用。
```javascript
document.addEventListener("deviceready", onDeviceReady, false);
function onDeviceReady() {
console.log(cordova.plugins.seaPDFPreview);
}
```
## 安装方法
```javascript
cordova plugin add cordova-plugin-sea-pdf-preview
```
## 支持平台
* Android
* iOS

## 回调函数
* 调用成功，返回json对象，有code、msg两个字段。
* 调用失败，返回字符串。

## 本地预览PDF
   Android平台PDF文件存放在assets文件夹下，iOS平台文件存放在根目录下。
```javascript
// assets/www/test.pdf
cordova.plugins.seaPDFPreview.preview(
{
type :"local",
filePath : "www",
fileName : "test"
},
function(data){

},
function(errorMsg){
alert(errorMsg);
}
);
```
## 在线预览PDF
```javascript
cordova.plugins.seaPDFPreview.preview(
{
type : "online",
filePath : "http://120.27.188.216:21003/Ferrari.pdf"
},
function(data){
myApp.alert(data.code+"---"+data.msg);
},
function(errorMsg){
myApp.alert(errorMsg);
}
);
```
