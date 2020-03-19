package com.easy.store.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class DownloadInfo {
    @Id
    private long id;
    private String localUrl;//本地存储地址
    private String serverUrl;//下载地址
    private long totalSize;//文件大
    private long currentSize;//当前大小
    private int state= 0;//0:无状态 1:等待 2:下载中 3:暂停 4:错误 5:完成

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "id=" + id +
                ", localUrl='" + localUrl + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", totalSize=" + totalSize +
                ", currentSize=" + currentSize +
                ", state=" + state +
                '}';
    }
}
