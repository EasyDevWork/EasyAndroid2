package com.easy.btcchain.manager;


import com.easy.btcchain.bean.BtcAccount;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import static org.bitcoinj.core.Utils.HEX;
import java.math.BigInteger;

public class BtcChainManager {

    public static final class Holder {
        public static final BtcChainManager INSTANCE = new BtcChainManager();
    }

    private BtcChainManager() {

    }

    public static BtcChainManager getInstance() {
        return BtcChainManager.Holder.INSTANCE;
    }


    public BtcAccount createWallet() {
        BtcAccount btcAccount = new BtcAccount();
        NetworkParameters parameters = MainNetParams.get();
        // 取得私鑰WIF格式
        String privateKeyAsHex = new ECKey().getPrivateKeyAsHex();
        BigInteger privateKeyInt = new BigInteger(1, HEX.decode(privateKeyAsHex.toLowerCase()));
        // 未壓縮
        ECKey ecKey = ECKey.fromPrivate(privateKeyInt, false);
        btcAccount.setPrivateKey(ecKey.getPrivateKeyAsWiF(parameters));
        // 公鑰(長度130)
        btcAccount.setPublicKey(ecKey.getPublicKeyAsHex());
        // 產生地址
        byte[] hash = ecKey.getPubKeyHash();
        LegacyAddress legacyAddress = new LegacyAddress(parameters, hash);
        btcAccount.setAddress(legacyAddress.toBase58());
        return btcAccount;
    }

}
