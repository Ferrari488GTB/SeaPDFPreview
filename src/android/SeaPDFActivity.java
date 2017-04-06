package com.sea.plugins;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import org.apache.cordova.CordovaInterface;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by administrator on 2017/3/31.
 */

public class SeaPDFActivity extends Activity {
    private Class layoutClass;
    private Class idClass;
    private int activityId;
    private int pdfViewId;
    private static CordovaInterface cordova;
    private static String type;
    private static String filePath;
    private static String fileName;
    private Handler handler;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            layoutClass = Class.forName(this.getApplicationContext().getPackageName()+".R$layout");
            activityId = layoutClass.getDeclaredField("sea_pdf_activity").getInt(null);
            idClass = Class.forName(this.getApplicationContext().getPackageName()+".R$id");
            pdfViewId = idClass.getDeclaredField("sea_pdf_preview").getInt(null);
            setContentView(activityId);
            handler = new Handler();
            if(type.equals("local")){
                localPreview();
            }else if(type.equals("online")){
                onlinePreview();
            }
        }catch (Exception e){
            e.printStackTrace();
            showErrorAlert(e.getMessage());
        }
    }

    private  void localPreview(){
        try{
            PDFView pdfView = (PDFView) findViewById(pdfViewId);
            pdfView.fromAsset(SeaPDFActivity.filePath+"/"+SeaPDFActivity.fileName+".pdf").load();
        }catch(Exception e){
            e.printStackTrace();
            showErrorAlert(e.getMessage());
        }
    }

    private void onlinePreview(){
        showProgressDialog("提示","正在加载，请稍候...");
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                PDFView pdfView = (PDFView) findViewById(pdfViewId);
                URL url;
                URLConnection conn;
                try {
                    url = new URL(SeaPDFActivity.filePath);
                    conn = url.openConnection();
                    conn.setRequestProperty("accept","*/*");
                    conn.setRequestProperty("connection","Keep-Alive");
                    conn.setRequestProperty("user-agent","Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
                    conn.setDoInput(true);
                    conn.setConnectTimeout(30000);
                    conn.connect();
                    pdfView
                            .fromStream(conn.getInputStream())
                            .onLoad(new OnLoadCompleteListener() {
                                @Override
                                public void loadComplete(int nbPages) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgressDialog();
                                        }
                                    });
                                }
                            })
                            .onError(new OnErrorListener() {
                                @Override
                                public void onError(final Throwable t) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            SeaPDFActivity.this.hideProgressDialog();
                                            SeaPDFActivity.this.showErrorAlert(t.getMessage());
                                        }
                                    });
                                }
                            })
                            .load();
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
