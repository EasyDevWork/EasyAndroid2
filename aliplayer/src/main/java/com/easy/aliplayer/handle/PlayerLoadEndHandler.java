package com.easy.aliplayer.handle;

import android.os.Handler;
import android.os.Message;

import com.easy.aliplayer.view.AliyunVodPlayerView;

import java.lang.ref.WeakReference;
/**
 * 当VodPlayer 没有加载完成的时候,调用onStop 去暂停视频,
 * 会出现暂停失败的问题。
 */
public class PlayerLoadEndHandler extends Handler {

    private WeakReference<AliyunVodPlayerView> weakReference;

    private boolean intentPause;

    public PlayerLoadEndHandler(AliyunVodPlayerView aliyunVodPlayerView) {
        weakReference = new WeakReference<>(aliyunVodPlayerView);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 0) {
            intentPause = true;
        }
        if (msg.what == 1) {
            AliyunVodPlayerView aliyunVodPlayerView = weakReference.get();
            if (aliyunVodPlayerView != null && intentPause) {
                aliyunVodPlayerView.onStop();
                intentPause = false;
            }
        }
    }
}
