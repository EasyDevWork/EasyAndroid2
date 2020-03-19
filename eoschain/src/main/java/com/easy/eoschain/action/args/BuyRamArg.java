package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;

import java.util.ArrayList;
import java.util.List;

public class BuyRamArg extends BaseArg{
    private String receiver;
    private String quant;

    public BuyRamArg(String actor, String privateKey, String permission, String receiver, String quant) {
        super(actor, privateKey, permission);
        this.receiver = receiver;
        this.quant = quant;
    }

    @Override
    public List<ActionAbi> getAbis() {
        List<ActionAbi> actionAbis = new ArrayList<>();
        String unstaketorex = new Writer().buyRam(actor, receiver, quant).toHex();
        actionAbis.add(new ActionAbi("eosio", "buyram", getTransactionAuthorizationAbi(), unstaketorex));
        return actionAbis;
    }
}
