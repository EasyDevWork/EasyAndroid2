package com.easy.framework.manager.screen;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.easy.framework.receive.StateChangeReceiver;
import com.easy.utils.SystemUtils;

public class ScreenStateLiveData extends LiveData<ScreenStateType> {

    private final Context mContext;
    private static ScreenStateLiveData screenStateLiveData;
    private StateChangeReceiver stateChangeReceiver;
    private final IntentFilter mIntentFilter;

    private ScreenStateLiveData(Context context) {
        mContext = context.getApplicationContext();
        stateChangeReceiver = new StateChangeReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mIntentFilter.addAction(Intent.ACTION_USER_PRESENT);
    }

    public static ScreenStateLiveData getInstance(Context context) {
        if (screenStateLiveData == null) {
            screenStateLiveData = new ScreenStateLiveData(context);
        }
        return screenStateLiveData;
    }

    public void setScreenState(String action) {
        setValue(getScreenType(action));
    }

    private ScreenStateType getScreenType(String action) {
        switch (action) {
            case Intent.ACTION_SCREEN_ON:
                return ScreenStateType.SCREEN_ON;
            case Intent.ACTION_SCREEN_OFF:
                return ScreenStateType.SCREEN_OFF;
            default:
                return ScreenStateType.SCREEN_UNLOCK;
        }
    }

    private ScreenStateType getScreenType(int action) {
        switch (action) {
            case 1:
                return ScreenStateType.SCREEN_ON;
            case 0:
                return ScreenStateType.SCREEN_OFF;
            default:
                return ScreenStateType.SCREEN_UNLOCK;
        }
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.d("StateChange", "ScreenState onActive");
        mContext.registerReceiver(stateChangeReceiver, mIntentFilter);
        setValue(getScreenType(SystemUtils.getScreenState(mContext)));
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d("StateChange", "ScreenState onInactive");
        mContext.unregisterReceiver(stateChangeReceiver);
    }
}
