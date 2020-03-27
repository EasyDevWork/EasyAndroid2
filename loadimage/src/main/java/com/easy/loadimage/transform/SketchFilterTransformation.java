package com.easy.loadimage.transform;

import androidx.annotation.NonNull;

import java.security.MessageDigest;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter;

/**
 *  旋涡; 螺旋形;
 */
public class SketchFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.easy.loadimage.transform.SketchFilterTransformation." + VERSION;

    public SketchFilterTransformation() {
        super(new GPUImageSketchFilter());
    }

    @Override
    public String toString() {
        return "SketchFilterTransformation()";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SketchFilterTransformation;
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
