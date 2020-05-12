package com.easy.demo.ui.mvvm;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestMvvmBinding;
import com.easy.framework.base.BaseMvvmActivity;
import com.easy.framework.base.BaseViewModel;
import com.easy.loadimage.ImageConfig;
import com.easy.utils.ToastUtils;

@ActivityInject
@Route(path = "/demo/TestMvvmActivity", name = "mvvm")
public class TestMvvmActivity extends BaseMvvmActivity<TestMvvmPresenter, TestMvvmBinding> implements TestMvvmView {

    @Override
    public BaseViewModel getViewModel() {
        return ViewModelProviders.of(this).get(TestViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.test_mvvm;
    }

    @Override
    public void initView() {

    }

    @BindingAdapter({"onClickCommand", "isThrottleFirst"})
    public static void loadImage(ImageView imageView, String url, boolean isThrottleFirst) {
        ImageConfig.create(imageView.getContext()).imageView(imageView).url(url).end();
        ToastUtils.showShort("isThrottleFirst:" + isThrottleFirst);
    }
}
