package com.easy.loadimage.transform;

import androidx.annotation.NonNull;

import java.security.MessageDigest;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter;

public class InvertFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.easy.loadimage.transform.InvertFilterTransformation." + VERSION;

    public InvertFilterTransformation() {
        super(new GPUImageColorInvertFilter());
    }

    @Override
    public String toString() {
        return "InvertFilterTransformation()";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof InvertFilterTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID).getBytes(CHARSET));
    }
}
