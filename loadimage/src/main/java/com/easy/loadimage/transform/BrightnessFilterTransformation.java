package com.easy.loadimage.transform;

import androidx.annotation.NonNull;

import java.security.MessageDigest;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;

/**
 * 亮度调整-高亮
 * brightness value ranges from -1.0 to 1.0, with 0.0 as the normal level
 */
public class BrightnessFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.easy.loadimage.transform.BrightnessFilterTransformation." + VERSION;

    private float brightness;

    public BrightnessFilterTransformation() {
        this(0.0f);
    }

    public BrightnessFilterTransformation(float brightness) {
        super(new GPUImageBrightnessFilter());
        this.brightness = brightness;
        GPUImageBrightnessFilter filter = getFilter();
        filter.setBrightness(this.brightness);
    }

    @Override
    public String toString() {
        return "BrightnessFilterTransformation(brightness=" + brightness + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BrightnessFilterTransformation &&
                ((BrightnessFilterTransformation) o).brightness == brightness;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + (int) ((brightness + 1.0f) * 10);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + brightness).getBytes(CHARSET));
    }
}
