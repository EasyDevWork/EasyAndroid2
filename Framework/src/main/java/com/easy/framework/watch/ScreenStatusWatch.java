package com.easy.framework.watch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * 屏幕开屏/锁屏监听工具类
 */
public class ScreenStatusWatch {

    private IntentFilter mScreenStatusFilter;
    private ScreenStatusListener mScreenStatusListener = null;
    //监听事件
    public interface ScreenStatusListener {
        void onScreenOn();

        void onScreenOff();

        void onScreenUnlock();
    }

    public ScreenStatusWatch() {
        mScreenStatusFilter = new IntentFilter();
        mScreenStatusFilter.addAction(Intent.ACTION_SCREEN_ON);
        mScreenStatusFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mScreenStatusFilter.addAction(Intent.ACTION_USER_PRESENT);
    }

    private BroadcastReceiver mScreenStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
                Log.d("ScreenStatusWatch","ACTION_SCREEN_ON");
                if (mScreenStatusListener != null) {
                    mScreenStatusListener.onScreenOn();
                }
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                Log.d("ScreenStatusWatch","ACTION_SCREEN_OFF");
                if (mScreenStatusListener != null) {
                    mScreenStatusListener.onScreenOff();
                }
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
                Log.d("ScreenStatusWatch","ACTION_USER_PRESENT");
                if (mScreenStatusListener != null) {
                    mScreenStatusListener.onScreenUnlock();
                }
            }
        }
    };

    //设置监听
    public void setScreenStatusListener(ScreenStatusListener l) {
        mScreenStatusListener = l;
    }

    //开始监听
    public void startListen(Context context) {
        if (context != null) {
            context.registerReceiver(mScreenStatusReceiver, mScreenStatusFilter);
        }
    }

    //结束监听
    public void stopListen(Context context) {
        if (context != null) {
            context.unregisterReceiver(mScreenStatusReceiver);
        }
    }
}
