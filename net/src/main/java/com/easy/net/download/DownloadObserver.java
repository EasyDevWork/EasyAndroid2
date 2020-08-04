package com.easy.net.download;


import android.util.Log;

import com.easy.net.RxDownLoad;
import com.easy.net.tools.ComputeUtils;
import com.easy.store.bean.DownloadDo;
import com.easy.store.dao.DownloadDao;

import java.lang.ref.SoftReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * 下载观察者(监听)
 * 备注:在此处监听: 开始下载 、下载错误 、下载完成  等状态
 */
public class DownloadObserver<T extends Download> implements DownloadProgressCallback, Observer<T> {

    private Download download;
    DownloadDo downloadDo;
    private Disposable disposable;
    private SoftReference<DownloadCallback> downloadCallback;
    DownloadDao downloadDao;
    boolean isFinish;//是否结束；用户停止下载进度更新
    long lastDownloadSize;//上一次下载的大小

    public void setDownload(Download download) {
        this.download = download;
        this.downloadDo = download.getDownloadDo();
        this.downloadCallback = new SoftReference<>(download.getCallback());
    }

    public DownloadObserver(Download download, DownloadDao downloadDao) {
        this.download = download;
        this.downloadDo = download.getDownloadDo();
        this.downloadDao = downloadDao;
        this.downloadCallback = new SoftReference<>(download.getCallback());
    }

    /**
     * 开始下载/继续下载
     * 备注：继续下载需要获取之前下载的数据
     */
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
        isFinish = false;
        Log.d("DownloadObserver", "onSubscribe:"+isFinish);
        downloadDo.setState(1);//等待状态
        downloadDao.insertOrUpdate(downloadDo);
        if (downloadCallback.get() != null) {//回调
            float progress = ComputeUtils.getProgress(downloadDo.getCurrentSize(), downloadDo.getTotalSize());
            downloadCallback.get().onProgress(downloadDo.getState(), downloadDo.getCurrentSize(), downloadDo.getTotalSize(), progress);
        }
    }

    /**
     * 下载出错
     * 备注：回调进度，回调onError
     */
    @Override
    public void onError(Throwable e) {
        isFinish = true;
        Log.d("DownloadObserver", "onError");
        downloadDo.setState(4);//错误状态
        RxDownLoad.get().removeDownload(download, false);//移除下载
        downloadDao.insertOrUpdate(downloadDo);
        if (downloadCallback.get() != null) {
            float progress = ComputeUtils.getProgress(downloadDo.getCurrentSize(), downloadDo.getTotalSize());
            downloadCallback.get().onProgress(downloadDo.getState(), downloadDo.getCurrentSize(), downloadDo.getTotalSize(), progress);
            downloadCallback.get().onError(e);
        }
    }

    /**
     * 下载完成
     * 备注：将开发者传入的Download子类回传
     */
    @Override
    public void onNext(T t) {
        downloadDo.setState(5);//下载完成
        RxDownLoad.get().removeDownload(download, false);//移除下载
        downloadDao.insertOrUpdate(downloadDo);
        if (downloadCallback.get() != null) {//回调
            downloadCallback.get().onSuccess(t);
        }
    }

    @Override
    public void onComplete() {
        isFinish = true;
        Log.d("DownloadObserver", "onComplete");
        DownloadCallback callback = downloadCallback.get();
        if (callback != null) {
            callback.onProgress(downloadDo.getState(), downloadDo.getCurrentSize(), downloadDo.getTotalSize(), 100);
        }
    }


    /**
     * 进度回调
     *
     * @param currentSize 当前值
     * @param totalSize   总大小
     */
    @Override
    public void progress(long currentSize, long totalSize) {
        if (downloadDo.getTotalSize() > totalSize) {
            currentSize = downloadDo.getTotalSize() - totalSize + currentSize;
        } else {
            downloadDo.setTotalSize(totalSize);
        }
        downloadDo.setCurrentSize(currentSize);
        /*下载进度==总进度修改为完成状态*/
        if ((downloadDo.getCurrentSize() == downloadDo.getTotalSize()) && (downloadDo.getTotalSize() != 0)) {
            downloadDo.setState(5);
        }
        /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
        if (downloadDo.getState() != 3) {
            float progress = (float) downloadDo.getCurrentSize() / (float) downloadDo.getTotalSize();
            DownloadCallback callback = downloadCallback.get();
            if (callback != null) {
                callback.onProgress(downloadDo.getState(), downloadDo.getCurrentSize(), downloadDo.getTotalSize(), progress);
                long growSize = downloadDo.getCurrentSize() - lastDownloadSize;
                long speed = (long) (growSize / (DownloadResponseBody.delayedTime / 1000f));
                callback.onSpeedToSend(speed);
                lastDownloadSize = downloadDo.getCurrentSize();
            }
        }
    }

    @Override
    public boolean isFinish() {
        return isFinish;
    }


    /**
     * 取消请求
     * 备注：暂停下载时调用
     */
    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        isFinish = true;
        Log.d("DownloadObserver", "dispose");
    }

}