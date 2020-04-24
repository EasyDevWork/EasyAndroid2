package com.easy.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyRemoteService extends Service {
    IBaseAidlInterface.Stub iStub = new BaseDataType();

    public MyRemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iStub;
    }
}
