package com.easy.framework.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.easy.framework.manager.network.NetworkStateLiveData;
import com.easy.framework.manager.screen.ScreenStateLiveData;

public class StateChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("StateChange", "Receiver action===>" + action);
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert manager != null;
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            NetworkStateLiveData.getInstance(context).setNetWork(activeNetwork);
        } else if (Intent.ACTION_SCREEN_ON.equals(action) // 开屏
                || Intent.ACTION_SCREEN_OFF.equals(action)// 锁屏
                || Intent.ACTION_USER_PRESENT.equals(action)) {// 解锁
            ScreenStateLiveData.getInstance(context).setScreenState(action);
        }
    }
}
