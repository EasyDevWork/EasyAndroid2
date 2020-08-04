package com.easy.net;

import android.os.Handler;
import android.os.Looper;

import com.easy.net.api.HttpApi;
import com.easy.net.download.Download;
import com.easy.net.download.DownloadCallback;
import com.easy.net.download.DownloadInterceptor;
import com.easy.net.download.DownloadObserver;
import com.easy.net.retrofit.RetrofitHelp;
import com.easy.net.tools.ComputeUtils;
import com.easy.net.tools.RequestUtils;
import com.easy.net.tools.ResponseUtils;
import com.easy.store.bean.DownloadDo;
import com.easy.store.dao.DownloadDao;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;


/**
 * 下载管理类
 * 备注：单例模式 开始下载，暂停下载，暂停全部，移除下载，获取下载列表
 */
public class RxDownLoad {
    /*单例模式*/
    private volatile static RxDownLoad instance;
    /*下载集合对应回调map*/
    private HashMap<String, DownloadObserver> callbackMap;
    /*Handler 回调下载进度到主线程*/
    private Handler handler;
    DownloadDao downloadDao;

    private RxDownLoad() {
        callbackMap = new HashMap<>();
        handler = new Handler(Looper.getMainLooper());
        downloadDao = new DownloadDao();
    }

    public static RxDownLoad get() {
        if (instance == null) {
            synchronized (RxDownLoad.class) {
                if (instance == null) {
                    instance = new RxDownLoad();
                }
            }
        }
        return instance;
    }

    /**
     * 开始下载
     *
     * @param downloadDo
     */
    public Download startDownload(DownloadDo downloadDo, DownloadCallback downloadCallback) {
        if (downloadDo == null) return null;
        Download download = new Download();
        download.setDownloadDo(downloadDo);
        download.setCallback(downloadCallback);

        /*正在下载不处理*/
        DownloadObserver downloadObserver = callbackMap.get(download.getDownloadDo().getTag());
        if (downloadObserver != null) {
            downloadObserver.setDownload(download);
            return download;
        }

        /*已完成下载*/
        if (download.getDownloadDo().getCurrentSize() == download.getDownloadDo().getTotalSize()
                && (download.getDownloadDo().getTotalSize() != 0)) {
            return download;
        }

        Logger.d("RHttp startDownload:" + download.getDownloadDo().getServerUrl());

        /*判断本地文件是否存在*/
        boolean isFileExists = ComputeUtils.isFileExists(download.getDownloadDo().getLocalUrl());
        if (!isFileExists && download.getDownloadDo().getCurrentSize() > 0) {
            download.getDownloadDo().setCurrentSize(0);
        }

        DownloadObserver observer = new DownloadObserver(download, downloadDao);
        callbackMap.put(download.getDownloadDo().getTag(), observer);
        //下载拦截器
        DownloadInterceptor downloadInterceptor = new DownloadInterceptor(observer, handler);
        //Retrofit
        Retrofit retrofit = RetrofitHelp.get().getRetrofitDownload(RequestUtils.getBasUrl(download.getDownloadDo().getServerUrl()), downloadInterceptor);
        HttpApi httpApi = retrofit.create(HttpApi.class);
        download.setApi(httpApi);

        httpDownload(download, observer);
        return download;
    }

    private void httpDownload(Download download, DownloadObserver observer) {
        if (download == null || observer == null) {
            return;
        }
        /*下载时添加 headerMap 暂时只获取 baseHeader 可扩展 startDownload() 参数形式，或者添加 Download 属性 记得添加到 headerMap*/
        Map<String, Object> headerMap = new HashMap<>();
        /* RANGE 断点续传下载 */
        //数据变换
        download.getApi().download("bytes=" + download.getDownloadDo().getCurrentSize() + "-", download.getDownloadDo().getServerUrl(), headerMap)
                .subscribeOn(Schedulers.io())
                .map((Function<ResponseBody, Object>) responseBody -> {
                            download.getDownloadDo().setState(2);//下载中状态
                            downloadDao.insertOrUpdate(download.getDownloadDo());
                            //写入文件
                            ResponseUtils.get().download2LocalFile(responseBody, new File(download.getDownloadDo().getLocalUrl()), download);
                            return download;
                        }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 继续下载
     *
     * @param download
     */
    public void continueDownload(Download download) {
        if (download == null) return;
        DownloadObserver downloadObserver = callbackMap.get(download.getDownloadDo().getTag());
        if (downloadObserver == null) {
            downloadObserver = new DownloadObserver(download, downloadDao);
            callbackMap.put(download.getDownloadDo().getTag(), downloadObserver);
        }
        DownloadInterceptor downloadInterceptor = new DownloadInterceptor(downloadObserver, handler);
        Retrofit retrofit = RetrofitHelp.get().getRetrofitDownload(RequestUtils.getBasUrl(download.getDownloadDo().getServerUrl()), downloadInterceptor);
        HttpApi httpApi = retrofit.create(HttpApi.class);
        download.setApi(httpApi);

        httpDownload(download, downloadObserver);
    }

    /**
     * 暂停/停止下载数据
     *
     * @param download
     */
    public void stopDownload(Download download) {

        if (download == null) return;
        Logger.d("RHttp stopDownload:" + download.getDownloadDo().getServerUrl());
        /**
         * 1.暂停网络数据
         * 2.设置数据状态
         * 3.更新数据库
         */

        /*1.暂停网络数据*/
        if (callbackMap.containsKey(download.getDownloadDo().getTag())) {
            DownloadObserver observer = callbackMap.get(download.getDownloadDo().getTag());
            observer.dispose();//取消
            callbackMap.remove(download.getDownloadDo().getTag());
        }

        /*2.设置数据状态*/
        download.getDownloadDo().setState(3);//暂停状态
        float progress = ComputeUtils.getProgress(download.getDownloadDo().getCurrentSize(), download.getDownloadDo().getTotalSize());//计算进度
        download.getCallback().onProgress(download.getDownloadDo().getState(), download.getDownloadDo().getCurrentSize(), download.getDownloadDo().getTotalSize(), progress);//回调

        /*3.更新数据库*/
        downloadDao.insertOrUpdate(download.getDownloadDo());
    }

    /**
     * 移除下载数据
     *
     * @param download
     * @param removeFile 是否移出本地文件
     */
    public void removeDownload(Download download, boolean removeFile) {

        if (download == null) return;
        Logger.d("RHttp removeDownload:" + download.getDownloadDo().getServerUrl());
        //未完成下载时,暂停再移除
        if (download.getDownloadDo().getState() != 5) {
            stopDownload(download);
        }
        //移除本地保存数据
        if (removeFile) {
            ComputeUtils.deleteFile(download.getDownloadDo().getLocalUrl());
        }

        callbackMap.remove(download.getDownloadDo().getTag());

        //移除数据
        downloadDao.delete(download.getDownloadDo());
    }

    /**
     * 暂停/停止全部下载数据
     */
    public void stopAllDownload() {
        callbackMap.clear();
    }
}
