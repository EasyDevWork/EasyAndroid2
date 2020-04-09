package com.easy.framework.manager;

import android.app.Activity;

import com.easy.framework.R;
import com.easy.framework.manager.activity.ActivityManager;
import com.easy.utils.ToastUtils;

public class AppQuitManager {

    private long fistTouchTime;

    public void onBackPressed(Activity activity) {
        long nowTouchTime = System.currentTimeMillis();
        if (nowTouchTime - fistTouchTime < 501) {
            ActivityManager.getInstance().exitApp();
        } else {
            fistTouchTime = nowTouchTime;
            String appName = activity.getString(R.string.app_name);
            ToastUtils.showShort(activity.getString(R.string.quit_app, appName));
        }
    }
}
