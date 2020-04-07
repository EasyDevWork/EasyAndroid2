package com.easy.demo.ui.loadimage;

import android.Manifest;

import com.easy.framework.base.BasePresenter;
import com.easy.framework.observable.DataObserver;
import com.easy.loadimage.EasyLoadImage;
import com.easy.utils.FileUtils;
import com.easy.utils.Utils;
import com.easy.utils.base.FileConstant;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;

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
        bindObservable(permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE))
                .lifecycleProvider(getRxLifecycle())
                .activityEvent(ActivityEvent.DESTROY)
                .observe(new DataObserver<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean granted) {
                        mvpView.permissionCallback(granted, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mvpView.permissionCallback(null, e);
                    }
                });
    }

    public void downloadImage(String imageUrl) {
        String fileExtension = FileUtils.getFileExtension(imageUrl, "png");
        String fileName = Utils.buildString("img_", System.currentTimeMillis(), ".", fileExtension);
        String saveFile = FileUtils.getFilePath(FileConstant.TYPE_PHOTO, getContext()) + fileName;
        Observable<File> observable = EasyLoadImage.downloadImageToGallery(getContext(), imageUrl, saveFile);

        bindObservable(observable).lifecycleProvider(getRxLifecycle())
                .activityEvent(ActivityEvent.DESTROY)
                .observe(new DataObserver<File>() {
                    @Override
                    protected void onSuccess(File file) {
                        mvpView.downloadCallback(file, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mvpView.downloadCallback(null, e);
                    }
                });
    }
}