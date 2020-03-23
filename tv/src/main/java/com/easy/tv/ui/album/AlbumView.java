package com.easy.tv.ui.album;

import com.easy.tv.base.CommonRespond;
import com.easy.framework.base.BaseView;
import com.easy.net.event.LifecycleEvent;
import com.easy.tv.bean.Album;

import java.util.List;

public interface AlbumView<E extends LifecycleEvent> extends BaseView<E> {

    void albumCallback(CommonRespond<List<Album>> respond);
}
