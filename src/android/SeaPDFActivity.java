package com.sea.plugins;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import com.github.barteksc.pdfviewer.PDFView;

import org.apache.cordova.CordovaInterface;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import io.cordova.hellocordova.R;

/**
 * Created by administrator on 2017/3/31.
 */

public class SeaPDFActivity extends Activity {
    private static CordovaInterface cordova;
    private static String type;
    private static String filePath;
    private static String fileName;
    private Handler handler;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sea_pdf_activity);
        handler = new Handler();
        if(type.equals("local")){
            localPreview();
        }else if(type.equals("online")){
            onlinePreview();
        }
    }

    private  void localPreview(){
        PDFView pdfView = (PDFView) findViewById(R.id.sea_pdf_preview);
        pdfView.fromAsset(SeaPDFActivity.filePath+"/"+SeaPDFActivity.fileName+".pdf").load();
    }

    private void onlinePreview(){
        showProgressDialog("提示","正在加载，请稍候...");
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Activity activity = SeaPDFActivity.this;
                Context context = activity.getApplicationContext();
                PDFView pdfView = (PDFView) findViewById(R.id.sea_pdf_preview);
                URL url;
                URLConnection conn;
                File file;
                InputStream is;
                BufferedInputStream bis;
                FileOutputStream fos;
                try {
                    url = new URL(SeaPDFActivity.filePath);
                    conn = url.openConnection();
                    conn.setRequestProperty("accept","*/*");
                    conn.setRequestProperty("connection","Keep-Alive");
                    conn.setRequestProperty("user-agent","Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
                    conn.setDoInput(true);
                    conn.setConnectTimeout(30000);
                    conn.connect();
                    file = new File(context.getFilesDir(),context.getString(R.string.sea_pdf_file_name));
                    is = conn.getInputStream();
                    bis = new BufferedInputStream(is);
                    fos = new FileOutputStream(file);
                    int length=0;
                    int total = 0;
                    byte[] bytes = new byte[3072];
                    while((length=bis.read(bytes))!=-1){
                        fos.write(bytes,0,length);
                        total+=length;
                    }
                    fos.flush();
                    is.close();
                    bis.close();
                    fos.close();
                    pdfView.fromFile(new File(context.getFilesDir(),context.getString(R.string.sea_pdf_file_name))).load();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDialog();
                        }
                    });
                }catch (final Exception e){
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            SeaPDFActivity.this.hideProgressDialog();
                            SeaPDFActivity.this.showErrorAlert(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog(String title,String message){
        if(progressDialog==null){
            progressDialog = ProgressDialog.show(this,title,message,true,false);
        }else{
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    private void hideProgressDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    private void showErrorAlert(String errorMsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("出错了");
        builder.setMessage(errorMsg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SeaPDFActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static CordovaInterface getCordova() {
        return cordova;
    }

    public static void setCordova(CordovaInterface cordova) {
        SeaPDFActivity.cordova = cordova;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        SeaPDFActivity.type = type;
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        SeaPDFActivity.filePath = filePath;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        SeaPDFActivity.fileName = fileName;
    }
}
