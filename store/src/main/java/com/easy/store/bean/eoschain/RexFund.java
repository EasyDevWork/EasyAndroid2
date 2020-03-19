package com.easy.store.bean.eoschain;

import java.util.List;

public class RexFund {
    /**
     * rows : [{"version":0,"owner":"johntrump123","balance":"0.1287 EOS"}]
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
         * version : 0
         * owner : johntrump123
         * balance : 0.1287 EOS
         */

        private int version;
        private String owner;
        private String balance;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }
    }
}
