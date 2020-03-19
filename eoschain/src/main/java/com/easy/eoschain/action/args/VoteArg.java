package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;

import java.util.ArrayList;
import java.util.List;

public class VoteArg extends BaseArg {
    private String voter;
    private String proxy;
    private List<String> producers;

    public VoteArg(String actor, String privateKey, String permission, String voter, String proxy, List<String> producers) {
        super(actor, privateKey, permission);
        this.voter = voter;
        this.producers = producers;
        this.proxy = proxy;
    }

    public String getVoter() {
        return voter;
    }

    public void setVoter(String voter) {
        this.voter = voter;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public List<String> getProducers() {
        return producers;
    }

    public void setProducers(List<String> producers) {
        this.producers = producers;
    }

    @Override
    public List<ActionAbi> getAbis() {
        List<ActionAbi> actionAbis = new ArrayList<>();
        String vote = new Writer().vote(voter,proxy,producers).toHex();
        actionAbis.add(new ActionAbi("eosio", "voteproducer", getTransactionAuthorizationAbi(), vote));
        return actionAbis;
    }
}
