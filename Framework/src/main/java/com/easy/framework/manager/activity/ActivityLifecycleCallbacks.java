package com.easy.framework.manager.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.easy.framework.even.ActivityWakeUpEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Stack;

public class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    public Stack<WeakReference<Activity>> store = new Stack<>();
    private long resumed;
    private int paused;
    private int started;
    private int stopped;
    private boolean isForeground = true;//是否在前台；
    private final String tag = "ActivityLifecycle";

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        WeakReference<Activity> weakActivity = new WeakReference<>(activity);
        store.add(weakActivity);
        Log.d(tag, getSimpleName(activity) + "<<Create>>");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
        Log.d(tag, getSimpleName(activity) + "<<Start>>");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
        if (isApplicationInForeground() && !isForeground) {
            isForeground = true;
            EventBus.getDefault().post(new ActivityWakeUpEvent());
        }
        Log.d(tag, getSimpleName(activity) + "<<Resumed>>");
    }

    public String getSimpleName(Activity activity) {
        return activity.getClass().getSimpleName();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        Log.d(tag, getSimpleName(activity) + "<<Paused>>");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        isForeground = isApplicationVisible();
        Log.d(tag, getSimpleName(activity) + "<<Stopped>>");
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
        Log.d(tag, getSimpleName(activity) + "<<SaveInstanceState>>");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        store.remove(activity);
        Log.d(tag, getSimpleName(activity) + "<<Destroyed>>");
    }

    public boolean isApplicationVisible() {
        return started > stopped;
    }

    public boolean isApplicationInForeground() {
        // 当所有 Activity 的状态中处于 resumed 的大于 paused 状态的，即可认为有Activity处于前台状态中
        return resumed > paused;
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return store.lastElement().get();
    }

    /**
     * 保留最后一个，关闭其他页面
     */
    public void finishOtherActivity() {
        Activity lastActivity = store.lastElement().get();
        for (WeakReference<Activity> activity : store) {
            if (activity.get() != null && lastActivity != activity.get()) {
                activity.get().finish();
            }
        }
    }

    /**
     * 保留指定页面，关闭其他页面
     *
     * @param saveActivity
     */
    public void finishOtherActivity(Activity saveActivity) {
        for (WeakReference<Activity> activity : store) {
            if (activity.get() != null && saveActivity != activity.get()) {
                activity.get().finish();
            }
        }
    }

    /**
     * 保留指定页面，关闭其他页面
     *
     * @param activityName
     */
    public void finishOtherActivity(String activityName) {
        if (TextUtils.isEmpty(activityName)) {
            return;
        }
        for (WeakReference<Activity> activity : store) {
            if (activity.get() != null) {
                if (activityName.equals(activity.get().getClass().getName())) {
                    continue;
                }
                activity.get().finish();
            }
        }
    }

    /**
     * 关闭所有页面
     */
    public void finishAllActivity() {
        for (WeakReference<Activity> activity : store) {
            if (activity.get() != null) {
                activity.get().finish();
            }
        }
    }

    public void finishActivity(String activityName) {
        if (TextUtils.isEmpty(activityName)) {
            return;
        }
        for (WeakReference<Activity> activity : store) {
            if (activity.get() != null && activityName.equals(activity.get().getClass().getName())) {
                activity.get().finish();
                break;
            }
        }
    }

    /**
     * 页面是否在后台
     *
     * @param activityName
     * @return
     */
    public boolean isInBackground(String activityName) {
        if (TextUtils.isEmpty(activityName)) {
            return false;
        }
        for (WeakReference<Activity> activity : store) {
            if (activity.get() != null && activityName.equals(activity.get().getClass().getName())) {
                return true;
            }
        }
        return false;
    }
}
