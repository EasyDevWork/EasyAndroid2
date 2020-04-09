package com.easy.framework.manager.network;

public interface INetStateChange {

    void onNetDisconnected();

    void onNetConnected(NetworkType networkType);
}
