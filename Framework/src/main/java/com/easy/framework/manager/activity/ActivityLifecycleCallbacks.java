package com.easy.framework.manager.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.easy.framework.base.BaseApplication;
import com.easy.framework.even.ActivityWakeUpEvent;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    public Stack<WeakReference<Activity>> store = new Stack<>();
    private final String tag = "ActivityLifecycle";
    boolean lastState = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        WeakReference<Activity> weakActivity = new WeakReference<>(activity);
        store.add(weakActivity);
        Log.d(tag, getSimpleName(activity) + "<<Create>> InForeground：" + isApplicationInForeground());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(tag, getSimpleName(activity) + "<<Start>> InForeground：" + isApplicationInForeground());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(tag, getSimpleName(activity) + "<<Resumed>> InForeground：" + isApplicationInForeground());
    }

    public String getSimpleName(Activity activity) {
        return activity.getClass().getSimpleName();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(tag, getSimpleName(activity) + "<<Paused>> InForeground：" + isApplicationInForeground());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d("activityLife", activity.getLocalClassName() + " Stopped InForeground:" + isApplicationInForeground());
        boolean currentState = isApplicationInForeground();
        if (currentState != lastState) {
            EventBus.getDefault().post(new ActivityWakeUpEvent());
        }
        lastState = currentState;
        Log.d(tag, getSimpleName(activity) + "<<Stopped>> InForeground：" + isApplicationInForeground());
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
        Log.d(tag, getSimpleName(activity) + "<<SaveInstanceState>> InForeground：" + isApplicationInForeground());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        List<WeakReference<Activity>> temp = new ArrayList<>();
        for (WeakReference<Activity> weakReference : store) {
            if (weakReference.get() != null && activity.getLocalClassName().equals(weakReference.get().getLocalClassName())) {
                temp.add(weakReference);
            }
        }
        if (temp.size() > 0) {
            store.removeAll(temp);
        }
        Log.d(tag, getSimpleName(activity) + "<<Destroyed>> InForeground："+ isApplicationInForeground());
    }

    public boolean isApplicationInForeground() {
        android.app.ActivityManager mActivityManager = (android.app.ActivityManager) BaseApplication.getInst().getSystemService(Context.ACTIVITY_SERVICE);
        assert mActivityManager != null;
        List<android.app.ActivityManager.RunningAppProcessInfo> processes = mActivityManager.getRunningAppProcesses();
        if (processes.size() == 0) {
            return false;
        }
        for (android.app.ActivityManager.RunningAppProcessInfo process : processes) {
            Log.d(tag, "isApplicationInForeground===>" + process.importance);
            if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
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
                if (activityName.equals(activity.get().getLocalClassName())) {
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
            if (activity.get() != null && activityName.equals(activity.get().getLocalClassName())) {
                activity.get().finish();
                break;
            }
        }
    }
}
