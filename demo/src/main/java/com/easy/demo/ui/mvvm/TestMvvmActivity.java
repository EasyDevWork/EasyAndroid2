package com.easy.demo.ui.mvvm;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.demo.BR;
import com.easy.demo.R;
import com.easy.demo.databinding.TestMvvmBinding;
import com.easy.framework.base.BaseMvvmActivity;

@Route(path = "/demo/TestMvvmActivity", name = "mvvm")
public class TestMvvmActivity extends BaseMvvmActivity<TestViewModel, TestMvvmBinding> implements TestMvvmView {

    @Override
    public int getLayoutId() {
        return R.layout.test_mvvm;
    }

    @Override
    public int initVariableId() {
        return BR.loginModel;
    }

    @Override
    public void initView() {
        viewModel.uiChange.passwordShowEvent.observe(this, aBoolean -> {
            Log.d("passwordShowCommand", "收到change=" + aBoolean);
            if (aBoolean) {
                viewBind.ivSwichPasswrod.setImageResource(R.drawable.show_psw);
                viewBind.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                //密码不可见
                viewBind.ivSwichPasswrod.setImageResource(R.drawable.show_psw_press);
                viewBind.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }
}
