package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;

import java.util.ArrayList;
import java.util.List;

/**
 * 赎回资源去买Rex
 */
public class UnstakeToRexArg extends BaseArg {
    private String receiver;
    private String from_net;//eg. "0.0001 EOS"
    private String from_cpu;//eg. "0.0001 EOS"

    public UnstakeToRexArg(String actor, String privateKey, String permission, String receiver, String from_net, String from_cpu) {
        super(actor, privateKey, permission);
        this.receiver = receiver;
        this.from_net = from_net;
        this.from_cpu = from_cpu;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getFrom_net() {
        return from_net;
    }

    public void setFrom_net(String from_net) {
        this.from_net = from_net;
    }

    public String getFrom_cpu() {
        return from_cpu;
    }

    public void setFrom_cpu(String from_cpu) {
        this.from_cpu = from_cpu;
    }

    @Override
    public List<ActionAbi> getAbis() {
        List<ActionAbi> actionAbis = new ArrayList<>();
        String unstaketorex = new Writer().unStakeToRex(actor, receiver, from_cpu, from_net).toHex();
        actionAbis.add(new ActionAbi("eosio", "unstaketorex", getTransactionAuthorizationAbi(), unstaketorex));
        return actionAbis;
    }
}
