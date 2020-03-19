package com.easy.demo.ui.dialog;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.interfaces.XPopupImageLoader;

import java.io.File;

import io.reactivex.annotations.NonNull;

public class PopupImageLoader implements XPopupImageLoader {

    @Override
    public void loadImage(int position, Object uri, ImageView imageView) {
        Glide.with(imageView).load(uri).into(imageView);
    }

    //必须实现这个方法，返回uri对应的缓存文件，可参照下面的实现，内部保存图片会用到。如果你不需要保存图片这个功能，可以返回null。
    @Override
    public File getImageFile(Context context, @NonNull Object uri) {
        try {
            return Glide.with(context).downloadOnly().load(uri).submit().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
