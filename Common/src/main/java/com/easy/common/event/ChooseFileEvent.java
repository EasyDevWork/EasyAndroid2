package com.easy.common.event;

import android.net.Uri;
import android.webkit.ValueCallback;

public class ChooseFileEvent {

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private boolean isFeedBack;//是否是意见反馈，如果是，不显示录像选项

    public boolean isFeedBack() {
        return isFeedBack;
    }

    public ValueCallback<Uri> getUploadMessage() {
        return uploadMessage;
    }

    public void setUploadMessage(ValueCallback<Uri> uploadMessage) {
        this.uploadMessage = uploadMessage;
    }

    public ValueCallback<Uri[]> getUploadMessageAboveL() {
        return uploadMessageAboveL;
    }

    public void setUploadMessageAboveL(ValueCallback<Uri[]> uploadMessageAboveL) {
        this.uploadMessageAboveL = uploadMessageAboveL;
    }

    public ChooseFileEvent(ValueCallback<Uri> uploadMessage, ValueCallback<Uri[]> uploadMessageAboveL) {
        this.uploadMessage = uploadMessage;
        this.uploadMessageAboveL = uploadMessageAboveL;
        isFeedBack = false;
    }


    public ChooseFileEvent(ValueCallback<Uri> uploadMessage, ValueCallback<Uri[]> uploadMessageAboveL, boolean isFeedBack) {
        this.uploadMessage = uploadMessage;
        this.uploadMessageAboveL = uploadMessageAboveL;
        this.isFeedBack = isFeedBack;
    }

    @Override
    public String toString() {
        return "ChooseFileEvent{" +
                "uploadMessage=" + uploadMessage +
                ", uploadMessageAboveL=" + uploadMessageAboveL +
                '}';
    }
}
