package com.easy.tv.ui.album;

import android.content.Context;

import com.easy.tv.base.CommonRespond;
import com.easy.framework.base.BasePresenter;
import com.easy.tv.bean.Album;
import com.easy.utils.DimensUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AlbumPresenter extends BasePresenter<AlbumView> {
    @Inject
    public AlbumPresenter() {

    }

    public void loadData() {
        Context context = getContext();
        int[] screens = DimensUtils.getWidthHeight(context);
        int width = (screens[0] - DimensUtils.dp2px(context, 100)) / 3;
        int height = (int) (screens[1] - DimensUtils.dp2px(context, 100) / 1.5);
        int size = width < height ? width : height;

        List<Album> albums = new ArrayList<>();
        String packageName = context.getPackageName();
        for (int i = 0; i < 13; i++) {
            Album album = new Album();
            int pic = getContext().getResources().getIdentifier("pic_" + i, "drawable", packageName);
            album.setWidth(size);
            album.setHeight(size);
            album.setUrl(pic);
            albums.add(album);
        }
        CommonRespond respond = new CommonRespond();
        respond.setCode(0);
        respond.setObj(albums);
        mvpView.albumCallback(respond);
    }
}