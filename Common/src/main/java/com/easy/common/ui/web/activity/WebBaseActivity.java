package com.easy.common.ui.web.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.ValueCallback;

import androidx.core.content.FileProvider;
import androidx.databinding.ViewDataBinding;

import com.easy.common.dialog.WebViewMenuDialog;
import com.easy.common.event.ChooseFileEvent;
import com.easy.framework.base.BaseActivity;
import com.easy.framework.base.BasePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public abstract class WebBaseActivity<P extends BasePresenter, V extends ViewDataBinding> extends BaseActivity<P, V> {

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    public final static int FILE = 10000;
    public final static int CAPTURE = 10001;
    public final static int VIDEO = 10002;
    public static String PHOTO_PATH, VIDEO_PATH;
    private Uri photoUri, videoUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openChooserList(ChooseFileEvent chooseFileEvent) {
        uploadMessage = chooseFileEvent.getUploadMessage();
        uploadMessageAboveL = chooseFileEvent.getUploadMessageAboveL();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFeedBack", chooseFileEvent.isFeedBack());
        WebViewMenuDialog.newInstance(bundle).show(getSupportFragmentManager(), null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (null == uploadMessage && null == uploadMessageAboveL) return;
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FILE) {
                    Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
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
                    } else if (uploadMessage != null) {
                        uploadMessage.onReceiveValue(photoUri);
                        uploadMessage = null;
                    }
                } else if (requestCode == VIDEO) {
                    if (uploadMessageAboveL != null) {
                        uploadMessageAboveL.onReceiveValue(new Uri[]{videoUri});
                        uploadMessageAboveL = null;
                    } else if (uploadMessage != null) {
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
