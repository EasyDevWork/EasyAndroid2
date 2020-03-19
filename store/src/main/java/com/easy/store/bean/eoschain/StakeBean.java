package com.easy.store.bean.eoschain;

import java.util.List;

/**
 * 抵押数据--包括为他人抵押，包括给自己抵押
 */
public class StakeBean {

    /**
     * rows : [{"from":"johntrump123","to":"125252555555","net_weight":"0.0000 EOS","cpu_weight":"0.5000 EOS"},{"from":"johntrump123","to":"johntrump123","net_weight":"0.1250 EOS","cpu_weight":"0.0954 EOS"}]
     * more : false
     */

    private boolean more;
    private List<RowsBean> rows;

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * from : johntrump123
         * to : 125252555555
         * net_weight : 0.0000 EOS
         * cpu_weight : 0.5000 EOS
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
}
