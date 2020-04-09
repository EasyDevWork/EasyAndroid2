package com.easy.widget;

import android.util.Log;
import android.view.View;

public abstract class QuickClickListener implements View.OnClickListener {
    private long mLastClickTime;
    private long timeInterval = 1000L;

    public QuickClickListener() {

    }

    public QuickClickListener(long interval) {
        this.timeInterval = interval;
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) {
            // 单次点击事件
            onSingleClick();
        } else {
            Log.d("QuickClickListener", "too quick.ignore");
        }
        mLastClickTime = nowTime;
    }

    protected abstract void onSingleClick();

//    protected abstract void onFastClick();
}