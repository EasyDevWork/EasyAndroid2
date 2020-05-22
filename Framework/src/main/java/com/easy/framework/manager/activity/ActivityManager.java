package com.easy.framework.manager.activity;

import android.app.Activity;

public class ActivityManager {

    private ActivityLifecycleCallbacks callbacks;

    private static final class Holder {
        private static final ActivityManager instance = new ActivityManager();
    }

    private ActivityManager() {
        callbacks = new ActivityLifecycleCallbacks();
    }

    public static ActivityManager getInstance() {
        return Holder.instance;
    }

    public ActivityLifecycleCallbacks getCallbacks() {
        return callbacks;
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return callbacks.getCurrentActivity();
    }

    /**
     * 关闭所有页面
     */
    public void finishAllActivity() {
        callbacks.finishAllActivity();
    }

    /**
     * 保留指定页面，关闭其他页面
     *
     * @param activityName
     */
    public void finishActivityExcept(String activityName) {
        callbacks.finishOtherActivity(activityName);
    }

    /**
     * 保留最后一个，关闭其他页面
     */
    public void finishOtherActivity() {
        callbacks.finishOtherActivity();
    }

    /**
     * 保留指定页面，关闭其他页面
     */
    public void finishOtherActivity(Activity activity) {
        callbacks.finishOtherActivity(activity);
    }


    public void finishActivity(String activityName) {
        if (callbacks != null) {
            callbacks.finishActivity(activityName);
        }
    }

    /**
     * 应用是否在后台
     *
     * @return
     */
    public boolean isApplicationInForeground() {
        if (callbacks != null) {
            return callbacks.isApplicationInForeground();
        }
        return false;
    }

    public void exitApp() {
        callbacks.finishAllActivity();
        System.exit(0);
    }
}
