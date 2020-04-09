package com.easy.framework.manager.screen;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import java.util.ArrayList;
import java.util.List;

public class ScreenManager {

    public static class Holder {
       public static ScreenManager holder = new ScreenManager();
    }

    public static ScreenManager getInstance() {
        return Holder.holder;
    }

    public List<IScreenStateChange> iScreenStateChanges = new ArrayList<>();

    private ScreenManager() {

    }

    /**
     * 注册网络广播改变回调
     *
     * @param observer
     */
    public static void registerObserver(IScreenStateChange observer) {
        if (!ScreenManager.getInstance().iScreenStateChanges.contains(observer)) {
            ScreenManager.getInstance().iScreenStateChanges.add(observer);
        }
    }

    /**
     * 注销网络广播改变回调
     *
     * @param observer
     */
    public static void unRegisterObserver(IScreenStateChange observer) {
        if (ScreenManager.getInstance().iScreenStateChanges.contains(observer)) {
            ScreenManager.getInstance().iScreenStateChanges.remove(observer);
        }
    }

    /**
     * 注册广播
     *
     * @param receiver
     * @param context
     */
    public static void registerReceiver(ScreenStateBroadcastReceiver receiver, Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        context.registerReceiver(receiver, filter);
    }

    /**
     * 注销广播
     *
     * @param receiver
     * @param context
     */
    public static void unRegisterReceiver(ScreenStateBroadcastReceiver receiver, Context context) {
        context.unregisterReceiver(receiver);
    }
}
