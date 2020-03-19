package com.easy.demo.ui.btcchain;

import com.easy.btcchain.manager.BtcChainManager;
import com.easy.common.base.CommonPresenter;

import javax.inject.Inject;

public class BtcChainPresenter extends CommonPresenter<BtcChainView> {
    @Inject
    public BtcChainPresenter() {

    }

    public void createWallet() {
        BtcChainManager.getInstance().createWallet();

    }
}