package com.easy.store.base;

import android.app.Application;
import android.util.Log;

import com.easy.store.BuildConfig;
import com.easy.store.bean.MyObjectBox;
import com.easy.store.dao.DownloadInfoDao;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class EasyStore {
    private static BoxStore boxStore;

    private static EasyStore instance = new EasyStore();

    private EasyStore() {
    }

    public static EasyStore getInstance() {
        return instance;
    }

    public void init(Application application) {
        boxStore = MyObjectBox.builder().androidContext(application).build();
        if (BuildConfig.DEBUG) {
            boolean started = new AndroidObjectBrowser(boxStore).start(application);
            Log.i("ObjectBrowser", "Started: " + started + "   " + boxStore.getObjectBrowserPort());
            //adb forward tcp:8090 tcp:8090
        }
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

    public DownloadInfoDao getDownloadDao() {
        return new DownloadInfoDao();
    }
}
