package com.easy.demo.ui.album;

import android.content.Context;

import com.easy.framework.base.BasePresenter;
import com.easy.demo.bean.Album;
import com.easy.utils.DimensUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TestAlbumPresenter extends BasePresenter<TestAlbumView> {
    @Inject
    public TestAlbumPresenter() {

    }

    public void loadData() {
        Context context = getContext();
        int[] screens = DimensUtils.getWidthHeight(context);
        int width = (screens[0] - DimensUtils.dp2px(context, 100)) / 3;
        int height = (int) (screens[1] - DimensUtils.dp2px(context, 100) / 1.5);
        int size = width < height ? width : height;

        List<Album> albums = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            Album album = new Album();
            album.setWidth(size);
            album.setName("name_" + i);
            album.setHeight(size);
            album.setUrl("http://img2.imgtn.bdimg.com/it/u=3137891603,2800618441&fm=26&gp=0.jpg");
            albums.add(album);
        }
        mvpView.albumCallback(albums);
    }
}