package com.easy.store.bean.eoschain;

public class TotalResources {
    /**
     * owner : johntrump121
     * net_weight : 0.0100 TLOS
     * cpu_weight : 0.0100 TLOS
     * ram_bytes : 4094
     */

    private String owner;
    private String net_weight;
    private String cpu_weight;
    private int ram_bytes;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public int getRam_bytes() {
        return ram_bytes;
    }

    public void setRam_bytes(int ram_bytes) {
        this.ram_bytes = ram_bytes;
    }
}
