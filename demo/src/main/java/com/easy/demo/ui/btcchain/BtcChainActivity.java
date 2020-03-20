package com.easy.demo.ui.btcchain;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.common.base.CommonActivity;
import com.easy.demo.R;
import com.easy.demo.databinding.TestBtcChainBinding;
import com.easy.net.rxlifecycle.ActivityEvent;

@ActivityInject
@Route(path = "/demo/BtcChainActivity", name = "Btc链")
public class BtcChainActivity extends CommonActivity<BtcChainPresenter, TestBtcChainBinding> implements BtcChainView<ActivityEvent> {

    @Override
    public int getLayoutId() {
        return R.layout.test_btc_chain;
    }

    @Override
    public void initView() {

    }

    public void createPrivateAndPublicKey(View view){

    }

    public void createWallet(View view){
        presenter.createWallet();
    }

}
