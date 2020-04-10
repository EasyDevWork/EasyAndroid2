package com.easy.net;

import com.easy.net.download.IDownload;
import com.easy.net.retrofit.RetrofitUtils;

public class EasyNet {

    public static void init(RetrofitConfig.Builder builder) {
        RetrofitUtils.get().initRetrofit(builder.build());
    }

    public static void initDownload(IDownload downloadDao) {
        RxDownLoad.get().init(downloadDao);
    }
}
