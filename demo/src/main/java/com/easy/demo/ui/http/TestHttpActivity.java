package com.easy.demo.ui.http;

import android.view.View;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestHttpBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxHttp;
import com.easy.net.beans.Response;
import com.easy.net.callback.RHttpCallback;
import com.easy.net.exception.ApiException;
import com.easy.utils.ToastUtils;

import java.util.TreeMap;

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
}
