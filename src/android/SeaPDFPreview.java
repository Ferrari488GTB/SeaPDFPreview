package com.sea.plugins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * This class echoes a string called from JavaScript.
 */
public class SeaPDFPreview extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        JSONObject json = args.getJSONObject(0);
        if (action.equals("preview")) {
            this.preview(json, callbackContext);
            return true;
        }
        return false;
    }

    private void preview(JSONObject json,CallbackContext callbackContext){
        String type;
        String filePath;
        String fileName;
        JSONObject result = new JSONObject();
        String errorMsg;
        Activity activity;
        Intent intent;
        try{
            type = json.getString("type");
            filePath = json.getString("filePath");
            fileName = json.has("fileName")?json.getString("fileName"):null;
            if(type!=null&&type.length()>0){
                if(filePath!=null&&filePath.length()>0){
                    if((type.equals("local")&&fileName!=null&&fileName.length()>0)||type.equals("online")){
                        result.put("code","1");
                        result.put("msg","正在预览");
                        callbackContext.success(result);
                        SeaPDFActivity.setCordova(cordova);
                        SeaPDFActivity.setType(type);
                        SeaPDFActivity.setFilePath(filePath);
                        SeaPDFActivity.setFileName(fileName);
                        activity = cordova.getActivity();
                        intent = new Intent(activity,SeaPDFActivity.class);
                        activity.startActivity(intent);
                        return;
                    }else{
                        errorMsg = "PDF文件名不能为空";
                    }
                }else{
                    errorMsg = "PDF路径不能为空";
                }
            }else{
                errorMsg = "预览类型不能为空";
            }
        }catch (Exception e){
            e.printStackTrace();
            errorMsg = e.getMessage();
        }
        callbackContext.error(errorMsg);
    }

}
