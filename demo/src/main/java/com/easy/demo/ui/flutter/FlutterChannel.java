package com.easy.demo.ui.flutter;


import androidx.annotation.Nullable;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

public class FlutterChannel {
    private static final String CHANNEL_NATIVE = "com.easy.flutter/nativeToFlutter";//native发给Flutter
    private static final String CHANNEL_FLUTTER = "com.easy.flutter/flutterToNative";//Flutter发给native

    public static final String sendMethod = "msg";

    public static MethodChannel nativeChannel;
    public static MethodChannel flutterChannel;

    public static void init(BinaryMessenger messenger) {
        nativeChannel = new MethodChannel(messenger, CHANNEL_NATIVE);
        flutterChannel = new MethodChannel(messenger, CHANNEL_FLUTTER);
    }

    public static void setHandler(MethodChannel.MethodCallHandler handler) {
        flutterChannel.setMethodCallHandler(handler);
    }

    public static void send(String method, @Nullable Object arguments) {
        nativeChannel.invokeMethod(method, arguments);
    }
}
