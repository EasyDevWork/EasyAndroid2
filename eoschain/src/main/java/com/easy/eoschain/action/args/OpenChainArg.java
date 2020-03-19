package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;

import java.util.ArrayList;
import java.util.List;

public class OpenChainArg extends BaseArg {
    private String symbol;

    public OpenChainArg(String actor, String privateKey, String permission, String symbol) {
        super(actor, privateKey, permission);
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public List<ActionAbi> getAbis() {
        List<ActionAbi> actionAbis = new ArrayList<>();
        String open = new Writer().openChain(actor, symbol).toHex();
        actionAbis.add(new ActionAbi("eosio.token", "open", getTransactionAuthorizationAbi(), open));
        return actionAbis;
    }
}
