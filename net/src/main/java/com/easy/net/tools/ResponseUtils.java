package com.easy.net.tools;


import com.easy.net.download.Download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;

/**
 * 响应工具类
 */
public class ResponseUtils {


    private static ResponseUtils instance = null;

    private ResponseUtils() {
    }

    public static ResponseUtils get() {
        if (instance == null) {
            synchronized (ResponseUtils.class) {
                if (instance == null) {
                    instance = new ResponseUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 下载文件到本地
     *
     * @param responseBody 响应体
     * @param file         目标文件
     * @param download     下载实体类
     */
    public void download2LocalFile(ResponseBody responseBody, File file, Download download) throws IOException {
        try {
            RandomAccessFile randomAccessFile = null;
            FileChannel channelOut = null;
            InputStream inputStream = null;
            try {
                //创建文件夹
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                //初始化
                inputStream = responseBody.byteStream();
                randomAccessFile = new RandomAccessFile(file, "rwd");
                channelOut = randomAccessFile.getChannel();
                //总长度
                long allLength = download.getDownloadDo().getTotalSize() == 0 ? responseBody.contentLength() : download.getDownloadDo().getCurrentSize() + responseBody.contentLength();

                MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, download.getDownloadDo().getCurrentSize(), allLength - download.getDownloadDo().getCurrentSize());

                byte[] buffer = new byte[1024 * 4];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    mappedBuffer.put(buffer, 0, length);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }


}
