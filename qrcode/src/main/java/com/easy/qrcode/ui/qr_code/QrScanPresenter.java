package com.easy.qrcode.ui.qr_code;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import com.easy.framework.base.BasePresenter;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class QrScanPresenter extends BasePresenter<QrScanView> {
    @Inject
    public QrScanPresenter() {
    }

    /**
     * 请求权限
     *
     * @param permissions
     */
    public void requestPermission(RxPermissions rxPermission, int type, String... permissions) {
        rxPermission.request(permissions)
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(granted -> mvpView.permissionCallback(granted, type, null));
    }

    public void selectAlbum(final FragmentActivity activity, final int code) {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(albumIntent, code);
    }

    public void scanAlbum(final String picturePath) {
        Single.create((SingleOnSubscribe<String>) emitter -> {
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
            Reader reader = new QRCodeReader();
            Map<DecodeHintType, String> hints = new EnumMap<>(DecodeHintType.class);
            try {
                Result result = reader.decode(binaryBitmap, hints);
                emitter.onSuccess(result.getText());
            } catch (Exception e1) {
                emitter.onError(e1);
                e1.printStackTrace();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(resultBack -> mvpView.scanAlbumCallBack(resultBack, 1), throwable -> mvpView.scanAlbumCallBack(throwable.getMessage(), 2));
    }
}