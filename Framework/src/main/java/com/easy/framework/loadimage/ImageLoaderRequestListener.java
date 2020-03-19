package com.easy.framework.loadimage;

public interface ImageLoaderRequestListener<R> {

    boolean onLoadFailed(String exception, boolean isFirstResource);

    boolean onResourceReady(R resource, boolean isFirstResource);
}
