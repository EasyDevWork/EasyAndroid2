package com.easy.demo.ui.album;


import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.base.TvBaseActivity;
import com.easy.demo.bean.Album;
import com.easy.demo.databinding.TestAlbumBinding;
import com.easy.libtv.SimpleOnItemListener;
import com.easy.libtv.TvRecyclerView;
import com.easy.net.event.ActivityEvent;
import com.easy.utils.ToastUtils;

import java.util.List;

@ActivityInject
@Route(path = "/demo/AlbumActivity", name = "相册列表")
public class TestAlbumActivity extends TvBaseActivity<TestAlbumPresenter, TestAlbumBinding> implements TestAlbumView<ActivityEvent> {

    TestAlbumAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.test_album;
    }

    @Override
    public void initView() {
        super.initView();

        adapter = new TestAlbumAdapter(this);
        viewBind.recyclerView.setAdapter(adapter);
        viewBind.recyclerView.setSpacingWithMargins(30, 35);
        viewBind.recyclerView.setOnFocusChangeListener((v, hasFocus) -> mFocusBorder.setVisible(hasFocus));
        viewBind.recyclerView.setOnItemListener(new SimpleOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.1f);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                Album album = adapter.getItem(position);
                ToastUtils.showShort(album.getName());
            }
        });
        presenter.loadData();
    }

    @Override
    public void albumCallback(List<Album> respond) {
        adapter.setNewData(respond);
        adapter.notifyDataSetChanged();
        viewBind.recyclerView.getNextFocusDownId();
    }
}
