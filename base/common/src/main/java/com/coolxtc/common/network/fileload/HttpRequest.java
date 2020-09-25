package com.coolxtc.common.network.fileload;


import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.File;

/**
 * 描 述:
 *
 * @author: lihui
 * @date: 2016-03-07 16:14
 */
public class HttpRequest {

    /**
     * 下载文件
     * @param url
     * @param target 保存的文件
     * @param callback
     */
    public static void download(String url, File target, FileDownloadCallback callback) {
        if (!TextUtils.isEmpty(url) && target != null) {
            FileDownloadTask task = new FileDownloadTask(url, target, callback);
            try{
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
