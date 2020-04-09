package com.easy.demo.ui.loadimage;

import android.Manifest;

import androidx.lifecycle.Lifecycle;

import com.easy.framework.base.BasePresenter;
import com.easy.loadimage.EasyLoadImage;
import com.easy.utils.FileUtils;
import com.easy.utils.EmptyUtils;
import com.easy.utils.StringUtils;
import com.easy.utils.base.FileConstant;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestLoadImagePresenter extends BasePresenter<TestLoadImageView> {
    @Inject
    public TestLoadImagePresenter() {

    }

    /**
     * 请求权限
     *
     * @param permissions
     */
    public void requestPermission(RxPermissions permissions) {
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(granted -> mvpView.permissionCallback(granted, null));
    }

    public void downloadImage(String imageUrl) {
        String fileExtension = FileUtils.getFileExtension(imageUrl, "png");
        String fileName = StringUtils.buildString("img_", System.currentTimeMillis(), ".", fileExtension);
        String saveFile = FileUtils.getFilePath(FileConstant.TYPE_PHOTO, getContext()) + fileName;
        Observable<File> observable = EasyLoadImage.downloadImageToGallery(getContext(), imageUrl, saveFile);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(file -> mvpView.downloadCallback(file, null), throwable -> mvpView.downloadCallback(null, throwable));
    }
}