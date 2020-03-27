package com.easy.loadimage.transform;

import androidx.annotation.NonNull;

import java.security.MessageDigest;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;

/**
 * 深褐色
 * Applies a simple sepia effect.
 * <p>
 * The intensity with a default of 1.0.
 */
public class SepiaFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.easy.loadimage.transform.SepiaFilterTransformation." + VERSION;

    private float intensity;

    public SepiaFilterTransformation() {
        this(1.0f);
    }

    public SepiaFilterTransformation(float intensity) {
        super(new GPUImageSepiaToneFilter());
        this.intensity = intensity;
        GPUImageSepiaToneFilter filter = getFilter();
        filter.setIntensity(this.intensity);
    }

    @Override
    public String toString() {
        return "SepiaFilterTransformation(intensity=" + intensity + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SepiaFilterTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + (int) (intensity * 10);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + intensity).getBytes(CHARSET));
    }
}
