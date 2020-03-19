package com.easy.framework.loadimage;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;


/**
 * 1.图片库设计
 * 目标1：切换图片库的时候不需要改动接口
 * 目标2：图片库升级的时候只需要改动实现类的代码
 * <p>
 * 2.核心类
 * 1.AbstractImageLoader 抽象类接口
 * 2.AbstractImageLoaderTarget 图片库加载Target接口
 * 3.ImageLoaderImpl 图片库功能实现类->基于Glide
 * 4.ImageLoaderOptions 图片库参数构建类
 * 5.ImageLoaderRequestListener 图片加载监听接口
 */
public abstract class AbstractImageLoader {

    public abstract AbstractImageLoader loadImage(@NonNull Context context, @NonNull byte[] bytes);

    public abstract AbstractImageLoader loadImage(@NonNull Context context, @NonNull File file);

    public abstract AbstractImageLoader loadImage(@NonNull Context context, @NonNull String url);

    public abstract AbstractImageLoader loadImage(@NonNull Context context, @NonNull int resId);

    public abstract AbstractImageLoader loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageLoaderOptions loaderOptions);

    public abstract AbstractImageLoader loadImage(@NonNull Context context, @NonNull File file, @NonNull ImageLoaderOptions loaderOptions);

    public abstract AbstractImageLoader loadImage(@NonNull Context context, @NonNull byte[] bytes, @NonNull ImageLoaderOptions loaderOptions);

    public abstract AbstractImageLoader loadImage(@NonNull Context context, @DrawableRes int resId, @Nullable ImageLoaderOptions loaderOptions);

    /**
     * 添加listener
     *
     * @param listener RequestListener
     */
    public abstract <R> AbstractImageLoader listener(@NonNull ImageLoaderRequestListener<R> listener);

    /**
     * Sets the {@link ImageView} the resource will be loaded into, cancels any existing loads into
     * the view, and frees any resources Glide may have previously loaded into the view so they may be
     * reused.
     *
     * @param imageView ImageView
     */
    public abstract void into(@NonNull ImageView imageView);

    /**
     * 预加载
     */
    public abstract void preload();

    /**
     * Set the target the resource will be loaded into.
     *
     * @param loaderTarget The target to load the resource into.
     * @param view         目标view对象,用于匹配加载资源Target的参数相关因素，并不会直接设置资源的显示
     */
    public abstract <T> void into(@NonNull View view, @NonNull final AbstractImageLoaderTarget<T> loaderTarget);


    /**
     * 减少引用计数
     *
     * @param context   Context
     * @param imageView imageView
     */
    public abstract void clear(@NonNull Context context, @NonNull ImageView imageView);

}
