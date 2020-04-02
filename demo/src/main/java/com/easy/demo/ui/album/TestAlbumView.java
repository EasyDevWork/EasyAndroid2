package com.easy.demo.ui.album;

import com.easy.framework.base.BaseView;
import com.easy.net.event.LifecycleEvent;
import com.easy.demo.bean.Album;

import java.util.List;

public interface TestAlbumView<E extends LifecycleEvent> extends BaseView<E> {

    void albumCallback(List<Album> respond);
}
