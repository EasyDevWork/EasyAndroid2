package com.easy.store.bean.eoschain;

import java.util.List;

public class VoterInfo {
    /**
     * owner : johntrump121
     * proxy :
     * producers : []
     * staked : 200
     * last_stake : 0
     * last_vote_weight : 0.00000000000000000
     * proxied_vote_weight : 0.00000000000000000
     * is_proxy : 0
     * flags1 : 0
     * reserved2 : 0
     * reserved3 : 0
     */

    private String owner;
    private String proxy;
    private int staked;
    private int last_stake;
    private String last_vote_weight;
    private String proxied_vote_weight;
    private int is_proxy;
    private int flags1;
    private int reserved2;
    private String reserved3;
    private List<String> producers;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public int getStaked() {
        return staked;
    }

    public void setStaked(int staked) {
        this.staked = staked;
    }

    public int getLast_stake() {
        return last_stake;
    }

    public void setLast_stake(int last_stake) {
        this.last_stake = last_stake;
    }

    public String getLast_vote_weight() {
        return last_vote_weight;
    }

    public void setLast_vote_weight(String last_vote_weight) {
        this.last_vote_weight = last_vote_weight;
    }

    public String getProxied_vote_weight() {
        return proxied_vote_weight;
    }

    public void setProxied_vote_weight(String proxied_vote_weight) {
        this.proxied_vote_weight = proxied_vote_weight;
    }

    public int getIs_proxy() {
        return is_proxy;
    }

    public void setIs_proxy(int is_proxy) {
        this.is_proxy = is_proxy;
    }

    public int getFlags1() {
        return flags1;
    }

    public void setFlags1(int flags1) {
        this.flags1 = flags1;
    }

    public int getReserved2() {
        return reserved2;
    }

    public void setReserved2(int reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public List<String> getProducers() {
        return producers;
    }

    public void setProducers(List<String> producers) {
        this.producers = producers;
    }
}
