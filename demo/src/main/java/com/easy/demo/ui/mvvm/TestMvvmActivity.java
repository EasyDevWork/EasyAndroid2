package com.easy.demo.ui.mvvm;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestMvvmBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.loadimage.ImageConfig;
import com.easy.net.event.ActivityEvent;

@ActivityInject
@Route(path = "/demo/TestMvvmActivity", name = "mvvm")
public class TestMvvmActivity extends BaseActivity<TestMvvmPresenter, TestMvvmBinding> implements TestMvvmView<ActivityEvent> {

    TestViewModel testViewModel;

    @Override
    public int getLayoutId() {
        return R.layout.test_mvvm;
    }

    @Override
    public void initView() {
        testViewModel = new TestViewModel(viewBind);
    }

    @BindingAdapter({"onClickCommand"})
    public static void loadImage(ImageView imageView, String url) {
        ImageConfig.create(imageView.getContext()).imageView(imageView).url(url).end();
    }
}
