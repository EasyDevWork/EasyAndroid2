package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;

import java.util.ArrayList;
import java.util.List;

/**
 * 卖Ram
 */
public class SellRamArg extends BaseArg {
    private float quant;//

    public SellRamArg(String actor, String privateKey, String permission, float quant) {
        super(actor, privateKey, permission);
        this.quant = quant;
    }

    @Override
    public List<ActionAbi> getAbis() {
        List<ActionAbi> actionAbis = new ArrayList<>();
        String sellRam = new Writer().sellRam(actor, (long) (quant * 1024)).toHex();
        actionAbis.add(new ActionAbi("eosio", "sellram", getTransactionAuthorizationAbi(), sellRam));
        return actionAbis;
    }
}
