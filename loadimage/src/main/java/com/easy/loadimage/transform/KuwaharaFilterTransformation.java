package com.easy.loadimage.transform;

import androidx.annotation.NonNull;

import java.security.MessageDigest;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageKuwaharaFilter;

/**
 * Kuwahara
 * Kuwahara all the colors in the image.
 *
 * The radius to sample from when creating the brush-stroke effect, with a default of 25.
 * The larger the radius, the slower the filter.
 */
public class KuwaharaFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.easy.loadimage.transform.KuwaharaFilterTransformation." + VERSION;

    private int radius;

    public KuwaharaFilterTransformation() {
        this(25);
    }

    public KuwaharaFilterTransformation(int radius) {
        super(new GPUImageKuwaharaFilter());
        this.radius = radius;
        GPUImageKuwaharaFilter filter = getFilter();
        filter.setRadius(this.radius);
    }

    @Override
    public String toString() {
        return "KuwaharaFilterTransformation(radius=" + radius + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof KuwaharaFilterTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + radius * 10;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + radius).getBytes(CHARSET));
    }
}
