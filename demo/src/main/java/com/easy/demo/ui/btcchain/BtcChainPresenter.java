package com.easy.demo.ui.btcchain;

import com.easy.btcchain.manager.BtcChainManager;
import com.easy.framework.base.BasePresenter;

import javax.inject.Inject;

public class BtcChainPresenter extends BasePresenter<BtcChainView> {
    @Inject
    public BtcChainPresenter() {

    }

    public void createWallet() {
        BtcChainManager.getInstance().createWallet();

    }
}