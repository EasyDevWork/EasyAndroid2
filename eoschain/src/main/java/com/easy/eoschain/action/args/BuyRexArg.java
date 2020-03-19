package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;

import java.util.ArrayList;
import java.util.List;

/**
 * 买Rex需要的参数
 */
public class BuyRexArg extends BaseArg {

    private String quant;//eg. "0.0001 EOS"

    public BuyRexArg(String actor, String privateKey, String permission, String quant) {
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
        String rexData = new Writer().rexOperate(actor, quant).toHex();
        actionAbis.add(new ActionAbi("eosio", "buyrex", getTransactionAuthorizationAbi(), rexData));
        String deposit = new Writer().deposit(actor, quant).toHex();
        actionAbis.add(new ActionAbi("eosio", "deposit", getTransactionAuthorizationAbi(), deposit));
        return actionAbis;
    }
}
