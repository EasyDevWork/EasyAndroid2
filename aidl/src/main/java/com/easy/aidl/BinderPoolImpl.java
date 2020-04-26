package com.easy.aidl;

import android.os.IBinder;
import android.os.RemoteException;

public class BinderPoolImpl extends IBinderPool.Stub {

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        switch (binderCode) {
            case 100:
                return new BookControllerImpl();
            case 200:
                return new FoodControllerImpl();
            default:
        }
        return null;
    }
}
