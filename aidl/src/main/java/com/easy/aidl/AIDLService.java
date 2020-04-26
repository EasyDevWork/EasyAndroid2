package com.easy.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AIDLService extends Service {
    BinderPoolImpl binderPool;

    public AIDLService() {
        binderPool = new BinderPoolImpl();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binderPool;
    }
}
