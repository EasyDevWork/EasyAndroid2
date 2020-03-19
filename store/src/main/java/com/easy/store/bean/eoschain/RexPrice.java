package com.easy.store.bean.eoschain;

import java.util.List;

public class RexPrice {

    /**
     * rows : [{"version":0,"total_lent":"57660694.4558 EOS","total_unlent":"20517371.8531 EOS","total_rent":"22291.2343 EOS","total_lendable":"78178066.3089 EOS","total_rex":"778707707252.0180 REX","namebid_proceeds":"0.0000 EOS","loan_num":232103}]
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
         * total_lent : 57660694.4558 EOS
         * total_unlent : 20517371.8531 EOS
         * total_rent : 22291.2343 EOS
         * total_lendable : 78178066.3089 EOS
         * total_rex : 778707707252.0180 REX
         * namebid_proceeds : 0.0000 EOS
         * loan_num : 232103
         */

        private int version;
        private String total_lent;
        private String total_unlent;
        private String total_rent;
        private String total_lendable;
        private String total_rex;
        private String namebid_proceeds;
        private int loan_num;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getTotal_lent() {
            return total_lent;
        }

        public void setTotal_lent(String total_lent) {
            this.total_lent = total_lent;
        }

        public String getTotal_unlent() {
            return total_unlent;
        }

        public void setTotal_unlent(String total_unlent) {
            this.total_unlent = total_unlent;
        }

        public String getTotal_rent() {
            return total_rent;
        }

        public void setTotal_rent(String total_rent) {
            this.total_rent = total_rent;
        }

        public String getTotal_lendable() {
            return total_lendable;
        }

        public void setTotal_lendable(String total_lendable) {
            this.total_lendable = total_lendable;
        }

        public String getTotal_rex() {
            return total_rex;
        }

        public void setTotal_rex(String total_rex) {
            this.total_rex = total_rex;
        }

        public String getNamebid_proceeds() {
            return namebid_proceeds;
        }

        public void setNamebid_proceeds(String namebid_proceeds) {
            this.namebid_proceeds = namebid_proceeds;
        }

        public int getLoan_num() {
            return loan_num;
        }

        public void setLoan_num(int loan_num) {
            this.loan_num = loan_num;
        }
    }
}
