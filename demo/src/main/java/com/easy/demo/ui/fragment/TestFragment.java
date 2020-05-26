package com.easy.demo.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Lifecycle;

import com.easy.apt.annotation.FragmentInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestFragmentBinding;
import com.easy.framework.base.BaseFragment;
import com.easy.utils.ToastUtils;

@FragmentInject
public class TestFragment extends BaseFragment<TestFragmentPresenter, TestFragmentBinding> implements TestFragmentView {

    @Override
    public int getLayoutId() {
        return R.layout.test_fragment;
    }

    @Override
    public void initView(View view) {
        viewBind.tvName.setText(tag + "====>");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle = getArguments();
        if (bundle != null) {
            tag = bundle.getString("type");
        }
    }

    public void btn3(View view) {
        showLoading();
    }

    public void btn4(View view) {
        getRxPermissions().request(Manifest.permission.CAMERA)
                .as(getAutoDispose())
                .subscribe(aBoolean -> ToastUtils.showShort("是否允许：" + aBoolean));
    }
}
