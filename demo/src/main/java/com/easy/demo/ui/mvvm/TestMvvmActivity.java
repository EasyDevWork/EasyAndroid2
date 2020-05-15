package com.easy.demo.ui.mvvm;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.demo.BR;
import com.easy.demo.R;
import com.easy.demo.databinding.TestMvvmBinding;
import com.easy.framework.base.BaseMvvmActivity;
import com.easy.framework.bean.AppVersion;
import com.easy.net.RxHttp;
import com.easy.net.beans.Response;
import com.easy.net.callback.RHttpCallback;
import com.easy.net.exception.ApiException;
import com.easy.utils.ToastUtils;

import java.util.TreeMap;


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

    public void testCache(View v) {
        TreeMap<String, Object> request = new TreeMap<>();
        RxHttp.get("http://publicobject.com/helloworld.txt")
                .addParameter(request)
                .addAutoDispose(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .request(new RHttpCallback<AppVersion>(AppVersion.class) {
                    @Override
                    public void handleSuccess(Response response) {
                        ToastUtils.showShort("success");
                    }

                    @Override
                    public void handleCancel() {
                        ToastUtils.showShort("cancel");
                    }

                    @Override
                    public void handleError(ApiException exception) {
                        ToastUtils.showShort(exception.toString());
                    }
                });
    }
}
