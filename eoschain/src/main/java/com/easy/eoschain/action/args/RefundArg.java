package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;

import java.util.ArrayList;
import java.util.List;

public class RefundArg extends  BaseArg{

    public RefundArg(String actor, String privateKey, String permission) {
        super(actor, privateKey, permission);
    }

    @Override
    public List<ActionAbi> getAbis() {
        List<ActionAbi> actionAbis = new ArrayList<>();
        String refund = new Writer().refund(actor).toHex();
        actionAbis.add(new ActionAbi("eosio", "refund", getTransactionAuthorizationAbi(), refund));
        return actionAbis;
    }
}
