package com.easy.demo.ui.skin;

import com.easy.framework.base.BasePresenter;
import com.easy.utils.FileUtils;

import java.io.FileOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TestSkinPresenter extends BasePresenter<TestSkinView> {
    @Inject
    public TestSkinPresenter() {

    }

    public void copySkinApkToSdcard(String fileName, String toFile) {
        Single.create((SingleOnSubscribe<Boolean>) emitter -> {
            InputStream is = getContext().getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(toFile);
            FileUtils.writeFile(is, fos);
            emitter.onSuccess(true);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose()).subscribe(result -> {
            mvpView.copyCallback(result);
        }, throwable -> mvpView.copyCallback(false));
    }
}