package com.easy.demo.ui.lifecycle;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.EmptyBinding;
import com.easy.framework.base.BaseActivity;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

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

    }

    @SuppressLint("CheckResult")
    public void bindLifeCycle(View view) {
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> Log.i(TAG, "bindLifeCycle==> Dispose"))
                .compose(this.bindToLifecycle())
                .subscribe(num -> Log.i(TAG, "bindLifeCycle==>  num:" + num));
    }

    @SuppressLint("CheckResult")
    public void bindUntilEvent(View view) {
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> Log.i(TAG, "bindLifeCycle==> Dispose"))
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(num -> Log.i(TAG, "bindLifeCycle==>  num:" + num));
    }

    public void goActivity(View view){
        ARouter.getInstance().build("/demo/TransparentActivity").navigation();
    }
}
