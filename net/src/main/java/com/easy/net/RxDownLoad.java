package com.easy.net;

import android.os.Handler;
import android.os.Looper;

import com.easy.net.api.HttpApi;
import com.easy.net.download.Download;
import com.easy.net.download.IDownload;
import com.easy.net.download.DownloadInterceptor;
import com.easy.net.download.DownloadObserver;
import com.easy.net.retrofit.RetrofitUtils;
import com.easy.net.tools.ComputeUtils;
import com.easy.net.tools.RequestUtils;
import com.easy.net.tools.ResponseUtils;
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
    /*下载集合*/
    private Set<Download> downloadSet;
    /*下载集合对应回调map*/
    private HashMap<String, DownloadObserver> callbackMap;
    /*Handler 回调下载进度到主线程*/
    private Handler handler;
    IDownload iDownload;

    private RxDownLoad() {
        downloadSet = new HashSet<>();
        callbackMap = new HashMap<>();
        handler = new Handler(Looper.getMainLooper());
    }

    public void init(IDownload iDownload) {
        this.iDownload = iDownload;
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
     * @param download
     */
    public void startDownload(final Download download) {
        if (download == null) return;

        /*正在下载不处理*/
        if (callbackMap.get(download.getDownloadInfo().getTag()) != null) {
            callbackMap.get(download.getDownloadInfo().getTag()).setDownload(download);
            return;
        }

        /*已完成下载*/
        if (download.getDownloadInfo().getCurrentSize() == download.getDownloadInfo().getTotalSize()
                && (download.getDownloadInfo().getTotalSize() != 0)) {
            return;
        }
        Logger.d("RHttp startDownload:" + download.getDownloadInfo().getServerUrl());
        /*判断本地文件是否存在*/
        boolean isFileExists = ComputeUtils.isFileExists(download.getDownloadInfo().getLocalUrl());
        if (!isFileExists && download.getDownloadInfo().getCurrentSize() > 0) {
            download.getDownloadInfo().setCurrentSize(0);
        }

        DownloadObserver observer = new DownloadObserver(download, handler, iDownload);
        callbackMap.put(download.getDownloadInfo().getTag(), observer);
        HttpApi httpApi;
        if (downloadSet.contains(download)) {
            httpApi = download.getApi();
        } else {
            //下载拦截器
            DownloadInterceptor downloadInterceptor = new DownloadInterceptor(observer);
            //Retrofit
            Retrofit retrofit = RetrofitUtils.get().getRetrofitDownload(RequestUtils.getBasUrl(download.getDownloadInfo().getServerUrl()), downloadInterceptor);
            httpApi = retrofit.create(HttpApi.class);
            download.setApi(httpApi);
            downloadSet.add(download);
        }

        /*下载时添加 headerMap 暂时只获取 baseHeader 可扩展 startDownload() 参数形式，或者添加 Download 属性 记得添加到 headerMap*/
        Map<String, Object> headerMap = new HashMap<>();
        /* RANGE 断点续传下载 */
        //数据变换
        httpApi.download("bytes=" + download.getDownloadInfo().getCurrentSize() + "-", download.getDownloadInfo().getServerUrl(), headerMap)
                .subscribeOn(Schedulers.io())
                .map((Function<ResponseBody, Object>) responseBody -> {
                            download.getDownloadInfo().setState(2);//下载中状态
                            iDownload.insertOrUpdate(download.getDownloadInfo());
                            //写入文件
                            ResponseUtils.get().download2LocalFile(responseBody, new File(download.getDownloadInfo().getLocalUrl()), download);
                            return download;
                        }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 暂停/停止下载数据
     *
     * @param download
     */
    public void stopDownload(Download download) {

        if (download == null) return;
        Logger.d("RHttp stopDownload:" + download.getDownloadInfo().getServerUrl());
        /**
         * 1.暂停网络数据
         * 2.设置数据状态
         * 3.更新数据库
         */

        /*1.暂停网络数据*/
        if (callbackMap.containsKey(download.getDownloadInfo().getTag())) {
            DownloadObserver observer = callbackMap.get(download.getDownloadInfo().getTag());
            observer.dispose();//取消
            callbackMap.remove(download.getDownloadInfo().getTag());
        }

        /*2.设置数据状态*/
        download.getDownloadInfo().setState(3);//暂停状态
        float progress = ComputeUtils.getProgress(download.getDownloadInfo().getCurrentSize(), download.getDownloadInfo().getTotalSize());//计算进度
        download.getCallback().onProgress(download.getDownloadInfo().getState(), download.getDownloadInfo().getCurrentSize(), download.getDownloadInfo().getTotalSize(), progress);//回调

        /*3.更新数据库*/
        iDownload.insertOrUpdate(download.getDownloadInfo());
    }

    /**
     * 移除下载数据
     *
     * @param download
     * @param removeFile 是否移出本地文件
     */
    public void removeDownload(Download download, boolean removeFile) {

        if (download == null) return;
        Logger.d("RHttp removeDownload:" + download.getDownloadInfo().getServerUrl());
        //未完成下载时,暂停再移除
        if (download.getDownloadInfo().getState() != 5) {
            stopDownload(download);
        }
        //移除本地保存数据
        if (removeFile) {
            ComputeUtils.deleteFile(download.getDownloadInfo().getLocalUrl());
        }

        callbackMap.remove(download.getDownloadInfo().getTag());
        downloadSet.remove(download);

        //移除数据
        iDownload.delete(download.getDownloadInfo());
    }

    /**
     * 暂停/停止全部下载数据
     */
    public void stopAllDownload() {
        for (Download download : downloadSet) {
            stopDownload(download);
        }
        callbackMap.clear();
        downloadSet.clear();
    }
}
