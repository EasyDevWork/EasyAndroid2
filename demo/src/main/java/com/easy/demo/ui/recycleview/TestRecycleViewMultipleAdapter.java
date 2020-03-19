package com.easy.demo.ui.recycleview;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easy.demo.R;
import com.easy.demo.bean.TestDo;

import java.util.List;

public class TestRecycleViewMultipleAdapter extends BaseMultiItemQuickAdapter<TestDo, BaseViewHolder> {
    Context context;

    public TestRecycleViewMultipleAdapter(List<TestDo> datas, Context context) {
        super(datas);
        this.context = context;

        addItemType(0, R.layout.test_recycle_view_item_red);
        addItemType(1, R.layout.test_recycle_view_item_green);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestDo item) {

        if (item.getType() == 0) {
            helper.setText(R.id.tvRedName, item.getName());
        } else {
            helper.setText(R.id.tvGreenName, item.getName());
        }
    }
}
