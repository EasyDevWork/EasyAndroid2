package com.easy.framework.network;

public interface INetStateChange {

    void onNetDisconnected();

    void onNetConnected(NetworkType networkType);
}
