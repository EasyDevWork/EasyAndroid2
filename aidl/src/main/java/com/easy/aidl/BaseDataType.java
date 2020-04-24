package com.easy.aidl;

import android.os.RemoteException;

public class BaseDataType extends IBaseAidlInterface.Stub {
    int anInt;
    long aLong;
    boolean aBoolean;
    float aFloat;
    double aDouble;
    String aString;

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
        this.anInt = anInt;
        this.aLong = aLong;
        this.aBoolean = aBoolean;
        this.aFloat = aFloat;
        this.aDouble = aDouble;
        this.aString = aString;
    }
    @Override
    public int getInt() throws RemoteException {
        return anInt;
    }
    @Override
    public long getLong() throws RemoteException {
        return aLong;
    }
    @Override
    public boolean getBoolean() throws RemoteException {
        return aBoolean;
    }
    @Override
    public float getFloat() throws RemoteException {
        return aFloat;
    }
    @Override
    public double getDouble() throws RemoteException {
        return aDouble;
    }
    @Override
    public String getString() throws RemoteException {
        return aString;
    }
}
