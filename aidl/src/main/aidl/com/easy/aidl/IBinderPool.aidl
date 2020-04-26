package com.easy.aidl;

interface IBinderPool {

     IBinder queryBinder(in ComponentName name, in IBinder service,int binderCode);

}
