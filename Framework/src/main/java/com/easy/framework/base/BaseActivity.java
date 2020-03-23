package com.easy.framework.base;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.easy.apt.lib.InjectActivity;
import com.easy.framework.R;
import com.easy.framework.base.lifecyle.BaseLifecycleActivity;
import com.easy.framework.statusbar.StatusBarUtil;
import com.easy.net.event.ActivityEvent;
import com.easy.utils.KeyBoardUtils;
import com.easy.widget.TitleView;
import com.easy.widget.swipeback.SwipeBackActivityHelper;
import com.easy.widget.swipeback.SwipeBackLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

public abstract class BaseActivity<P extends BasePresenter, V extends ViewDataBinding> extends BaseLifecycleActivity implements BaseView<ActivityEvent> {

    public V viewBind;
    @Inject
    public P presenter;
    public Context context;
    //右滑退出
    public SwipeBackActivityHelper mHelper;
    public SwipeBackLayout mSwipeBackLayout;
    BasePopupView loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        viewBind = DataBindingUtil.setContentView(this, getLayoutId());
        initStateBar();
        setBackground(R.color.color_ffffff);
        initSwipeBackLayout();
        InjectActivity.inject(this);
        if (presenter != null) {
            presenter.attachView(this, context);
        }
        initView();
    }

    @Override
    public void onDestroy() {
        hideLoading();
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
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

    public TitleView addTitleView() {
        ViewGroup rootView = findViewById(android.R.id.content);
        if (rootView != null && rootView.getChildCount() > 0) {
            View view = rootView.getChildAt(0);
            if (view instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) view;
                TitleView titleView = new TitleView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.addView(titleView, 0, layoutParams);
                titleView.setLeftClickListener(v -> finish());
                return titleView;
            }
        }
        return null;
    }

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

    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShow()) {
            loadingDialog.dismiss();
        }
    }

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new XPopup.Builder(this).autoDismiss(false)
                    .asLoading("正在加载中")
                    .show();
        } else if (!loadingDialog.isShow()) {
            loadingDialog.show();
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

    /**
     * 点击空白地方隐藏键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            View view = getCurrentFocus();
            if (view != null) {
                KeyBoardUtils.hideKeyboard(ev, view, getApplicationContext());//调用方法判断是否需要隐藏键盘
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
