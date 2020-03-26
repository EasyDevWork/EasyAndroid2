package com.easy.loadimage;

import android.widget.ImageView;

public class ImageConfig {
    protected String url;
    protected int drawableId;
    protected ImageView imageView;
    protected int placeholder;
    protected int errorPic;

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public void setErrorPic(int errorPic) {
        this.errorPic = errorPic;
    }
}
