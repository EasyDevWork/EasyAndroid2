package com.easy.tv.ui.albumdetail;

import android.view.View;

import com.easy.apt.annotation.FragmentInject;
import com.easy.net.rxlifecycle.FragmentEvent;
import com.easy.tv.R;
import com.easy.tv.base.TvBaseDialogFragment;
import com.easy.tv.databinding.AlbumBinding;

@FragmentInject
public class AlbumDetailFragment extends TvBaseDialogFragment<AlbumDetailPresenter, AlbumBinding> implements AlbumDetailView<FragmentEvent> {


    @Override
    public void initView(View view) {
        super.initView(view);

    }


    @Override
    public int getLayoutId() {
        return R.layout.album_detail;
    }


}
