package com.easy.framework.even;

import android.net.Uri;
import android.webkit.ValueCallback;

public class ChooseFileEvent {

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;

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
    }

    @Override
    public String toString() {
        return "ChooseFileEvent{" +
                "uploadMessage=" + uploadMessage +
                ", uploadMessageAboveL=" + uploadMessageAboveL +
                '}';
    }
}
