package com.easy.framework.manager.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetStateChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            notifyObservers(NetworkManager.getNetworkType());
        }
    }

    private void notifyObservers(NetworkType networkType) {
        if (networkType == NetworkType.NETWORK_NO) {
            for (INetStateChange observer : NetworkManager.getInstance().iNetStateChanges) {
                observer.onNetDisconnected();
            }
        } else {
            for (INetStateChange observer : NetworkManager.getInstance().iNetStateChanges) {
                observer.onNetConnected(networkType);
            }
        }
    }
}
