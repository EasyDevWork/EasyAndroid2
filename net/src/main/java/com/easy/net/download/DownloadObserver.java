package com.easy.net.download;


import android.os.Handler;

import com.easy.net.RxDownLoad;
import com.easy.net.tools.ComputeUtils;
import com.easy.store.dao.DownloadInfoDao;

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
    private Handler handler;
    private Disposable disposable;
    private SoftReference<DownloadCallback> downloadCallback;
    DownloadInfoDao downloadInfoDao;

    public void setDownload(Download download) {
        this.download = download;
        this.downloadCallback = new SoftReference<>(download.getCallback());
    }

    public DownloadObserver(Download download, Handler handler, DownloadInfoDao downloadInfoDao) {
        this.download = download;
        this.downloadInfoDao = downloadInfoDao;
        this.handler = handler;
        this.downloadCallback = new SoftReference<>(download.getCallback());
    }

    /**
     * 开始下载/继续下载
     * 备注：继续下载需要获取之前下载的数据
     */
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
        download.getDownloadInfo().setState(1);//等待状态
        downloadInfoDao.insertOrUpdate(download.getDownloadInfo());
        if (downloadCallback.get() != null) {//回调
            float progress = ComputeUtils.getProgress(download.getDownloadInfo().getCurrentSize(), download.getDownloadInfo().getTotalSize());
            downloadCallback.get().onProgress(download.getDownloadInfo().getState(), download.getDownloadInfo().getCurrentSize(), download.getDownloadInfo().getTotalSize(), progress);
        }
    }

    /**
     * 下载出错
     * 备注：回调进度，回调onError
     */
    @Override
    public void onError(Throwable e) {
        download.getDownloadInfo().setState(4);//错误状态
        RxDownLoad.get().removeDownload(download, false);//移除下载
        downloadInfoDao.insertOrUpdate(download.getDownloadInfo());
        if (downloadCallback.get() != null) {
            float progress = ComputeUtils.getProgress(download.getDownloadInfo().getCurrentSize(), download.getDownloadInfo().getTotalSize());
            downloadCallback.get().onProgress(download.getDownloadInfo().getState(), download.getDownloadInfo().getCurrentSize(), download.getDownloadInfo().getTotalSize(), progress);
            downloadCallback.get().onError(e);
        }
    }

    /**
     * 下载完成
     * 备注：将开发者传入的Download子类回传
     */
    @Override
    public void onNext(T t) {
        download.getDownloadInfo().setState(5);//下载完成
        RxDownLoad.get().removeDownload(download, false);//移除下载
        downloadInfoDao.insertOrUpdate(download.getDownloadInfo());
        if (downloadCallback.get() != null) {//回调
            downloadCallback.get().onSuccess(t);
        }
    }

    @Override
    public void onComplete() {
    }


    /**
     * 进度回调
     *
     * @param currentSize 当前值
     * @param totalSize   总大小
     */
    @Override
    public void progress(long currentSize, long totalSize) {
        if (download.getDownloadInfo().getTotalSize() > totalSize) {
            currentSize = download.getDownloadInfo().getTotalSize() - totalSize + currentSize;
        } else {
            download.getDownloadInfo().setTotalSize(totalSize);
        }
        download.getDownloadInfo().setCurrentSize(currentSize);
        handler.post(() -> {
            /*下载进度==总进度修改为完成状态*/
            if ((download.getDownloadInfo().getCurrentSize() == download.getDownloadInfo().getTotalSize()) && (download.getDownloadInfo().getTotalSize() != 0)) {
                download.getDownloadInfo().setState(5);
            }
            /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
            if (download.getDownloadInfo().getState() != 3) {
                float progress = (float) download.getDownloadInfo().getCurrentSize() / (float) download.getDownloadInfo().getTotalSize();
                if (downloadCallback.get() != null) {
                    downloadCallback.get().onProgress(download.getDownloadInfo().getState(), download.getDownloadInfo().getCurrentSize(), download.getDownloadInfo().getTotalSize(), progress);
                }
            }
        });
    }

    /**
     * 取消请求
     * 备注：暂停下载时调用
     */
    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}