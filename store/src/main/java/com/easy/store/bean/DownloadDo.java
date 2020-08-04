package com.easy.store.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class DownloadDo {
    @Id
    private long id;
    private String fileName;//文件名
    /**
     * link{@com.easy.utils.base.FileConstant}
     */
    private int type;//0:文本 1：图片 2：音频 3：视频 4：apk 5:其他
    private String icon;
    private String localUrl;//本地存储地址
    private String serverUrl;//下载地址
    private long totalSize;//文件大
    private long currentSize;//当前大小
    private int state = 0;//0:无状态 1:等待 2:下载中 3:暂停 4:错误 5:完成
    private String tag = System.currentTimeMillis() + "_" + Math.random();//下载标记
    private String packageName;//如果是Apk 有包名
    private String downloadTime;//下载完成时间

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
        return "DownloadDo{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", type=" + type +
                ", icon='" + icon + '\'' +
                ", localUrl='" + localUrl + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", totalSize=" + totalSize +
                ", currentSize=" + currentSize +
                ", state=" + state +
                ", tag='" + tag + '\'' +
                ", packageName='" + packageName + '\'' +
                ", downloadTime='" + downloadTime + '\'' +
                '}';
    }
}
