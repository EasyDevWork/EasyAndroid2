// IBaseAidlInterface.aidl
package com.easy.aidl;


interface IBaseAidlInterface {

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,double aDouble, String aString);

    int getInt();
    long getLong();
    boolean getBoolean();
    float getFloat();
    double getDouble();
    String getString();
}
