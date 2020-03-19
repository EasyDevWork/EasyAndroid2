package com.easy.eoschain.manager;


import com.alibaba.fastjson.JSONObject;
import com.easy.eoschain.utils.EosUtils;
import com.easy.framework.utils.Utils;
import com.easy.store.bean.EosAccount;
import com.easy.store.bean.eoschain.CpuLimit;
import com.easy.store.bean.eoschain.KeysBean;
import com.easy.store.bean.eoschain.NetLimit;
import com.easy.store.bean.eoschain.Permissions;
import com.easy.store.bean.eoschain.RamPrice;
import com.easy.store.bean.eoschain.RefundRequest;
import com.easy.store.bean.eoschain.RequiredAuth;
import com.easy.store.bean.eoschain.RexBean;
import com.easy.store.bean.eoschain.RexFund;
import com.easy.store.bean.eoschain.RexPrice;
import com.easy.store.bean.eoschain.SelfDelegatedBandwidthBean;
import com.easy.store.bean.eoschain.StakeBean;
import com.easy.store.bean.eoschain.Token;
import com.easy.store.bean.eoschain.TokenPrice;
import com.easy.store.bean.eoschain.TokenShow;
import com.easy.store.bean.eoschain.TotalResources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EosParseManager {
    /**
     * 私钥转公钥
     *
     * @param privateKey
     * @return
     */
    public static String privateKeyToPublicKey(String privateKey) {
        if (Utils.isNotEmpty(privateKey)) {
            EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKey);
            return eosPrivateKey.getPublicKey().toString();
        }
        return null;
    }

    /**
     * 获取账号拥有的权限
     *
     * @param eosAccount
     * @return
     */
    public static List<Permissions> getOwnPermissions(EosAccount eosAccount) {
        List<Permissions> have = new ArrayList<>();
        if (eosAccount != null) {
            String publicKey = eosAccount.getPublicKey();
            if (Utils.isNotEmpty(publicKey)) {
                List<Permissions> permissions = eosAccount.getPermissions();
                if (Utils.isNotEmpty(permissions)) {
                    for (Permissions permission : permissions) {
                        RequiredAuth requiredAuth = permission.getRequired_auth();
                        if (requiredAuth != null) {
                            List<KeysBean> keysList = requiredAuth.getKeys();
                            if (keysList != null && keysList.size() > 0) {
                                for (KeysBean keysEos : keysList) {
                                    if (publicKey.equals(keysEos.getKey())) {
                                        have.add(permission);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return have;
    }

    /**
     * 获取拥有的权限名称
     *
     * @param eosAccount
     * @return
     */
    public static List<String> getPermission(EosAccount eosAccount) {
        List<String> temp = new ArrayList<>();
        if (eosAccount != null && Utils.isNotEmpty(eosAccount.getOwnPermissions())) {
            List<Permissions> permissions = eosAccount.getOwnPermissions();
            for (Permissions permission : permissions) {
                temp.add(permission.getPerm_name());
            }
        }
        return temp;
    }

    /**
     * 解析Ram
     *
     * @param eosAccount
     * @return
     */
    public static String[] parseRam(EosAccount eosAccount) {
        String[] ram = new String[]{"0", "0"};
        if (eosAccount != null) {
            if (Utils.isNotEmpty(eosAccount.getRamUsage())) {
                //已用
                ram[0] = String.valueOf(Double.valueOf(eosAccount.getRamUsage()) / 1024);
            }
            if (Utils.isNotEmpty(eosAccount.getRamQuota())) {
                //总量
                ram[1] = String.valueOf(Double.valueOf(eosAccount.getRamQuota()) / 1024);
            }
        }
        return ram;
    }

    /**
     * 解析Ram价格
     *
     * @param eosAccount
     * @return
     */
    public static String parseRamPrice(EosAccount eosAccount) {
        String price = "0";
        if (eosAccount != null && eosAccount.getRamPrice() != null) {
            RamPrice ramPrice = eosAccount.getRamPrice();
            List<RamPrice.RowsBean> rowsBeans = ramPrice.getRows();
            if (Utils.isNotEmpty(rowsBeans)) {
                RamPrice.RowsBean rowsBean = rowsBeans.get(0);
                if (Utils.isNotEmpty(rowsBean.getQuote())
                        && Utils.isNotEmpty(rowsBean.getBase())
                        && Utils.isNotEmpty(rowsBean.getQuote().getBalance())
                        && Utils.isNotEmpty(rowsBean.getQuote().getBalance())
                ) {
                    String quoteBalance = EosUtils.getNumOfData(rowsBean.getQuote().getBalance());
                    String baseBalance = EosUtils.getNumOfData(rowsBean.getBase().getBalance());
                    double baseBalanceDouble = Utils.formatDouble(baseBalance);
                    if (baseBalanceDouble != 0) {
                        price = String.valueOf(Utils.formatDouble(quoteBalance) / baseBalanceDouble * 1024);
                    }
                }
            }
        }
        return price;
    }

    /**
     * 赎回中的CPU
     *
     * @return
     */
    public static String refundCpu(EosAccount eosAccount) {
        if (eosAccount != null && eosAccount.getRefund_request() != null) {
            RefundRequest refundRequest = eosAccount.getRefund_request();
            if (Utils.isNotEmpty(refundRequest.getCpu_amount())) {
                return EosUtils.getNumOfData(refundRequest.getCpu_amount());
            }
        }
        return "0";
    }

    /**
     * 为他人抵押CPU--先获取抵押列表数据 EosChainManager.getInstance().requestStakeData
     *
     * @param eosAccount
     * @return
     */
    public static String mortgageCpuForOther(EosAccount eosAccount) {
        if (eosAccount != null && eosAccount.getStakeBean() != null) {
            StakeBean stakeBean = eosAccount.getStakeBean();
            List<StakeBean.RowsBean> stakeRows = stakeBean.getRows();
            if (Utils.isNotEmpty(stakeRows)) {
                Double netTotal = 0d;
                for (StakeBean.RowsBean stakeRow : stakeRows) {
                    if (!stakeRow.getTo().equals(eosAccount.getName())) {
                        netTotal += EosUtils.getDoubleNumOfData(stakeRow.getCpu_weight());
                    }
                }
                return String.valueOf(netTotal);
            }
        }
        return "0";
    }

    /**
     * 他人为自己抵押CPU
     *
     * @param eosAccount
     * @return
     */
    public static String mortgageCpuForSelf(EosAccount eosAccount) {
        Double totalCpu = Double.valueOf(totalCpu(eosAccount));
        Double selfCpu = Double.valueOf(selfCpu(eosAccount));
        return String.valueOf(totalCpu - selfCpu);
    }

    /**
     * 自己抵押给自己的CPU
     *
     * @return
     */
    public static String selfCpu(EosAccount eosAccount) {
        if (eosAccount != null && eosAccount.getSelf_delegated_bandwidth() != null) {
            SelfDelegatedBandwidthBean selfDelegatedBandwidth = eosAccount.getSelf_delegated_bandwidth();
            if (Utils.isNotEmpty(selfDelegatedBandwidth.getCpu_weight())) {
                return EosUtils.getNumOfData(selfDelegatedBandwidth.getCpu_weight());
            }
        }
        return "0";
    }

    /**
     * CPU总量
     *
     * @param eosAccount
     * @return
     */
    public static String totalCpu(EosAccount eosAccount) {
        if (eosAccount != null && eosAccount.getTotal_resources() != null) {
            TotalResources totalResources = eosAccount.getTotal_resources();
            if (Utils.isNotEmpty(totalResources.getCpu_weight())) {
                return EosUtils.getNumOfData(totalResources.getCpu_weight());
            }
        }
        return "0";
    }

    /**
     * cpu使用情况
     *
     * @param eosAccount
     * @return
     */
    public static String[] cpuUseInfo(EosAccount eosAccount) {
        String[] results = new String[]{"0", "0", "0"};
        if (eosAccount != null && eosAccount.getCpuLimit() != null) {
            CpuLimit cpuLimit = eosAccount.getCpuLimit();
            results[0] = Utils.div(cpuLimit.getUsed(), "1000", 4);//毫秒 已用CPU
            results[1] = Utils.div(cpuLimit.getMax(), "1000", 4);//毫秒 总量CPU
            results[2] = Utils.div(results[0], results[1], 4);//毫秒 百分比
        }
        return results;
    }

    /**
     * 赎回NET
     *
     * @return
     */
    public static String refundNet(EosAccount eosAccount) {
        if (eosAccount != null && eosAccount.getRefund_request() != null) {
            RefundRequest refundRequest = eosAccount.getRefund_request();
            if (Utils.isNotEmpty(refundRequest.getNet_amount())) {
                return EosUtils.getNumOfData(refundRequest.getNet_amount());
            }
        }
        return "0";
    }

    /**
     * 自己抵押给自己的Net
     *
     * @return
     */
    public static String selfNet(EosAccount eosAccount) {
        if (eosAccount != null && eosAccount.getSelf_delegated_bandwidth() != null) {
            SelfDelegatedBandwidthBean selfDelegatedBandwidth = eosAccount.getSelf_delegated_bandwidth();
            if (Utils.isNotEmpty(selfDelegatedBandwidth.getNet_weight())) {
                return EosUtils.getNumOfData(selfDelegatedBandwidth.getNet_weight());
            }
        }
        return "0";
    }

    /**
     * 他人为自己抵押Net
     *
     * @param eosAccount
     * @return
     */
    public static String mortgageNetForSelf(EosAccount eosAccount) {
        Double totalNet = Double.valueOf(totalNet(eosAccount));
        Double selfNet = Double.valueOf(selfNet(eosAccount));
        return String.valueOf(totalNet - selfNet);
    }

    /**
     * 为他人抵押Net---先获取抵押列表数据 EosChainManager.getInstance().requestStakeData
     *
     * @param eosAccount
     * @return
     */
    public static String mortgageNetForOther(EosAccount eosAccount) {
        if (eosAccount != null && eosAccount.getStakeBean() != null) {
            StakeBean stakeBean = eosAccount.getStakeBean();
            List<StakeBean.RowsBean> stakeRows = stakeBean.getRows();
            if (Utils.isNotEmpty(stakeRows)) {
                Double netTotal = 0d;
                for (StakeBean.RowsBean stakeRow : stakeRows) {
                    if (!stakeRow.getTo().equals(eosAccount.getName())) {
                        netTotal += EosUtils.getDoubleNumOfData(stakeRow.getNet_weight());
                    }
                }
                return String.valueOf(netTotal);
            }
        }
        return "0";
    }

    /**
     * NET总量
     *
     * @param eosAccount
     * @return
     */
    public static String totalNet(EosAccount eosAccount) {
        if (eosAccount != null && eosAccount.getTotal_resources() != null) {
            TotalResources totalResources = eosAccount.getTotal_resources();
            if (Utils.isNotEmpty(totalResources.getNet_weight())) {
                return EosUtils.getNumOfData(totalResources.getNet_weight());
            }
        }
        return "0";
    }

    /**
     * 获取token信息 --
     * 需求请求token列表
     * 需求请求token价格列表
     * 需求请求美元价格
     *
     * @param eosAccount
     * @return
     */
    public static Map<String, TokenShow> queryTokens(EosAccount eosAccount) {
        Map<String, TokenShow> map = new HashMap<>();
        if (eosAccount != null && eosAccount.getTokenList() != null) {
            for (Token token : eosAccount.getTokenList()) {
                TokenShow tokenShow = new TokenShow();
                tokenShow.setSymbol(token.getSymbol());
                tokenShow.setContract(token.getContract());
                tokenShow.setPrecision(token.getPrecision());
                tokenShow.setBalance(token.getBalance());
                map.put(token.getSymbol() + "_" + token.getContract(), tokenShow);
            }
            if (Utils.isNotEmpty(eosAccount.getTokenPriceList())) {
                for (TokenPrice tokenPrice : eosAccount.getTokenPriceList()) {
                    TokenShow tokenShow = map.get(tokenPrice.getCurrency() + "_" + tokenPrice.getContract());
                    if (tokenShow != null) {
                        tokenShow.setChange(tokenPrice.getChange());
                        tokenShow.setPrice(tokenPrice.getLast());
                        tokenShow.setTotalPrice(Utils.mul(tokenShow.getBalance(), tokenShow.getPrice(), 8));
                    }
                }
            }
        }
        return map;
    }

    /**
     * net使用情况
     *
     * @param eosAccount
     * @return
     */
    public static String[] netUseInfo(EosAccount eosAccount) {
        String[] results = new String[]{"0", "0", "0"};
        if (eosAccount != null && eosAccount.getNetLimit() != null) {
            NetLimit netLimit = eosAccount.getNetLimit();
            results[0] = Utils.div(netLimit.getUsed(), "1024", 4);//毫秒 已用CPU
            results[1] = Utils.div(netLimit.getMax(), "1024", 4);//毫秒 总量CPU
            results[2] = Utils.div(results[0], results[1], 4);//毫秒 百分比
        }
        return results;
    }

    /**
     * Rex总数量--必须请求REX数量接口 EosChainManager.getInstance().requestRexData
     *
     * @param eosAccount
     * @return
     */
    public static String totalRexNum(EosAccount eosAccount) {
        if (eosAccount != null && eosAccount.getRexBean() != null) {
            RexBean rexBean = eosAccount.getRexBean();
            List<RexBean.RowsBean> rows = rexBean.getRows();
            if (Utils.isNotEmpty(rows)) {
                RexBean.RowsBean rowsBean = rows.get(0);
                return EosUtils.getNumOfData(rowsBean.getRex_balance());
            }
        }
        return "0";
    }

    /**
     * rex总数量：rex数量*rex单价+rex_fund
     * 必须请求:
     * REX数量接口 EosChainManager.getInstance().requestRexData
     * REX价格接口 EosChainManager.getInstance().requestRexPrice
     * REX_FUND接口 EosChainManager.getInstance().requestRexFundData
     *
     * @param eosAccount
     * @return
     */
    public static String rexTotal(EosAccount eosAccount) {
        if (eosAccount != null
                && eosAccount.getRexFund() != null
                && eosAccount.getRexPrice() != null
                && eosAccount.getRexBean() != null) {
            String rexFund = rexFund(eosAccount);
            String rexPrice = rexPrice(eosAccount)[0];
            String rexNum = totalRexNum(eosAccount);
            return Utils.add(rexFund, Utils.mul(rexNum, rexPrice, 8), 8);
        }
        return "0";
    }

    /**
     * rex fund价值--REX_FUND接口 EosChainManager.getInstance().requestRexFundData
     *
     * @param eosAccount
     * @return
     */
    public static String rexFund(EosAccount eosAccount) {
        if (eosAccount != null && eosAccount.getRexFund() != null) {
            RexFund rexFund = eosAccount.getRexFund();
            List<RexFund.RowsBean> rowsBeans = rexFund.getRows();
            if (Utils.isNotEmpty(rowsBeans)) {
                RexFund.RowsBean rowsBean = rowsBeans.get(0);
                return EosUtils.getNumOfData(rowsBean.getBalance());
            }
        }
        return "0";
    }

    /**
     * rex 价格--REX价格接口 EosChainManager.getInstance().requestRexPrice
     *
     * @param eosAccount
     * @return
     */
    public static String[] rexPrice(EosAccount eosAccount) {
        String[] price = new String[]{"0", "0", "0", "0"};
        if (eosAccount != null && eosAccount.getRexPrice() != null) {
            RexPrice rexPrice = eosAccount.getRexPrice();
            List<RexPrice.RowsBean> rowsBeans = rexPrice.getRows();
            if (Utils.isNotEmpty(rowsBeans)) {
                RexPrice.RowsBean rowsBean = rowsBeans.get(0);
                JSONObject rexPriceJson = parseRexPrice(rowsBean);
                String totalUnlent = rexPriceJson.getString("total_unlent");
                String rental = rexPriceJson.getString("rental");
                price[0] = rexPriceJson.getString("price");//购买REX单价：EOS/REX
                price[1] = rental;//租赁单价 rex/eos
                if (Utils.isNotEmpty(rental)) {
                    price[2] = Utils.div("1.0", rental, 8);//租赁单价 eos/rex
                    String coreLiquidBalance = Utils.mul(eosAccount.getCoreLiquidBalance(), rental, 8);
                    if (Utils.compare(coreLiquidBalance, totalUnlent)) {
                        price[3] = totalUnlent;
                    } else {
                        price[3] = coreLiquidBalance;
                    }
                }
            }
        }
        return price;
    }

    public static JSONObject parseRexPrice(RexPrice.RowsBean rowsBean) {
        String totolRex = EosUtils.getNumOfData(rowsBean.getTotal_rex());
        String totalLendable = EosUtils.getNumOfData(rowsBean.getTotal_lendable());
        String totalUnlent = EosUtils.getNumOfData(rowsBean.getTotal_unlent());
        String totalRent = EosUtils.getNumOfData(rowsBean.getTotal_rent());
        String price = Utils.div(totalLendable, totolRex, 8);
        String rental = Utils.div(totalUnlent, Utils.add("1", totalRent, 8), 8);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("price", price);
        jsonObject.put("rental", rental);
        jsonObject.put("total_unlent", totalUnlent);
        jsonObject.put("total_lendable", totalLendable);

        return jsonObject;
    }
}
