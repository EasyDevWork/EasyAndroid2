package com.easy.common.ui.qr_code;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import androidx.fragment.app.FragmentActivity;

import com.easy.framework.base.BasePresenter;
import com.easy.framework.base.DataObservable;
import com.easy.framework.base.DataObserver;
import com.easy.net.event.ActivityEvent;
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

import io.reactivex.Observable;

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
        DataObservable.builder(rxPermission.request(permissions))
                .lifecycleProvider(mvpView.getRxLifecycle())
                .activityEvent(ActivityEvent.DESTROY)
                .dataObserver(new DataObserver<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean granted) {
                        mvpView.permissionCallback(granted, type, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mvpView.permissionCallback(null, type, e);
                    }
                });
    }

    public void selectAlbum(final FragmentActivity activity, final int code) {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(albumIntent, code);
    }

    public void scanAlbum(final String picturePath) {
        Observable<String> dataObservable = Observable.create(e -> {
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
            Reader reader = new QRCodeReader();
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            try {
                Result result = reader.decode(binaryBitmap, hints);
                e.onNext(result.getText());
            } catch (Exception e1) {
                e1.printStackTrace();
                if (e1.getMessage() != null) {
                    e.onNext(e1.getMessage());
                } else {
                    e.onNext("error");
                }
            }
        });

        DataObservable.builder(dataObservable)
                .lifecycleProvider(mvpView.getRxLifecycle())
                .activityEvent(ActivityEvent.DESTROY)
                .dataObserver(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        mvpView.scanAlbumCallBack(result, 1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mvpView.scanAlbumCallBack(e.getMessage(), 2);
                    }
                });
    }
}