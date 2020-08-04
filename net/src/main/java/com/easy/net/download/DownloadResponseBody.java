package com.easy.net.download;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * 下载ResponseBody
 */
public class DownloadResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private DownloadProgressCallback callback;
    private BufferedSource bufferedSource;
    private long readBytesCount = 0L;
    private long totalBytesCount = 0L;
    Handler handler;
    public static long delayedTime = 100;//500毫秒更新一次

    public DownloadResponseBody(ResponseBody responseBody, DownloadProgressCallback callback, Handler handler) {
        this.responseBody = responseBody;
        this.callback = callback;
        this.handler = handler;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            ForwardingSource forwardingSource = new ForwardingSource(responseBody.source()) {
                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    readBytesCount += bytesRead != -1 ? bytesRead : 0;
                    if (totalBytesCount == 0) {
                        totalBytesCount = contentLength();
                    }
//                    Log.d("DownloadResponseBody", "download progress readBytesCount:" + readBytesCount + "  totalBytesCount:" + totalBytesCount);
                    return bytesRead;
                }
            };
            if (callback != null) {
                handler.postDelayed(getRunnable(), 500);
            }
            bufferedSource = Okio.buffer(forwardingSource);
        }
        return bufferedSource;
    }

    public Runnable getRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                if (callback != null && !callback.isFinish()) {
                    callback.progress(readBytesCount, totalBytesCount);
                    Log.d("DownloadResponseBody", "handler 500ms后发送");
                    handler.postDelayed(this, delayedTime);
                } else {
                    handler.removeCallbacks(this);
                    Log.d("DownloadResponseBody", "移除handler");
                }
            }
        };
    }
}
