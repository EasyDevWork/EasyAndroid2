package com.easy.net;

import com.easy.net.download.IDownload;
import com.easy.net.retrofit.RetrofitUtils;

public class EasyNet {

    public static void init(RetrofitConfig.Builder builder, IDownload downloadDao) {
        RetrofitUtils.get().initRetrofit(builder.build());
        RxDownLoad.get().init(downloadDao);
    }
}
