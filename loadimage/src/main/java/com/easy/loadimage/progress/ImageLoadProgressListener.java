package com.easy.loadimage.progress;

public interface ImageLoadProgressListener {
    void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes);
}
