package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;
import com.easy.eoschain.utils.EosUtils;
import com.easy.utils.BigDecimalUtils;
import com.easy.utils.EmptyUtils;
import com.easy.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 租赁Rex需要的参数
 */
public class RexRentArg extends BaseArg {
    private String cpu;//eg. "0.0001 EOS"
    private String net; //eg. "0.0001 EOS"

    public RexRentArg(String actor, String privateKey, String permission, String cpu, String net) {
        super(actor, privateKey, permission);
        this.cpu = cpu;
        this.net = net;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    @Override
    public List<ActionAbi> getAbis() {
        List<ActionAbi> actionAbis = new ArrayList<>();
        Double cpuNum = EosUtils.getDoubleNumOfData(cpu);
        Double netNum = EosUtils.getDoubleNumOfData(net);
        if (cpuNum > 0 || netNum > 0) {
            String totalNum = BigDecimalUtils.add(cpuNum.toString(), netNum.toString(), 4);
            String unit = EosUtils.getUnit(cpu);
            String deposit = new Writer().deposit(actor, StringUtils.buildString(totalNum, " ", unit)).toHex();
            actionAbis.add(new ActionAbi("eosio", "deposit", getTransactionAuthorizationAbi(), deposit));

            String unit0 = StringUtils.buildString("0.0000 ", unit);
            if (cpuNum > 0) {
                String rentCpu = new Writer().rent(actor, cpu, unit0, unit0).toHex();
                actionAbis.add(new ActionAbi("eosio", "rentcpu", getTransactionAuthorizationAbi(), rentCpu));
            }

            if (netNum > 0) {
                String rentNet = new Writer().rent(actor, net, unit0, unit0).toHex();
                actionAbis.add(new ActionAbi("eosio", "rentnet", getTransactionAuthorizationAbi(), rentNet));
            }
        }
        return actionAbis;
    }
}
