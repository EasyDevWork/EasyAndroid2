package com.easy.eoschain.bean;

public class Producer {
    private String owner;
    private String total_votes;
    private String producer_key;
    private int is_active;
    private String url;
    private int unpaid_blocks;
    private String last_claim_time;
    private int location;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTotal_votes() {
        return total_votes;
    }

    public void setTotal_votes(String total_votes) {
        this.total_votes = total_votes;
    }

    public String getProducer_key() {
        return producer_key;
    }

    public void setProducer_key(String producer_key) {
        this.producer_key = producer_key;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUnpaid_blocks() {
        return unpaid_blocks;
    }

    public void setUnpaid_blocks(int unpaid_blocks) {
        this.unpaid_blocks = unpaid_blocks;
    }

    public String getLast_claim_time() {
        return last_claim_time;
    }

    public void setLast_claim_time(String last_claim_time) {
        this.last_claim_time = last_claim_time;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
