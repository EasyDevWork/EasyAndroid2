package com.easy.demo.ui.flutter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.engine.FlutterEngine;

public class TestFlutterFragment extends FlutterFragment {

    private static FlutterEngine mFlutterEngine;
    private static final String CHANNEL_NATIVE = "com.example.flutter/native";

    public static TestFlutterFragment newInstance(String initialRoute, FlutterEngine flutterEngine) {
        TestFlutterFragment fragment = new TestFlutterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INITIAL_ROUTE, initialRoute);
        fragment.setArguments(args);
        mFlutterEngine = flutterEngine;
        return fragment;
    }

    @Nullable
    @Override
    public FlutterEngine provideFlutterEngine(@NonNull Context context) {
        return mFlutterEngine;
    }
}