var exec = require('cordova/exec');

var SeaPDFPreview = function(){
	
};

SeaPDFPreview.prototype.preview = function(arg0, success, error){
	exec(success, error, "SeaPDFPreview", "preview", [arg0]);
};

module.exports = new SeaPDFPreview();