package com.easy.store.base;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.List;

import static java.util.Collections.emptyList;

public class EasyStoreInit implements Initializer<EasyStore> {
    @NonNull
    @Override
    public EasyStore create(@NonNull Context context) {
        Log.d("Initializer", "EasyStoreInit");
        EasyStore.getInstance().init(context);
        return EasyStore.getInstance();
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return emptyList();
    }
}
