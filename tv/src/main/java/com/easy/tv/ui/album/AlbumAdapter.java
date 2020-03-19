package com.easy.tv.ui.album;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easy.tv.R;
import com.easy.tv.bean.Album;

import java.util.List;

public class AlbumAdapter extends BaseQuickAdapter<Album, BaseViewHolder> {


    public AlbumAdapter(List data) {
        super(R.layout.album_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Album item) {
        View llContain = helper.getView(R.id.itemContain);
        ViewGroup.LayoutParams layoutParams = llContain.getLayoutParams();
        layoutParams.height = item.getHeight();
        layoutParams.width = item.getWidth();
        llContain.setLayoutParams(layoutParams);

        helper.setImageResource(R.id.ivImage, item.getUrl());
    }
}
