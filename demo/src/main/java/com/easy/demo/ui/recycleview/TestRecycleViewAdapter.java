package com.easy.demo.ui.recycleview;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easy.demo.R;
import com.easy.demo.bean.TestDo;
import com.easy.loadimage.EasyLoadImage;

public class TestRecycleViewAdapter extends BaseQuickAdapter<TestDo, BaseViewHolder> {
    Context context;

    public TestRecycleViewAdapter(Context context) {
        super(R.layout.test_recycle_view_item);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TestDo item) {
        helper.setText(R.id.tvName, item.getName());
        EasyLoadImage.loadImage(context,item.getImage(),helper.getView(R.id.ivImage));
    }
}
