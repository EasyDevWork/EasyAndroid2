package com.easy.eoschain.action;

import com.easy.eoschain.action.args.AuthArg;
import com.easy.eoschain.encrypt.write.BaseWriter;
import com.easy.eoschain.manager.EosPublicKey;
import com.easy.store.bean.eoschain.KeysBean;
import com.easy.store.bean.eoschain.RequiredAuth;

import java.util.List;

public class Writer extends BaseWriter {

    public BaseWriter deposit(String account, String amount) {
        byteWriter.putAccountName(account);
        byteWriter.putAsset(amount);
        return this;
    }

    public BaseWriter rent(String from, String receiver, String loanPayment, String loanFund) {
        byteWriter.putAccountName(from);
        byteWriter.putAccountName(receiver);
        byteWriter.putAsset(loanPayment);
        byteWriter.putAsset(loanFund);
        return this;
    }

    public BaseWriter unStakeToRex(String owner, String receiver, String fromCpu, String fromNet) {
        byteWriter.putAccountName(owner);
        byteWriter.putAccountName(receiver);
        byteWriter.putAsset(fromCpu);
        byteWriter.putAsset(fromNet);
        return this;
    }

    /**
     * 抵押CPU/net
     * @param from
     * @param receiver
     * @param stakeNetQuantity
     * @param stakeCpuQuantity
     * @param transfer
     * @return
     */
    public BaseWriter delegatebw(String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity, int transfer) {
        byteWriter.putAccountName(from);
        byteWriter.putAccountName(receiver);
        byteWriter.putAsset(stakeNetQuantity);
        byteWriter.putAsset(stakeCpuQuantity);
        byteWriter.putInt(transfer);
        return this;
    }

    /**
     * 赎回CPU/NET
     * @param from
     * @param receiver
     * @param stakeNetQuantity
     * @param stakeCpuQuantity
     * @return
     */
    public BaseWriter undelegatebw(String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity) {
        byteWriter.putAccountName(from);
        byteWriter.putAccountName(receiver);
        byteWriter.putAsset(stakeNetQuantity);
        byteWriter.putAsset(stakeCpuQuantity);
        return this;
    }

    public BaseWriter rexOperate(String account, String quant) {
        byteWriter.putAccountName(account);
        byteWriter.putAsset(quant);
        return this;
    }

    public BaseWriter buyRam(String payer, String receiver, String quant) {
        byteWriter.putAccountName(payer);
        byteWriter.putAccountName(receiver);
        byteWriter.putAsset(quant);
        return this;
    }

    public BaseWriter sellRam(String account, Long bytes) {
        byteWriter.putAccountName(account);
        byteWriter.putLong(bytes);
        return this;
    }

    public BaseWriter openChain(String owner, String symbol) {
        byteWriter.putAccountName(owner);
        byteWriter.putSymbol(symbol);
        byteWriter.putAccountName(owner);
        return this;
    }

    public BaseWriter vote(String voter,String proxy,List<String> producers) {
        byteWriter.putAccountName(voter);
        byteWriter.putAccountName(proxy);
        byteWriter.putAccountNameCollection(producers);
        return this;
    }
    /**
     * 手动赎回
     * @param owner
     * @return
     */
    public BaseWriter refund(String owner) {
        byteWriter.putAccountName(owner);
        return this;
    }

    public BaseWriter updateAuth(AuthArg auth) {
        byteWriter.putAccountName(auth.getAccount());
        byteWriter.putAccountName(auth.getPermission());
        byteWriter.putAccountName(auth.getParent());
        squishAccountRequiredAuthAbi(auth.getAuth());
        return this;
    }

    private void squishAccountRequiredAuthAbi(RequiredAuth requiredAuth) {
        byteWriter.putInt(requiredAuth.getThreshold());
        squishCollectionAccountKeyAbi(requiredAuth.getKeys());
        byteWriter.putStringCollection(requiredAuth.getAccounts());
        byteWriter.putStringCollection(requiredAuth.getWaits());
    }

    private void squishCollectionAccountKeyAbi(List<KeysBean> keysBeans) {
        byteWriter.putVariableUInt((long) keysBeans.size());
        for (KeysBean keysBean : keysBeans) {
            squish(keysBean);
        }
    }

    private void squish(KeysBean keysBean) {
        byteWriter.putPublicKey(new EosPublicKey(keysBean.getKey()));
        byteWriter.putShort(keysBean.getWeight());
    }
}
