package com.easy.framework.watch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 网络连接状态的监听器。通过注册broadcast实现的
 */
public class NetStatusWatch {
    private Context mContext;
    //网络变化监听
    private NetChangeListener mNetChangeListener;
    private NetConnectedListener mNetConnectedListener;
    private boolean isReconnect;
    //广播过滤器，监听网络变化
    private IntentFilter mNetIntentFilter = new IntentFilter();
    private boolean isRegister;

    /**
     * 网络变化监听事件
     */
    public interface NetChangeListener {
        /**
         * wifi变为4G
         */
        void onWifiTo4G();

        /**
         * 4G变为wifi
         */
        void on4GToWifi();

        /**
         * 网络断开
         */
        void onNetDisconnected();
    }

    /**
     * 判断是否有网络的监听
     */
    public interface NetConnectedListener {
        /**
         * 网络已连接
         */
        void onReNetConnected(boolean isReconnect);

        /**
         * 网络未连接
         */
        void onNetUnConnected();
    }

    public NetStatusWatch(Context context) {
        mContext = context.getApplicationContext();
        mNetIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取手机的连接服务管理器，这里是连接管理器类
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

            if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                if (mNetConnectedListener != null) {
                    mNetConnectedListener.onReNetConnected(isReconnect);
                    isReconnect = false;
                }
            } else if (activeNetworkInfo == null) {
                if (mNetConnectedListener != null) {
                    isReconnect = true;
                    mNetConnectedListener.onNetUnConnected();
                }
            }

            if (NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED == mobileState) {
                Log.d("NetStatusWatch", "onWifiTo4G()");
                if (mNetChangeListener != null) {
                    mNetChangeListener.onWifiTo4G();
                }
            } else if (NetworkInfo.State.CONNECTED == wifiState && NetworkInfo.State.CONNECTED != mobileState) {
                if (mNetChangeListener != null) {
                    mNetChangeListener.on4GToWifi();
                }
            } else if (NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED != mobileState) {
                if (mNetChangeListener != null) {
                    mNetChangeListener.onNetDisconnected();
                }
            }

        }
    };

    /**
     * 设置网络变化监听
     *
     * @param l 监听事件
     */
    public void setNetChangeListener(NetChangeListener l) {
        mNetChangeListener = l;
    }

    public void setNetConnectedListener(NetConnectedListener mNetConnectedListener) {
        this.mNetConnectedListener = mNetConnectedListener;
    }

    /**
     * 开始监听
     */
    public void startWatch() {
        mContext.registerReceiver(mReceiver, mNetIntentFilter);
        isRegister = true;
    }

    /**
     * 结束监听
     */
    public void stopWatch() {
        if (isRegister) {
            mContext.unregisterReceiver(mReceiver);
        }
    }
}
