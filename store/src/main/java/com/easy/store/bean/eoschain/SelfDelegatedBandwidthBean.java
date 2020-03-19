package com.easy.store.bean.eoschain;

public class SelfDelegatedBandwidthBean {
    /**
     * from : johntrump121
     * to : johntrump121
     * net_weight : 0.0100 TLOS
     * cpu_weight : 0.0100 TLOS
     */

    private String from;
    private String to;
    private String net_weight;
    private String cpu_weight;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getNet_weight() {
        return net_weight;
    }

    public void setNet_weight(String net_weight) {
        this.net_weight = net_weight;
    }

    public String getCpu_weight() {
        return cpu_weight;
    }

    public void setCpu_weight(String cpu_weight) {
        this.cpu_weight = cpu_weight;
    }
}
