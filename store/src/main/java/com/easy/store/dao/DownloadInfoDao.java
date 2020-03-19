package com.easy.store.dao;

import com.easy.store.bean.DownloadInfo;

import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;

public class DownloadInfoDao extends BaseDao {

    Box<DownloadInfo> box;

    @Inject
    public DownloadInfoDao() {
        if (boxStore != null) {
            box = boxStore.boxFor(DownloadInfo.class);
        }
    }

    public void insertOrUpdate(DownloadInfo downloadInfo) {
        if (box != null) {
            box.put(downloadInfo);
        }
    }

    public void delete(DownloadInfo downloadInfo) {
        if (box != null) {
            box.remove(downloadInfo);
        }
    }

    public List<DownloadInfo> query() {
        if (box != null) {
            return box.query().build().find();
        }
        return null;
    }
}
