package com.easy.loadimage.transform;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 *  裁剪成距行
 */
public class CropSquareTransformation extends BitmapTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.easy.loadimage.transform.CropSquareTransformation." + VERSION;

    private int size;

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool,
                               @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        this.size = Math.max(outWidth, outHeight);
        return TransformationUtils.centerCrop(pool, toTransform, size, size);
    }

    @Override
    public String toString() {
        return "CropSquareTransformation(size=" + size + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CropSquareTransformation && ((CropSquareTransformation) o).size == size;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + size * 10;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + size).getBytes(CHARSET));
    }
}
