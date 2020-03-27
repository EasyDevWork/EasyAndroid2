package com.easy.loadimage.transform;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import java.security.MessageDigest;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageSwirlFilter;

/**
 * Creates a swirl distortion on the image.
 */
public class SwirlFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.easy.loadimage.transform.SwirlFilterTransformation." + VERSION;

    private float radius;
    private float angle;
    private PointF center;

    public SwirlFilterTransformation() {
        this(.5f, 1.0f, new PointF(0.5f, 0.5f));
    }

    /**
     * @param radius from 0.0 to 1.0, default 0.5
     * @param angle  minimum 0.0, default 1.0
     * @param center default (0.5, 0.5)
     */
    public SwirlFilterTransformation(float radius, float angle, PointF center) {
        super(new GPUImageSwirlFilter());
        this.radius = radius;
        this.angle = angle;
        this.center = center;
        GPUImageSwirlFilter filter = getFilter();
        filter.setRadius(this.radius);
        filter.setAngle(this.angle);
        filter.setCenter(this.center);
    }

    @Override
    public String toString() {
        return "SwirlFilterTransformation(radius=" + radius + ",angle=" + angle + ",center="
                + center.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SwirlFilterTransformation &&
                ((SwirlFilterTransformation) o).radius == radius &&
                ((SwirlFilterTransformation) o).angle == radius &&
                ((SwirlFilterTransformation) o).center.equals(center.x, center.y);
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + (int) (radius * 1000) + (int) (angle * 10) + center.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + radius + angle + center.hashCode()).getBytes(CHARSET));
    }
}
