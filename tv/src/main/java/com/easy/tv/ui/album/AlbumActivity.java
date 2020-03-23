package com.easy.tv.ui.album;

import android.view.View;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.tv.base.CommonRespond;
import com.easy.libtv.SimpleOnItemListener;
import com.easy.libtv.TvRecyclerView;
import com.easy.net.event.ActivityEvent;
import com.easy.tv.R;
import com.easy.tv.base.TvBaseActivity;
import com.easy.tv.bean.Album;
import com.easy.tv.databinding.AlbumBinding;
import com.easy.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

@ActivityInject
@Route(path = "/tv/AlbumActivity", name = "相册列表")
public class AlbumActivity extends TvBaseActivity<AlbumPresenter, AlbumBinding> implements AlbumView<ActivityEvent> {

    AlbumAdapter adapter;
    List<Album> datas = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.album;
    }

    @Override
    public void initView() {
        super.initView();

        adapter = new AlbumAdapter(datas);
        viewBind.recyclerView.setSpacingWithMargins(10, 3);
        viewBind.recyclerView.setAdapter(adapter);
        viewBind.recyclerView.setOnFocusChangeListener((v, hasFocus) -> mFocusBorder.setVisible(hasFocus));
        viewBind.recyclerView.setOnItemListener(new SimpleOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.1f);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                Album album = datas.get(position);
                ToastUtils.showShort(album.getName());
            }
        });
        presenter.loadData();
    }

    @Override
    public void albumCallback(CommonRespond<List<Album>> respond) {
        if (respond.getCode() == 0 && respond.getObj() != null) {
            datas.clear();
            datas.addAll(respond.getObj());
            adapter.notifyDataSetChanged();
            viewBind.recyclerView.getNextFocusDownId();
        }
    }
}
