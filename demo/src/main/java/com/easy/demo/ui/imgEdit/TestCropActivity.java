package com.easy.demo.ui.imgEdit;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestImgEditBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TestCropActivity", name = "图片处理")
public class TestCropActivity extends BaseActivity<EmptyPresenter, TestImgEditBinding> implements EmptyView {


    @Override
    public int getLayoutId() {
        return R.layout.test_img_edit;
    }

    @Override
    public void initView() {

    }

}
