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
        StringBuilder builder = new StringBuilder();
        builder.append("1.支持切换域名").append("\n");
        builder.append("2.支持解析JSON结果").append("\n");
        builder.append("3.支持返回不是JSON结果").append("\n");
        builder.append("4.自动取消请求--绑定生命周期").append("\n");
        builder.append("5.支持Cookies").append("\n");
        builder.append("6.支持离线缓存--可设置缓存时间").append("\n");
        viewBind.tvFunction.setText(builder.toString());
    }

    public void testLifeCycle(View v) {
        presenter.testLifeCycle();
    }

    public void testJson(View v) {
        presenter.testJsonData();
    }

    public void testNoJson(View v) {
        presenter.testNoJsonData();
    }

    public void testHostGroup(View v) {
        presenter.testHostGroup();
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
