package com.easy.demo.ui.recycleview;

import android.graphics.Color;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.common.base.CommonActivity;
import com.easy.common.view.FootView;
import com.easy.common.view.HeadView;
import com.easy.common.view.StateView;
import com.easy.demo.R;
import com.easy.demo.bean.TestDo;
import com.easy.demo.databinding.TestRecycleViewBinding;
import com.easy.net.beans.Response;
import com.easy.net.rxlifecycle.ActivityEvent;
import com.easy.framework.utils.DimensUtils;
import com.easy.framework.utils.ToastUtils;
import com.easy.framework.utils.Utils;
import com.gavin.com.library.StickyDecoration;
import com.gavin.com.library.listener.GroupListener;

import java.util.ArrayList;
import java.util.List;

@ActivityInject
@Route(path = "/demo/TestRecycleViewActivity", name = "RecycleView测试")
public class TestRecycleViewActivity extends CommonActivity<TestRecycleViewPresenter, TestRecycleViewBinding> implements TestRecycleView<ActivityEvent> {
//    TestRecycleViewAdapter adapter;
    TestRecycleViewMultipleAdapter adapter;
    StateView emptyView;
    int page = 0;

    @Override
    public int getLayoutId() {
        return R.layout.test_recycle_view;
    }

    @Override
    public void initView() {
//        adapter = new TestRecycleViewAdapter(this);
        adapter = new TestRecycleViewMultipleAdapter(new ArrayList<>(),this);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);

        //add emptyView
        emptyView = new StateView(this);
        emptyView.setOnClickListener(view -> {
            showLoading();
            presenter.loadData(page);
        });

        //add headView
        HeadView headView = new HeadView(this);
        adapter.addHeaderView(headView);
//
//        //add footView
        FootView footView = new FootView(this);
        adapter.addFooterView(footView);

        //加载更多
        adapter.setOnLoadMoreListener(() -> {
            page++;
            presenter.loadData(page);
        }, viewBind.recyclerView);

        //点击
        adapter.setOnItemClickListener((adapter, view, position) -> {
            TestDo testDo = (TestDo) adapter.getData().get(position);
            ToastUtils.showShort(position + ":" + testDo.getName());
        });

        viewBind.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewBind.recyclerView.setHasFixedSize(true);
        viewBind.recyclerView.addItemDecoration(getStickyDecoration());//item标题吸顶
        viewBind.recyclerView.setAdapter(adapter);

        //下拉
        viewBind.swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 0;
            presenter.loadData(page);
        });

        showLoading();
        //为了看到加载框延迟1秒
        viewBind.recyclerView.postDelayed(() -> presenter.loadData(page), 1000);
    }

    private StickyDecoration getStickyDecoration() {
        //点击事件，返回当前分组下的第一个item的position
        return StickyDecoration.Builder
                .init(new GroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        //组名回调
                        List<TestDo> testDos = adapter.getData();
                        if (testDos.size() > position && position > -1) {
                            return testDos.get(position).getTitle();
                        }
                        return null;
                    }
                })
                .setGroupBackground(Color.parseColor("#F5F5F5"))        //背景色
                .setGroupHeight(DimensUtils.dp2px(this, 36))     //高度
                .setDivideColor(Color.parseColor("#666666"))            //分割线颜色
//                .setDivideHeight(DensityUtil.dip2px(getContext(), 1))     //分割线高度 (默认没有分割线)
                .setGroupTextColor(Color.parseColor("#888888"))                                    //字体颜色 （默认）
                .setGroupTextSize(DimensUtils.sp2px(this, 14))    //字体大小
                .setTextSideMargin(DimensUtils.dp2px(this, 15))  // 边距   靠左时为左边距  靠右时为右边距
                //.setHeaderCount(2)                                            // header数量（默认0）
                .setOnClickListener((position, id) -> {
                    ToastUtils.showShort("Group点击事件:" + id);//Group点击事件
                })
                .build();
    }

    @Override
    public void dataCallback(Response<List<TestDo>> response) {
        hideLoading();
        if (page == 0) {
            viewBind.swipeRefreshLayout.setRefreshing(false);
        } else {
            adapter.loadMoreComplete();
        }
        if (response.getCode() == Response.SUCCESS_STATE) {
            if (response.getResultObj() != null) {
                if (page == 0) {
                    adapter.setNewData(response.getResultObj());
                } else {
                    adapter.addData(response.getResultObj());
                }
            }
            if (Utils.isEmpty(adapter.getData())) {
                emptyView.setTips(getString(R.string.no_date));
                adapter.setEmptyView(emptyView);
            }
        } else {//错误的时候
            if (Utils.isEmpty(adapter.getData())) {
                //没数据显示错误信息
                emptyView.setTips(response.getMsg());
                adapter.setEmptyView(emptyView);
            } else {
                //已经有数据吐司提示错误信息
                ToastUtils.showShort(response.getMsg());
            }
        }
    }
}
