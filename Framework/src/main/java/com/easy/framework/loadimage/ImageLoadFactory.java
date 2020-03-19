package com.easy.framework.loadimage;

public class ImageLoadFactory {

    public static ImageLoaderImpl create() {
        return new ImageLoaderImpl();
    }
}
