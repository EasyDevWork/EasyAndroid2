package com.easy.store.dao;

import com.easy.store.bean.DownloadDo;
import com.easy.store.bean.DownloadDo_;

import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;

public class DownloadDao extends BaseDao {

    Box<DownloadDo> box;

    @Inject
    public DownloadDao() {
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

    public List<DownloadDo> query(int type) {
        if (box != null) {
            return box.query().equal(DownloadDo_.type, type).build().find();
        }
        return null;
    }

    public DownloadDo query(String tag) {
        if (box != null) {
            return box.query().equal(DownloadDo_.tag, tag).build().findFirst();
        }
        return null;
    }

    public void insertOrUpdate(DownloadDo downloadDo) {
        if (box != null) {
            box.put(downloadDo);
        }
    }

    public void delete(DownloadDo temp) {
        if (box != null) {
            DownloadDo downloadDo = query(temp.getTag());
            if (downloadDo != null) {
                box.remove(downloadDo);
            }
        }
    }

    public void deleteAll() {
        if (box != null) {
            box.removeAll();
        }
    }

    public void delete(List<DownloadDo> downloadDos) {
        if (box != null) {
            box.remove(downloadDos);
        }
    }
}
