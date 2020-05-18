package com.easy.demo.ui.http;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestHttpBinding;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TestHttpActivity", name = "HTTP测试")
public class TestHttpActivity extends BaseActivity<TestHttpPresenter, TestHttpBinding> implements TestHttpView {

    @Override
    public int getLayoutId() {
        return R.layout.test_http;
    }

    @Override
    public void initView() {

    }

    public void testJson(View v) {
        presenter.testJsonData();
    }

    public void testNoJson(View v) {
        presenter.testNoJsonData();
    }

    @Override
    public void callback(String content, Exception e) {
        if (e != null) {
            viewBind.testShow.setText(e.getMessage());
        } else {
            viewBind.testShow.setText(content);
        }
    }
}
