package com.easy.tv.base;

import android.content.pm.ActivityInfo;
import android.util.TypedValue;
import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.easy.common.base.CommonActivity;
import com.easy.common.base.CommonPresenter;
import com.easy.tv.R;
import com.owen.focus.FocusBorder;

public abstract class TvBaseActivity<P extends CommonPresenter, V extends ViewDataBinding> extends CommonActivity<P, V> {
    public FocusBorder mFocusBorder;

    @Override
    public void setOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
