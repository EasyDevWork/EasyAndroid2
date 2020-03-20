package com.easy.loadimage;

public class ImageLoadFactory {

    public static ImageLoaderImpl create() {
        return new ImageLoaderImpl();
    }
}
