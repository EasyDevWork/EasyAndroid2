package com.easy.common.base;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.databinding.ViewDataBinding;

import com.easy.common.view.TitleView;
import com.easy.framework.base.mvp.BaseMvpActivity;
import com.easy.framework.rxlifecycle.ActivityEvent;
import com.easy.framework.utils.KeyBoardUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

public abstract class CommonActivity<P extends CommonPresenter, V extends ViewDataBinding> extends BaseMvpActivity<P, V> implements CommonView<ActivityEvent> {

    BasePopupView loadingDialog;

    private void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShow()) {
            loadingDialog.dismiss();
        }
    }

    private void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new XPopup.Builder(this).autoDismiss(false)
                    .asLoading("正在加载中")
                    .show();
        } else if (!loadingDialog.isShow()) {
            loadingDialog.show();
        }
    }

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

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void onDestroy() {
        dismissLoadingDialog();
        super.onDestroy();
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
