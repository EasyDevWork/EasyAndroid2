package com.easy.eoschain.bean;

import java.util.List;

public class ProducerInfo {
    private List<Producer> rows;
    private String total_producer_vote_weight;
    private String more;

    public List<Producer> getRows() {
        return rows;
    }

    public void setRows(List<Producer> rows) {
        this.rows = rows;
    }

    public String getTotal_producer_vote_weight() {
        return total_producer_vote_weight;
    }

    public void setTotal_producer_vote_weight(String total_producer_vote_weight) {
        this.total_producer_vote_weight = total_producer_vote_weight;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }
}
