package com.easy.framework.manager.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenStateBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            notifyObservers(ScreenStateType.SCREEN_ON);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            notifyObservers(ScreenStateType.SCREEN_OFF);
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            notifyObservers(ScreenStateType.SCREEN_UNLOCK);
        }
    }

    private void notifyObservers(ScreenStateType type) {
        for (IScreenStateChange observer : ScreenManager.getInstance().iScreenStateChanges) {
            observer.onScreenState(type);
        }
    }
}
