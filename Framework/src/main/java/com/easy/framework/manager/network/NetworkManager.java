package com.easy.framework.manager.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.easy.framework.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;

public class NetworkManager {

    public static class Holder {
        public static NetworkManager holder = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return Holder.holder;
    }

    public List<INetStateChange> iNetStateChanges = new ArrayList<>();

    private NetworkManager() {

    }

    /**
     * 注册网络广播改变回调
     *
     * @param observer
     */
    public static void registerObserver(INetStateChange observer) {
        if (!NetworkManager.getInstance().iNetStateChanges.contains(observer)) {
            NetworkManager.getInstance().iNetStateChanges.add(observer);
        }
    }

    /**
     * 注销网络广播改变回调
     *
     * @param observer
     */
    public static void unRegisterObserver(INetStateChange observer) {
        if (NetworkManager.getInstance().iNetStateChanges.contains(observer)) {
            NetworkManager.getInstance().iNetStateChanges.remove(observer);
        }
    }

    /**
     * 注册广播
     *
     * @param receiver
     * @param context
     */
    public static void registerReceiver(NetStateChangeReceiver receiver, Context context) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, intentFilter);
    }

    /**
     * 注销广播
     *
     * @param receiver
     * @param context
     */
    public static void unRegisterReceiver(NetStateChangeReceiver receiver, Context context) {
        context.unregisterReceiver(receiver);
    }

    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getInst().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo();
        }
        return null;
    }

    /**
     * 获取当前网络类型
     * 需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}
     */
    public static NetworkType getNetworkType() {
        NetworkType netType = NetworkType.NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NetworkType.NETWORK_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                        netType = NetworkType.NETWORK_4G;
                        break;

                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NetworkType.NETWORK_2G;
                        break;
                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NetworkType.NETWORK_3G;
                        } else {
                            netType = NetworkType.NETWORK_UNKNOWN;
                        }
                        break;
                }
            } else {
                netType = NetworkType.NETWORK_UNKNOWN;
            }
        }
        return netType;
    }
}
