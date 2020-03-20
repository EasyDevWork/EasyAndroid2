package com.easy.utils;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class VolumeUtils {

    public static int getVolumeSize(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        int current = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
        Log.d("VoideUtils", "系统音量值：" + max + "-" + current);
        return current;
    }

    public static void setVolumeSize(Context context, int volume) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM,volume,AudioManager.FLAG_PLAY_SOUND);
    }
}
