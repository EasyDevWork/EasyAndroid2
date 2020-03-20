package com.easy.demo.ui.empty;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.common.base.CommonActivity;
import com.easy.demo.R;
import com.easy.demo.databinding.EmptyBinding;
import com.easy.net.rxlifecycle.ActivityEvent;

@ActivityInject
@Route(path = "/demo/EmptyActivity", name = "空页面")
public class EmptyActivity extends CommonActivity<EmptyPresenter, EmptyBinding> implements EmptyView<ActivityEvent> {

    @Override
    public int getLayoutId() {
        return R.layout.empty;
    }

    @Override
    public void initView() {

    }
}
