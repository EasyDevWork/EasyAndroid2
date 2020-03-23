package com.easy.demo.ui.status_bar;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestStatusBarBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.framework.statusbar.StatusBarUtil;
import com.easy.net.event.ActivityEvent;

@ActivityInject
@Route(path = "/demo/StatusBarActivity", name = "状态栏页面")
public class StatusBarActivity extends BaseActivity<StatusBarPresenter, TestStatusBarBinding> implements StatusBarView<ActivityEvent> {

    int i = 0;

    @Override
    public int getLayoutId() {
        return R.layout.test_status_bar;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initStateBar() {
        //沉浸式重新该方法
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
    }

    public void btn1(View view) {
        i++;
        if (i % 2 == 0) {
            StatusBarUtil.setTranslucentStatus(this);
            StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        } else {
            StatusBarUtil.setRootViewFitsSystemWindows(this, true);
            StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.color_c83c3c));
        }
    }

    public void btn2(View view) {
        i++;
        if (i % 2 == 0) {
            StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.color_c83c3c));
        } else {
            StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.color_008577));
        }
    }
}
