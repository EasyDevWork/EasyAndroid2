package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;

import java.util.ArrayList;
import java.util.List;

/**
 * 赎回CPU/NET的参数
 * 注意：--如果只抵押CPU，NET要赋值0.0000 EOS,反之亦然
 */
public class UnStakeArg extends BaseArg {
    private String from;
    private String receiver;
    private String netQuantity;//eg. "0.0001 EOS"
    private String cpuQuantity;//eg. "0.0001 EOS"

    public UnStakeArg(String actor, String privateKey, String permission, String from, String receiver, String netQuantity, String cpuQuantity) {
        super(actor, privateKey, permission);
        this.from = from;
        this.receiver = receiver;
        this.netQuantity = netQuantity;
        this.cpuQuantity = cpuQuantity;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getNetQuantity() {
        return netQuantity;
    }

    public void setNetQuantity(String netQuantity) {
        this.netQuantity = netQuantity;
    }

    public String getCpuQuantity() {
        return cpuQuantity;
    }

    public void setCpuQuantity(String cpuQuantity) {
        this.cpuQuantity = cpuQuantity;
    }

    @Override
    public List<ActionAbi> getAbis() {
        List<ActionAbi> actionAbis = new ArrayList<>();
        String delegatebw = new Writer().undelegatebw(actor, receiver, netQuantity, cpuQuantity).toHex();
        actionAbis.add(new ActionAbi("eosio", "undelegatebw", getTransactionAuthorizationAbi(), delegatebw));
        return actionAbis;
    }
}
