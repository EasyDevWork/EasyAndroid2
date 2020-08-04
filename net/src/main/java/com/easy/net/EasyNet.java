package com.easy.net;

import com.easy.net.retrofit.RetrofitConfig;
import com.easy.net.retrofit.RetrofitHelp;

public class EasyNet {

    public static void init(RetrofitConfig.Builder builder) {
        RetrofitHelp.get().initRetrofit(builder.build());
    }
}
