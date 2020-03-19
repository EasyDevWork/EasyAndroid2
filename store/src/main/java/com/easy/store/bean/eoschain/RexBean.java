package com.easy.store.bean.eoschain;

import java.util.List;

public class RexBean {

    /**
     * rows : [{"version":0,"owner":"johntrump123","vote_stake":"0.7927 EOS","rex_balance":"7896.1021 REX","matured_rex":75352176,"rex_maturities":[{"first":"2019-12-02T00:00:00","second":3598845},{"first":"2106-02-07T06:28:15","second":10000}]}]
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
         * vote_stake : 0.7927 EOS
         * rex_balance : 7896.1021 REX
         * matured_rex : 75352176
         * rex_maturities : [{"first":"2019-12-02T00:00:00","second":3598845},{"first":"2106-02-07T06:28:15","second":10000}]
         */

        private int version;
        private String owner;
        private String vote_stake;
        private String rex_balance;
        private int matured_rex;
        private List<RexMaturitiesBean> rex_maturities;

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

        public String getVote_stake() {
            return vote_stake;
        }

        public void setVote_stake(String vote_stake) {
            this.vote_stake = vote_stake;
        }

        public String getRex_balance() {
            return rex_balance;
        }

        public void setRex_balance(String rex_balance) {
            this.rex_balance = rex_balance;
        }

        public int getMatured_rex() {
            return matured_rex;
        }

        public void setMatured_rex(int matured_rex) {
            this.matured_rex = matured_rex;
        }

        public List<RexMaturitiesBean> getRex_maturities() {
            return rex_maturities;
        }

        public void setRex_maturities(List<RexMaturitiesBean> rex_maturities) {
            this.rex_maturities = rex_maturities;
        }

        public static class RexMaturitiesBean {
            /**
             * first : 2019-12-02T00:00:00
             * second : 3598845
             */

            private String first;
            private int second;

            public String getFirst() {
                return first;
            }

            public void setFirst(String first) {
                this.first = first;
            }

            public int getSecond() {
                return second;
            }

            public void setSecond(int second) {
                this.second = second;
            }
        }
    }
}
