package com.easy.net;

import com.easy.net.download.IDownload;

public class EasyNet {

    public static void init(IDownload downloadDao) {
        RxDownLoad.get().init(downloadDao);
    }
}
