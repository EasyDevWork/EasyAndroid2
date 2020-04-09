package com.easy.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetUtils {

    /**
     * 静态判断是不是4G网络
     *
     * @param context 上下文
     * @return 是否是4G
     */
    public static boolean is4GConnected(Context context) {
        //获取手机的连接服务管理器，这里是连接管理器类
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission")
        NetworkInfo mobileNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo.State mobileState = NetworkInfo.State.UNKNOWN;
        if (mobileNetworkInfo != null) {
            mobileState = mobileNetworkInfo.getState();
        }
        return NetworkInfo.State.CONNECTED == mobileState;
    }

    /**
     * 静态方法获取是否有网络连接
     *
     * @param context 上下文
     * @return 是否连接
     */
    public static boolean hasNet(Context context) {
        //获取手机的连接服务管理器，这里是连接管理器类
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        NetworkInfo.State wifiState = NetworkInfo.State.UNKNOWN;
        NetworkInfo.State mobileState = NetworkInfo.State.UNKNOWN;

        if (wifiNetworkInfo != null) {
            wifiState = wifiNetworkInfo.getState();
        }
        if (mobileNetworkInfo != null) {
            mobileState = mobileNetworkInfo.getState();
        }

        if (NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED != mobileState) {
            return false;
        }
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            return false;
        }

        return true;
    }

    public static String getIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
//        wifiManager.setWifiEnabled(true);
        @SuppressLint("MissingPermission")
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();

        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }
}
