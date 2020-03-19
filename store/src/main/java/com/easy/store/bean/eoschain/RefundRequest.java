package com.easy.store.bean.eoschain;

public class RefundRequest {

    /**
     * owner : johntrump123
     * request_time : 2019-12-04T06:10:35
     * net_amount : 0.0000 EOS
     * cpu_amount : 0.0010 EOS
     */

    private String owner;
    private String request_time;
    private String net_amount;
    private String cpu_amount;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getNet_amount() {
        return net_amount;
    }

    public void setNet_amount(String net_amount) {
        this.net_amount = net_amount;
    }

    public String getCpu_amount() {
        return cpu_amount;
    }

    public void setCpu_amount(String cpu_amount) {
        this.cpu_amount = cpu_amount;
    }
}
