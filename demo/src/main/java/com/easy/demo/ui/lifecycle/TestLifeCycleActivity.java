package com.easy.demo.ui.lifecycle;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.EmptyBinding;
import com.easy.framework.base.BaseActivity;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

@ActivityInject
@Route(path = "/demo/TestLifeCycleActivity", name = "测试业务生命周期")
public class TestLifeCycleActivity extends BaseActivity<TestLifeCyclePresenter, EmptyBinding> implements TestLifeCycleView {
    String TAG = "ActivityLifecycle";

    @Override
    public int getLayoutId() {
        return R.layout.test_lifecycle;
    }

    @Override
    public void initView() {
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> {
                    Log.i(TAG, "bindLifeCycle==> Dispose");
                })
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_PAUSE)))
                .subscribe((Consumer<Long>) num -> Log.i(TAG, "bindLifeCycle==>  num:" + num), throwable -> Log.i(TAG, "bindLifeCycle==>  error"));
    }

    public void bindLifeCycle(View view) {
        presenter.clickBindLifeCycle();
    }

    public void bindUntilEvent(View view) {
        presenter.clickBindUntilEvent();
    }

    public void bindLifeCycle2(View view) {
        presenter.bindLifeCycle2();
    }

    public void goActivity(View view) {
        ARouter.getInstance().build("/demo/TransparentActivity").navigation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
