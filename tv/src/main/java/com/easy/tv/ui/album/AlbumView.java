package com.easy.tv.ui.album;

import com.easy.common.base.CommonView;
import com.easy.common.bean.CommonRespond;
import com.easy.framework.rxlifecycle.LifecycleEvent;
import com.easy.tv.bean.Album;

import java.util.List;

public interface AlbumView<E extends LifecycleEvent> extends CommonView<E> {

    void albumCallback(CommonRespond<List<Album>> respond);
}
