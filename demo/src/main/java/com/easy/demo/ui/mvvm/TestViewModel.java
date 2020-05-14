package com.easy.demo.ui.mvvm;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.easy.demo.ui.mvvm.binding.BindingCommand;
import com.easy.framework.base.BaseViewModel;
import com.easy.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestViewModel extends BaseViewModel {

    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //封装一个界面发生改变的观察者
    public UIChangeObservable uiChange = new UIChangeObservable();
    //登陆按钮的是否可用
    public ObservableBoolean loginAble = new ObservableBoolean();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> passwordShowEvent = new SingleLiveEvent<>();
    }

    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(hasFocus -> {
        if (hasFocus) {
            clearBtnVisibility.set(View.VISIBLE);
        } else {
            clearBtnVisibility.set(View.INVISIBLE);
        }
    });

    //清除用户名的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand clearUserNameOnClickCommand = new BindingCommand(() -> userName.set(""));

    //密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
    public BindingCommand passwordShowCommand = new BindingCommand(() -> {
        //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
        boolean change = uiChange.passwordShowEvent.getValue() == null || !uiChange.passwordShowEvent.getValue();
        Log.d("passwordShowCommand", "点击了change=" + change);
        uiChange.passwordShowEvent.setValue(change);
    });

    public BindingCommand<String> onTextChangedCommand = new BindingCommand<>(isEmpty -> {
        if (TextUtils.isEmpty(userName.get()) || TextUtils.isEmpty(password.get())) {
            loginAble.set(false);
        } else {
            loginAble.set(true);
        }
    });

    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(() -> login());

    private void login() {
        if (TextUtils.isEmpty(userName.get())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        //RaJava模拟登录
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(() -> {
                    Log.i("login", "bindLifeCycle==> Dispose");
                }).as(getAutoDispose())
                .subscribe(num -> {
                    Log.i("login", "bindLifeCycle==>  num:" + num);
                }, throwable -> Log.i("login", "bindLifeCycle==>  error"));

    }
}
