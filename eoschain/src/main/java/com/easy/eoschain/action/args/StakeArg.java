package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;

import java.util.ArrayList;
import java.util.List;

/**
 * 抵押CPU/NET的参数
 * 注意：--如果只抵押CPU，NET要赋值0.0000 EOS,反之亦然
 */
public class StakeArg extends BaseArg {
    private String from;
    private String receiver;
    private String netQuantity;//eg. "0.0001 EOS"
    private String cpuQuantity;//eg. "0.0001 EOS"
    private int transfer;//1:转账过去，0：只抵押不转账

    public StakeArg(String actor, String privateKey, String permission, String from, String receiver, String netQuantity, String cpuQuantity, int transfer) {
        super(actor, privateKey, permission);
        this.from = from;
        this.receiver = receiver;
        this.netQuantity = netQuantity;
        this.cpuQuantity = cpuQuantity;
        this.transfer = transfer;
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

    public int getTransfer() {
        return transfer;
    }

    public void setTransfer(int transfer) {
        this.transfer = transfer;
    }

    @Override
    public List<ActionAbi> getAbis() {
        List<ActionAbi> actionAbis = new ArrayList<>();
        String delegatebw = new Writer().delegatebw(actor, receiver, netQuantity, cpuQuantity, transfer).toHex();
        actionAbis.add(new ActionAbi("eosio", "delegatebw", getTransactionAuthorizationAbi(), delegatebw));
        return actionAbis;
    }
}
