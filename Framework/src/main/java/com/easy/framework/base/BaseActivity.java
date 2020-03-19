package com.easy.framework.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.easy.framework.R;
import com.easy.framework.base.lifecyle.BaseLifecycleActivity;
import com.easy.framework.statusbar.StatusBarUtil;
import com.easy.framework.swipeback.SwipeBackActivityHelper;
import com.easy.framework.swipeback.SwipeBackLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

public abstract class BaseActivity<V extends ViewDataBinding> extends BaseLifecycleActivity {
    public Context context;
    public V viewBind;
    public boolean supportMvp;//是否支持MVP

    public SwipeBackActivityHelper mHelper;
    public SwipeBackLayout mSwipeBackLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setOrientation();
        super.onCreate(savedInstanceState);
        context = this;
        viewBind = DataBindingUtil.setContentView(this, getLayoutId());
        initStateBar();
        setBackground(R.color.color_ffffff);
        initSwipeBackLayout();
        if (!supportMvp) {
            initView();
        }
    }
    public void setOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void setBackground(int id) {
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.setBackgroundResource(id);
        }
    }

    public RxPermissions getRxPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);
        return rxPermissions;
    }

    public abstract int getLayoutId();

    public abstract void initView();

    /**
     * 初始化左滑关闭
     */
    public void initSwipeBackLayout() {
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        mSwipeBackLayout = mHelper.getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mHelper != null) {
            mHelper.onPostCreate();
        }
    }

    public void closeSwipeBackLayout() {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setEnableGesture(false);
        }
    }

    public void openSwipeBackLayout() {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setEnableGesture(true);
        }
    }

    /**
     * 设置自定义状态颜色
     */
    public void initStateBar() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        } else {
            StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));
        }
    }
}
