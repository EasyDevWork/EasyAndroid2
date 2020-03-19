package com.easy.store.bean.eoschain;

/**
 * EOS兑换USDT 价格
 */
public class Eos2UsdtPrice {

    /**
     * price : 2.6961
     * volume_24h : 2.3239286411214E9
     * market_cap : 2439895392
     * percent_change_1h : -0.14
     * percent_change_24h : 1.6628959276018027
     * percent_change_7d : -18.33
     */

    private double price;
    private double volume_24h;
    private long market_cap;
    private double percent_change_1h;
    private double percent_change_24h;
    private double percent_change_7d;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume_24h() {
        return volume_24h;
    }

    public void setVolume_24h(double volume_24h) {
        this.volume_24h = volume_24h;
    }

    public long getMarket_cap() {
        return market_cap;
    }

    public void setMarket_cap(long market_cap) {
        this.market_cap = market_cap;
    }

    public double getPercent_change_1h() {
        return percent_change_1h;
    }

    public void setPercent_change_1h(double percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public double getPercent_change_24h() {
        return percent_change_24h;
    }

    public void setPercent_change_24h(double percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }

    public double getPercent_change_7d() {
        return percent_change_7d;
    }

    public void setPercent_change_7d(double percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }
}
