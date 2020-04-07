package com.easy.demo.ui.transparent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TransparentBinding;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TransparentActivity", name = "透明页面")
public class TransparentActivity extends BaseActivity<TransparentPresenter, TransparentBinding> implements TransparentView {

    @Override
    public int getLayoutId() {
        return R.layout.transparent;
    }

    @Override
    public void initView() {

    }

    @Override
    public void setBackground(int id) {
//        super.setBackground(id);
    }
}
