package com.easy.demo.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.easy.demo.R;
import com.easy.framework.base.BaseActivity;
import com.easy.framework.base.BasePresenter;
import com.easy.tv.focusBorder.FocusBorder;

public abstract class TvBaseActivity<P extends BasePresenter, V extends ViewDataBinding> extends BaseActivity<P, V> {
    public FocusBorder mFocusBorder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mFocusBorder = new FocusBorder.Builder()
                .asColor()
                .borderColorRes(R.color.actionbar_color)
                .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 3f)
                .shadowColorRes(R.color.green_bright)
                .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 5f)
                .build(this);
    }

    public void onMoveFocusBorder(View focusedView, float scale) {
        if (null != mFocusBorder) {
            mFocusBorder.onFocus(focusedView, FocusBorder.OptionsFactory.get(scale, scale));
        }
    }
}
