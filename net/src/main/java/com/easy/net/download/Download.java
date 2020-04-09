package com.easy.net.download;

import com.easy.net.api.HttpApi;

/**
 * 下载实体类
 * 备注:用户使用下载类需要继承此类
 */

public class Download {

    DownloadInfo downloadInfo;
    private HttpApi api;//接口service
    private DownloadCallback callback;//回调接口
    public DownloadInfo getDownloadInfo() {
        return downloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
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

    @Override
    public String toString() {
        return "Download{" +
                "downloadInfo=" + downloadInfo +
                ", api=" + api +
                ", callback=" + callback +
                '}';
    }
}
