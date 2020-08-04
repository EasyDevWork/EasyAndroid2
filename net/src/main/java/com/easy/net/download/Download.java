package com.easy.net.download;

import com.easy.net.api.HttpApi;
import com.easy.store.bean.DownloadDo;

/**
 * 下载实体类
 * 备注:用户使用下载类需要继承此类
 */

public class Download {

    DownloadDo downloadDo;
    private HttpApi api;//接口service
    private DownloadCallback callback;//回调接口

    public DownloadDo getDownloadDo() {
        return downloadDo;
    }

    public void setDownloadDo(DownloadDo downloadDo) {
        this.downloadDo = downloadDo;
    }

    public HttpApi getApi() {
        return api;
    }

    public void setApi(HttpApi api) {
        this.api = api;
    }

    public DownloadCallback getCallback() {
        return callback;
    }

    public void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }

}