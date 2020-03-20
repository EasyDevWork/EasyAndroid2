package com.easy.store.dao;

import com.easy.net.download.DownloadInfo;
import com.easy.net.download.IDownload;
import com.easy.store.bean.DownloadDo;
import com.easy.store.bean.DownloadDo_;

import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;

public class DownloadInfoDao extends BaseDao implements IDownload {

    Box<DownloadDo> box;

    @Inject
    public DownloadInfoDao() {
        if (boxStore != null) {
            box = boxStore.boxFor(DownloadDo.class);
        }
    }

    public List<DownloadDo> query() {
        if (box != null) {
            return box.query().build().find();
        }
        return null;
    }

    public DownloadDo query(String tag) {
        if (box != null) {
            return box.query().equal(DownloadDo_.downloadTag, tag).build().findFirst();
        }
        return null;
    }

    @Override
    public void insertOrUpdate(DownloadInfo downloadInfo) {
        if (box != null) {
            DownloadDo downloadDo = query(downloadInfo.getTag());
            if (downloadDo == null) {
                downloadDo = new DownloadDo();
                downloadDo.setDownloadTag(downloadInfo.getTag());
            }
            downloadDo.setCurrentSize(downloadInfo.getCurrentSize());
            downloadDo.setLocalUrl(downloadInfo.getLocalUrl());
            downloadDo.setServerUrl(downloadInfo.getServerUrl());
            downloadDo.setState(downloadInfo.getState());
            downloadDo.setTotalSize(downloadInfo.getTotalSize());
            box.put(downloadDo);
        }
    }

    @Override
    public void delete(DownloadInfo downloadInfo) {
        if (box != null) {
            DownloadDo downloadDo = query(downloadInfo.getTag());
            if (downloadDo != null) {
                box.remove(downloadDo);
            }
        }
    }
}
