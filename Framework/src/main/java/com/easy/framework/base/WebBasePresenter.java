package com.easy.framework.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.easy.utils.SystemUtils;

import java.io.File;
import java.io.FileOutputStream;

import javax.inject.Inject;

public class WebBasePresenter extends BasePresenter<WebBaseView> {
    @Inject
    public WebBasePresenter() {
    }

    // 保存图片方法
    public void saveImage(String data) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        Glide.with(context).asBitmap().load(data).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                save2Album(bitmap, "${System.currentTimeMillis()}.jpg");
            }
        });
    }

    private void save2Album(Bitmap bitmap, String fileName) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            SystemUtils.insertImageMedia(context,file);
            SystemUtils.sendBroadcastUpdateMedia(context,file);
            Toast.makeText(context, "图片保存成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}