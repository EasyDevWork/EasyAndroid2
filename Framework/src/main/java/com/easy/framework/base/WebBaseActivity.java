package com.easy.framework.base;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.ValueCallback;

import androidx.core.content.FileProvider;
import androidx.databinding.ViewDataBinding;

import com.easy.framework.even.ChooseFileEvent;
import com.easy.widget.WebViewMenuDialog;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

public abstract class WebBaseActivity<P extends BasePresenter, V extends ViewDataBinding> extends BaseActivity<P, V> {

    //处理WebView上传视频/图片/拍照功能
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    public final static int FILE = 10000;//修改要对应改com.easy.widget.WebViewMenuDialog这个类
    public final static int CAPTURE = 10001;
    public final static int VIDEO = 10002;
    public static String PHOTO_PATH, VIDEO_PATH;
    private Uri photoUri, videoUri;

    public WebBaseFragment webViewFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPath();
        LiveEventBus.get("FileChooser", ChooseFileEvent.class).observe(this, this::openChooserList);
    }

    /**
     * 初始化图片/视频上传路径
     */
    private void initPath() {
        //path为保存图片的路径，执行完拍照以后能保存到指定的路径下
//        PATH = MeetOneFileUtils.getSaveFilePath(PrivateConstant.FileInfo.TYPE_PHOTO, this) + "capture.jpg";
        PHOTO_PATH = getCacheDir() + "/capture.jpg";
        File mTempFile = new File(WebBaseActivity.PHOTO_PATH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", mTempFile);
        } else {
            photoUri = Uri.fromFile(mTempFile);
        }

        VIDEO_PATH = getCacheDir() + "/video.mp3";
        mTempFile = new File(WebBaseActivity.VIDEO_PATH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            videoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", mTempFile);
        } else {
            videoUri = Uri.fromFile(mTempFile);
        }
    }

    public void openChooserList(ChooseFileEvent chooseFileEvent) {
        uploadMessage = chooseFileEvent.getUploadMessage();
        uploadMessageAboveL = chooseFileEvent.getUploadMessageAboveL();
        Bundle bundle = new Bundle();
        WebViewMenuDialog.newInstance(bundle, new WebViewMenuDialog.Callback() {
            @Override
            public void cancelDialog() {

            }

            @Override
            public void choosePhotograph() {
                RxPermissions permissions = new RxPermissions(WebBaseActivity.this);
                permissions.request(Manifest.permission.CAMERA)
                        .as(getAutoDispose())
                        .subscribe(granted -> {
                            if (granted) {
                                //步骤四：调取系统拍照
                                Intent intent = new Intent();
                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(intent, CAPTURE);
                            }
                        });
            }

            @Override
            public void chooseVideo() {
                RxPermissions permissions = new RxPermissions(WebBaseActivity.this);
                permissions.request(Manifest.permission.CAMERA)
                        .as(getAutoDispose())
                        .subscribe(granted -> {
                    if (granted) {
                        //步骤四：调取系统拍照
                        Intent intent = new Intent();
                        intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                        startActivityForResult(intent, VIDEO);
                    }
                });
            }
        }).show(getSupportFragmentManager(), null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == uploadMessage && null == uploadMessageAboveL) return;
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FILE) {
                    Uri result = data == null ? null : data.getData();
                    if (uploadMessageAboveL != null) {
                        onActivityResultAboveL(requestCode, resultCode, data);
                    } else if (uploadMessage != null) {
                        uploadMessage.onReceiveValue(result);
                        uploadMessage = null;
                    }
                } else if (requestCode == CAPTURE) {
                    if (uploadMessageAboveL != null) {
                        uploadMessageAboveL.onReceiveValue(new Uri[]{photoUri});
                        uploadMessageAboveL = null;
                    } else {
                        uploadMessage.onReceiveValue(photoUri);
                        uploadMessage = null;
                    }
                } else if (requestCode == VIDEO) {
                    if (uploadMessageAboveL != null) {
                        uploadMessageAboveL.onReceiveValue(new Uri[]{videoUri});
                        uploadMessageAboveL = null;
                    } else {
                        uploadMessage.onReceiveValue(videoUri);
                        uploadMessage = null;
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
            cancel();
        }
    }

    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    public void cancelDialog() {
        cancel();
    }

    private void cancel() {
        if (uploadMessageAboveL != null) {
            uploadMessageAboveL.onReceiveValue(null);
            uploadMessageAboveL = null;
        } else if (uploadMessage != null) {
            uploadMessage.onReceiveValue(null);
            uploadMessage = null;
        }
    }
}
