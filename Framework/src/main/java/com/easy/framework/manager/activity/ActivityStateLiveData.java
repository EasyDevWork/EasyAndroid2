package com.easy.framework.manager.activity;

import android.util.Log;

import androidx.lifecycle.LiveData;

public class ActivityStateLiveData extends LiveData<ActivityStateType> {
    public static class Holder {
        public static final ActivityStateLiveData install = new ActivityStateLiveData();
    }

    private ActivityStateLiveData() {

    }

    public static ActivityStateLiveData getInstance() {
        return Holder.install;
    }

    public void setActivityState(ActivityStateType type) {
        setValue(type);
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.d("StateChange", "ActivityState onActive");
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d("StateChange", "ActivityState onInactive");
    }
}
