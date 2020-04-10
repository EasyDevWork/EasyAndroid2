package com.easy.demo.ui.mvvm;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import com.easy.demo.bean.TestMvvm;
import com.easy.demo.bean.TestMvvm2;
import com.easy.demo.databinding.TestMvvmBinding;
import com.easy.store.bean.Accounts;
import com.easy.store.dao.AccountsDao;
import com.easy.utils.DimensUtils;

public class TestViewModel extends ViewModel {
    TestMvvm testMvvm;
    TestMvvm2 testM2;
    int i = 12, j = 0;
    Context context;
    Accounts accounts;
    LifecycleOwner owner;
    TestMvvmBinding viewDataBinding;
    AccountsDao accountsDao;

    public void init(TestMvvmBinding viewDataBinding, LifecycleOwner owner) {
        this.viewDataBinding = viewDataBinding;
        this.owner = owner;
        testMvvm = new TestMvvm();
        viewDataBinding.setTestMvvm(testMvvm);

        testM2 = new TestMvvm2();
        viewDataBinding.setTestM2(testM2);
        viewDataBinding.setTestVm(this);

        context = viewDataBinding.getRoot().getContext();

        accountsDao = new AccountsDao();
        getAccountsLiveData();
    }

    public void getAccountsLiveData() {
        accountsDao.getAccountsLiveData().observe(owner, accounts -> {
            if (accounts != null && accounts.size() > 0) {
                viewDataBinding.setAccount(accounts.get(0));
            }
        });

    }

    /**
     * 直接引用--参数必须是引用要用的参数
     *
     * @param view
     */
    public void clickChangeNameBtn(View view) {
        i++;
        testM2.setName("testM2_" + i);
        testMvvm.setName("testMvvm_" + i);
        testM2.setSize(DimensUtils.dp2px(context, 15));
        testMvvm.setSize(DimensUtils.dp2px(context, 15));
    }

    /**
     * 绑定监听-参数不受影响
     */
    public void clickChangeSizeBtn() {
        j++;
        testM2.setName("testM2");
        testMvvm.setName("testMvvm");
        testM2.setSize(DimensUtils.dp2px(context, 20 + j));
        testMvvm.setSize(DimensUtils.dp2px(context, 40 - j));
    }

    public String imageUrl() {
        return "http://img2.imgtn.bdimg.com/it/u=3137891603,2800618441&fm=26&gp=0.jpg";
    }

    public boolean isThrottleFirst() {
        return true;
    }

    public void changeAccount() {
        if (accounts == null) {
            accounts = new Accounts();
            accounts.setAge(111);
            accounts.setName("account1");
            accountsDao.add(accounts);
        } else {
            int age = accounts.getAge() + 1;
            accounts.setAge(age);
            accounts.setName("account_" + age);
            accountsDao.update(accounts);
        }
    }
}
