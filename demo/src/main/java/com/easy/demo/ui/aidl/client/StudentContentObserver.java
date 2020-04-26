package com.easy.demo.ui.aidl.client;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class StudentContentObserver extends ContentObserver {
    Handler mHandler;

    public StudentContentObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Message message = Message.obtain();
        message.obj = uri;
        message.what = 2;
        mHandler.sendMessage(message);
    }
}
