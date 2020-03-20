package com.easy.net.download;

public interface IDownload {
    void insertOrUpdate(DownloadInfo downloadInfo);

    void delete(DownloadInfo downloadInfo);
}
