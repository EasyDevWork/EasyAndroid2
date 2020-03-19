package com.easy.store.base;

import android.app.Application;
import android.util.Log;

import com.easy.store.BuildConfig;
import com.easy.store.bean.MyObjectBox;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class DataStore {
    private static BoxStore boxStore;

    private static DataStore instance = new DataStore();

    private DataStore() {
    }

    public static DataStore getInstance() {
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
}
