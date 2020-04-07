package com.easy.demo.ui.album;

import com.easy.demo.bean.Album;
import com.easy.framework.base.BaseView;

import java.util.List;

public interface TestAlbumView extends BaseView {

    void albumCallback(List<Album> respond);
}
