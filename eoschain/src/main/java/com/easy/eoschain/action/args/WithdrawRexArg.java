package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;

import java.util.ArrayList;
import java.util.List;

/**
 * 赎回Rex
 * 注意：卖Rex后要主动调用这个赎回
 */
public class WithdrawRexArg extends BaseArg {
    private String quant;//eg. "0.0001 EOS"

    public WithdrawRexArg(String actor, String privateKey, String permission, String quant) {
        super(actor, privateKey, permission);
        this.quant = quant;
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    @Override
    public List<ActionAbi> getAbis() {
        List<ActionAbi> actionAbis = new ArrayList<>();
        String sellrex = new Writer().rexOperate(actor, quant).toHex();
        actionAbis.add(new ActionAbi("eosio", "withdraw", getTransactionAuthorizationAbi(), sellrex));
        return actionAbis;
    }
}
