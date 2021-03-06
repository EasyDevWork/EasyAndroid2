package com.easy.demo.ui.eoschain;

import androidx.lifecycle.Lifecycle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easy.eoschain.action.args.AuthArg;
import com.easy.eoschain.action.args.BuyRamArg;
import com.easy.eoschain.action.args.BuyRexArg;
import com.easy.eoschain.action.args.OpenChainArg;
import com.easy.eoschain.action.args.RefundArg;
import com.easy.eoschain.action.args.RexRentArg;
import com.easy.eoschain.action.args.SellRamArg;
import com.easy.eoschain.action.args.SellRexArg;
import com.easy.eoschain.action.args.StakeArg;
import com.easy.eoschain.action.args.UnStakeArg;
import com.easy.eoschain.action.args.UnstakeToRexArg;
import com.easy.eoschain.action.args.UpdateAuthArg;
import com.easy.eoschain.action.args.VoteArg;
import com.easy.eoschain.action.args.WithdrawRexArg;
import com.easy.eoschain.bean.ChainAccount;
import com.easy.eoschain.bean.CurrencyInfo;
import com.easy.eoschain.manager.EosChainManager;
import com.easy.eoschain.manager.EosParseManager;
import com.easy.eoschain.utils.EosUtils;
import com.easy.framework.base.BasePresenter;
import com.easy.net.beans.Response;
import com.easy.store.bean.EosAccount;
import com.easy.store.bean.eoschain.Eos2UsdtPrice;
import com.easy.store.bean.eoschain.RamPrice;
import com.easy.store.bean.eoschain.RexBean;
import com.easy.store.bean.eoschain.RexFund;
import com.easy.store.bean.eoschain.RexPrice;
import com.easy.store.bean.eoschain.StakeBean;
import com.easy.store.bean.eoschain.Token;
import com.easy.store.bean.eoschain.TokenPrice;
import com.easy.store.bean.eoschain.TokenShow;
import com.easy.store.dao.EosAccountDao;
import com.easy.utils.BigDecimalUtils;
import com.easy.utils.EmptyUtils;
import com.easy.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class EosChainPresenter extends BasePresenter<EosChainView> {
    @Inject
    public Lazy<EosAccountDao> eosAccountDao;

    @Inject
    public EosChainPresenter() {

    }

    /**
     * 创建钱包
     *
     * @param accountName
     * @param netType
     */
    public void createWallet(String accountName, String netType) {
        EosAccount eosAccount = eosAccountDao.get().query(accountName, netType);
        if (eosAccount == null) {
            eosAccount = new EosAccount();
            eosAccount.setName(accountName);
            eosAccount.setNetType(netType);
            eosAccountDao.get().insertOrUpdate(eosAccount);
            ToastUtils.showShort("创建成功");
        } else {
            ToastUtils.showShort("已经存在，不用创建");
        }
    }

    /**
     * 删除钱包
     *
     * @param accountName
     * @param netType
     */
    public void deleteWallet(String accountName, String netType) {
        EosAccount eosAccount = eosAccountDao.get().query(accountName, netType);
        if (eosAccount != null) {
            int result = eosAccountDao.get().delete(eosAccount);
            if (result == 1) {
                ToastUtils.showShort("删除成功");
                return;
            }
        }
        ToastUtils.showShort("删除失败");
    }

    /**
     * 设置私钥
     *
     * @param privateKey
     */
    public void setPrivateKey(String privateKey) {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null) {
            return;
        }
        eosAccount.setPrivateKey(privateKey);
        eosAccount.setPublicKey(EosParseManager.privateKeyToPublicKey(privateKey));
        eosAccount.setOwnPermissions(EosParseManager.getOwnPermissions(eosAccount));
        eosAccountDao.get().insertOrUpdate(eosAccount);
        ToastUtils.showShort("设置成功");
    }

    /**
     * 请求链信息
     */
    public void requestChainInfo() {
        EosChainManager.getInstance().requestChainInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.chainInfoCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.chainInfoCallback(response);
                        });
        ;
    }

    /**
     * 查询合约账号信息
     */
    public void requestContract(String accountName) {
        EosChainManager.getInstance().requestContract(accountName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.requestContractCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.requestContractCallback(response);
                        });
    }

    /**
     * 查询投票列表
     */
    public void requestVoteList() {
        EosChainManager.getInstance().requestVoteList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.requestVoteListCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.requestVoteListCallback(response);
                        });
        ;
    }

    public void requestCurrencyListBalance(List<CurrencyInfo> currencyList) {
        EosChainManager.getInstance().requestCurrencyListBalance(currencyList)
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(currencyInfos -> mvpView.currencyListBalanceCallback(currencyInfos),
                        throwable -> mvpView.currencyListBalanceCallback(null));
    }

    /**
     * 获取货币数量
     */
    public void requestCurrencyBalance(CurrencyInfo balance) {
        EosChainManager.getInstance().requestCurrencyBalance(balance)
                .map(response -> {
                    if (EmptyUtils.isNotEmpty(response.getOriData())) {
                        List<String> currencys = JSON.parseArray(response.getOriData(), String.class);
                        response.setResultObj(currencys.get(0));
                    }
                    return response;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.currencyBalanceCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.currencyBalanceCallback(response);
                        });
    }

    /**
     * 请求USDT价格
     */
    public void requestUsdtPrice() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null) {
            return;
        }
        EosChainManager.getInstance().requestUsdtPrice()
                .map(response -> {
                    if (response.getCode() == Response.SUCCESS_STATE) {
                        String oriData = response.getOriData();
                        JSONObject jsonObject = JSON.parseObject(oriData);
                        JSONObject dataJ = jsonObject.getJSONObject("data");
                        JSONObject quotesJ = dataJ.getJSONObject("quotes");
                        Eos2UsdtPrice usdtPrice = JSON.parseObject(quotesJ.getString("USD"), Eos2UsdtPrice.class);
                        eosAccount.setUsdtPrice(usdtPrice);
                        eosAccountDao.get().insertOrUpdate(eosAccount);
                    }
                    return response;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.usdtPriceCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.usdtPriceCallback(response);
                        });
    }

    /**
     * 获取RexFund数据
     */
    public void requestRexFundData() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null) {
            return;
        }
        EosChainManager.getInstance().requestRexFundData(eosAccount.getName())
                .map(response -> {
                    if (response.getCode() == Response.SUCCESS_STATE) {
                        RexFund rexFund = response.getResultObj();
                        eosAccount.setRexFund(rexFund);
                        eosAccountDao.get().insertOrUpdate(eosAccount);
                    }
                    return response;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.rexDataCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.rexDataCallback(response);
                        });
        ;
    }

    /**
     * 获取rex数据
     */
    public void requestRexData() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null) {
            return;
        }
        EosChainManager.getInstance().requestRexData(eosAccount.getName())
                .map(response -> {
                    if (response.getCode() == Response.SUCCESS_STATE) {
                        RexBean stakeBean = response.getResultObj();
                        eosAccount.setRexBean(stakeBean);
                        eosAccountDao.get().insertOrUpdate(eosAccount);
                    }
                    return response;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.rexBeanDataCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.rexBeanDataCallback(response);
                        });
    }

    /**
     * 获取REX价格
     */
    public void requestRexPrice() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null) {
            return;
        }
        EosChainManager.getInstance().requestRexPriceData()
                .map(response -> {
                    if (response.getCode() == Response.SUCCESS_STATE) {
                        RexPrice rexPrice = response.getResultObj();
                        eosAccount.setRexPrice(rexPrice);
                        eosAccountDao.get().insertOrUpdate(eosAccount);
                    }
                    return response;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.rexPriceCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.rexPriceCallback(response);
                        });
    }

    /**
     * 获取Ram价格
     */
    public void requestRamPrice() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null) {
            return;
        }
        EosChainManager.getInstance().requestRamPrice()
                .map(response -> {
                    if (response.getCode() == Response.SUCCESS_STATE) {
                        RamPrice ramPrice = (RamPrice) response.getResultObj();
                        eosAccount.setRamPrice(ramPrice);
                        eosAccountDao.get().insertOrUpdate(eosAccount);
                    }
                    return response;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.ramPriceCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.ramPriceCallback(response);
                        });
    }

    /**
     * 获取抵押数据
     */
    public void requestStakeData() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null) {
            return;
        }
        EosChainManager.getInstance().requestStakeData(eosAccount.getName())
                .map((Function<Response<StakeBean>, Response<StakeBean>>) response -> {
                    if (response.getCode() == Response.SUCCESS_STATE) {
                        StakeBean stakeBean = (StakeBean) response.getResultObj();
                        eosAccount.setStakeBean(stakeBean);
                        eosAccountDao.get().insertOrUpdate(eosAccount);
                    }
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.stakeDataCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.stakeDataCallback(response);
                        });
    }

    public EosAccount queryAccount() {
        EosAccount eosAccount = eosAccountDao.get().query();
        if (eosAccount == null) {
            ToastUtils.showShort("请先创建钱包");
            return null;
        }
        return eosAccount;
    }

    /**
     * 请求账号信息
     */
    public void requestAccountInfo() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null) {
            return;
        }
        EosChainManager.getInstance().requestAccountInfo(eosAccount.getName())
                .map(response -> {
                    if (response.getCode() == Response.SUCCESS_STATE) {
                        ChainAccount chainAccount = response.getResultObj();
                        eosAccount.setName(chainAccount.getAccount_name());
                        eosAccount.setCoreLiquidBalance(EosUtils.getNumOfData(chainAccount.getCore_liquid_balance()));
                        eosAccount.setCpuLimit(chainAccount.getCpu_limit());
                        eosAccount.setNetLimit(chainAccount.getNet_limit());
                        eosAccount.setRamQuota(String.valueOf(chainAccount.getRam_quota()));
                        eosAccount.setRamUsage(String.valueOf(chainAccount.getRam_usage()));
                        eosAccount.setPermissions(chainAccount.getPermissions());
                        eosAccount.setVoterInfo(chainAccount.getVoter_info());
                        eosAccount.setRefund_request(chainAccount.getRefund_request());
                        eosAccount.setTotal_resources(chainAccount.getTotal_resources());
                        eosAccount.setSelf_delegated_bandwidth(chainAccount.getSelf_delegated_bandwidth());
                        eosAccountDao.get().insertOrUpdate(eosAccount);
                    }
                    return response;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.chainAccountInfoCallback(result, null),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.chainAccountInfoCallback(response, null);
                        });
    }

    public void requestCurrencyPrice() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null) {
            return;
        }
        EosChainManager.getInstance().requestTokenPrice()
                .map(response -> {
                    if (response.getCode() == 200) {
                        String oriData = response.getOriData();
                        JSONObject jsonObject = JSON.parseObject(oriData);
                        List<TokenPrice> tokenList = JSON.parseArray(jsonObject.getString("data"), TokenPrice.class);
                        eosAccount.setTokenPriceList(tokenList);
                        eosAccountDao.get().insertOrUpdate(eosAccount);
                    }
                    return response;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.tokenCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.tokenCallback(response);
                        });
    }

    public String[] createPrivateAndPublicKey() {
        return EosChainManager.getInstance().createPrivateAndPublicKey();
    }

    public void requestCurrencyList() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null) {
            return;
        }
        EosChainManager.getInstance().requestTokenData(eosAccount.getName())
                .map(response -> {
                    if (response.getCode() == Response.SUCCESS_STATE) {
                        String oriData = response.getOriData();
                        JSONObject jsonObject = JSON.parseObject(oriData);
                        JSONObject accountJ = jsonObject.getJSONObject("account");
                        List<Token> tokenList = JSON.parseArray(accountJ.getString("tokens"), Token.class);
                        eosAccount.setTokenList(tokenList);
                        eosAccountDao.get().insertOrUpdate(eosAccount);
                    }
                    return response;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(result -> mvpView.tokenCallback(result),
                        throwable -> {
                            Response response = new Response();
                            response.setMsg(throwable.getMessage());
                            mvpView.tokenCallback(response);
                        });
    }

    public void parseAccountInfo() {
        EosAccount account = queryAccount();
        if (account == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        String ramPrice = EosParseManager.parseRamPrice(account);
        builder.append("ram 价格：").append(ramPrice).append(" \n");
        String[] ram = EosParseManager.parseRam(account);
        builder.append("ram 使用：").append(ram[0]).append(" \n");
        builder.append("ram 总量：").append(ram[1]).append(" \n");
        builder.append("eos 可用数量：").append(account.getCoreLiquidBalance()).append(" EOS").append(" \n");
        String[] prices = EosParseManager.rexPrice(account);
        builder.append("rex 租金单价：").append(prices[1]).append(" rex/eos").append(" \n");
        builder.append("rex 租金单价：").append(prices[2]).append(" eos/rex").append(" \n");
        builder.append("rex 可租借数量：").append(prices[3]).append(" rex").append(" \n");
        builder.append("rex 购买单价：").append(prices[0]).append(" eos/rex").append(" \n");
        String totalRexNum = EosParseManager.totalRexNum(account);
        builder.append("rex 拥有的数量：").append(totalRexNum).append(" rex").append(" \n");
        String rexFund = EosParseManager.rexFund(account);
        builder.append("rex fund：").append(rexFund).append(" EOS").append(" \n");
        String rexTotal = EosParseManager.rexTotal(account);
        builder.append("rex 总价值：").append(rexTotal).append(" EOS").append(" \n");
        String[] cpuUseInfo = EosParseManager.cpuUseInfo(account);
        builder.append("cpu 已用：").append(cpuUseInfo[0]).append(" ms").append(" \n");
        builder.append("cpu 总量：").append(cpuUseInfo[1]).append(" ms").append(" \n");
        builder.append("cpu 百分比：").append(BigDecimalUtils.mul(cpuUseInfo[2], "100", 2)).append(" %").append(" \n");
        String refundCpu = EosParseManager.refundCpu(account);
        builder.append("cpu 赎回：").append(refundCpu).append(" EOS").append(" \n");
        String selfCpu = EosParseManager.selfCpu(account);
        builder.append("cpu 自身：").append(selfCpu).append(" EOS").append(" \n");
        String mortgageCpuForOther = EosParseManager.mortgageCpuForOther(account);
        builder.append("cpu 自己抵押给他人：").append(mortgageCpuForOther).append(" EOS").append(" \n");
        String mortgageCpuForSelf = EosParseManager.mortgageCpuForSelf(account);
        builder.append("cpu 他人抵押给自己：").append(mortgageCpuForSelf).append(" EOS").append(" \n");
        String totalCpu = EosParseManager.totalCpu(account);
        builder.append("cpu 总的：").append(totalCpu).append(" EOS").append(" \n");
        String[] netUseInfo = EosParseManager.netUseInfo(account);
        builder.append("net 已用：").append(netUseInfo[0]).append(" KB").append(" \n");
        builder.append("net 总量：").append(netUseInfo[1]).append(" KB").append(" \n");
        builder.append("net 百分比：").append(BigDecimalUtils.mul(netUseInfo[2], "100", 2)).append(" %").append(" \n");
        String refundNet = EosParseManager.refundNet(account);
        builder.append("net 赎回：").append(refundNet).append(" EOS").append(" \n");
        String selfNet = EosParseManager.selfNet(account);
        builder.append("net 自身：").append(selfNet).append(" EOS").append(" \n");
        String mortgageNetForOther = EosParseManager.mortgageNetForOther(account);
        builder.append("net 自己抵押给他人：").append(mortgageNetForOther).append(" EOS").append(" \n");
        String mortgageNetForSelf = EosParseManager.mortgageNetForSelf(account);
        builder.append("net 他人抵押给自己：").append(mortgageNetForSelf).append(" EOS").append(" \n");
        String totalNet = EosParseManager.totalNet(account);
        builder.append("net 总的：").append(totalNet).append(" EOS").append(" \n");

        String publicKey = EosParseManager.privateKeyToPublicKey(account.getPrivateKey());
        builder.append("公钥 ：").append(publicKey).append(" \n");
        builder.append("私钥 ：").append(account.getPrivateKey()).append(" \n");

        List<String> permissions = EosParseManager.getPermission(account);
        builder.append("拥有的权限 ：");
        for (String permission : permissions) {
            builder.append(permission).append(" ");
        }
        builder.append(" \n");

        Map<String, TokenShow> tokens = EosParseManager.queryTokens(account);
        for (Map.Entry<String, TokenShow> entry : tokens.entrySet()) {
            TokenShow tokenShow = entry.getValue();
            builder.append(tokenShow.getSymbol()).append(">>")
                    .append(" 单价:").append(tokenShow.getPrice()).append(" eos")
                    .append(" 数量:").append(tokenShow.getBalance()).append(" ").append(tokenShow.getSymbol().toLowerCase())
                    .append(" 总价:").append(tokenShow.getTotalPrice()).append(" eos")
                    .append(" \n");
        }
        mvpView.accountParseCallback(builder.toString());
    }

    /**
     * 退还-- 有时申请赎回3天后不一定自动到账，需要手动在触发这个动作
     */
    public void refund() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        RefundArg refundArg = new RefundArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner");
        Disposable disposable = EosChainManager.getInstance().push(refundArg.getPrivateKey(), refundArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 投票
     */
    public void vote() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        List<String> producers = new ArrayList<>();
        producers.add("acroeos12345");
        producers.add("bitfinexeos1");
        producers.add("eos42freedom");
        producers.add("eosbeijingbp");
        producers.add("eosbixinboot");

        producers.add("eoscanadacom");
        producers.add("eoscannonchn");
        producers.add("eoscybexiobp");
        producers.add("eosfishrocks");
        producers.add("eosflareiobp");

        producers.add("eosflytomars");
        producers.add("eoshuobipool");
        producers.add("eosiomeetone");
        producers.add("eosiosg11111");
        producers.add("eoslaomaocom");

        producers.add("eosnationftw");
        producers.add("eosnewyorkio");
        producers.add("eostitanprod");
        producers.add("okcapitalbp1");
        producers.add("oraclegogogo");
        producers.add("zbeosbp11111");

        VoteArg voteArg = new VoteArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", eosAccount.getName(), "", producers);
        Disposable disposable = EosChainManager.getInstance().push(voteArg.getPrivateKey(), voteArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    public void voteProxy() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }

        VoteArg voteArg = new VoteArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", eosAccount.getName(), "meetoneproxy", new ArrayList<>());
        Disposable disposable = EosChainManager.getInstance().push(voteArg.getPrivateKey(), voteArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 租赁Rex
     *
     * @param cpu 0.0001 EOS
     * @param net 0.0001 EOS
     */
    public void rexRent(String cpu, String net) {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        RexRentArg rexRent = new RexRentArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", cpu, net);
        Disposable disposable = EosChainManager.getInstance().push(rexRent.getPrivateKey(), rexRent.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 用抵押的资源买Rex
     *
     * @param cpu
     * @param net
     * @param receiver
     */
    public void unStakeToRex(String cpu, String net, String receiver) {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        UnstakeToRexArg unstakeToRex = new UnstakeToRexArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", receiver, cpu, net);
        Disposable disposable = EosChainManager.getInstance().push(unstakeToRex.getPrivateKey(), unstakeToRex.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 买Rex
     *
     * @param quant
     */
    public void buyRex(String quant) {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        BuyRexArg buyRexArg = new BuyRexArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", quant);
        Disposable disposable = EosChainManager.getInstance().push(buyRexArg.getPrivateKey(), buyRexArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 卖Rex
     *
     * @param quant
     */
    public void sellRex(String quant) {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        SellRexArg sellRexArg = new SellRexArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", quant);
        Disposable disposable = EosChainManager.getInstance().push(sellRexArg.getPrivateKey(), sellRexArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 提现Rex
     *
     * @param quant
     */
    public void withdrawRex(String quant) {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        WithdrawRexArg withdrawRexArg = new WithdrawRexArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", quant);
        Disposable disposable = EosChainManager.getInstance().push(withdrawRexArg.getPrivateKey(), withdrawRexArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 抵押资源
     *
     * @param cpu
     * @param net
     * @param receiver
     */
    public void stake(String cpu, String net, String receiver, int transfer) {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        StakeArg stakeArg = new StakeArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", eosAccount.getName(), receiver, cpu, net, transfer);
        Disposable disposable = EosChainManager.getInstance().push(stakeArg.getPrivateKey(), stakeArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 赎回资源
     *
     * @param cpu
     * @param net
     * @param receiver
     */
    public void unStake(String cpu, String net, String receiver) {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        UnStakeArg unStakeArg = new UnStakeArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", eosAccount.getName(), receiver, cpu, net);
        Disposable disposable = EosChainManager.getInstance().push(unStakeArg.getPrivateKey(), unStakeArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 买Ram
     */
    public void buyRam(String quant) {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        BuyRamArg buyRamArg = new BuyRamArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", eosAccount.getName(), quant);
        Disposable disposable = EosChainManager.getInstance().push(buyRamArg.getPrivateKey(), buyRamArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 卖Ram
     */
    public void sellRam(float quant) {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        SellRamArg sellRamArg = new SellRamArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", quant);
        Disposable disposable = EosChainManager.getInstance().push(sellRamArg.getPrivateKey(), sellRamArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 更新权限
     */
    public void updateAuth(AuthArg authArg) {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }

        UpdateAuthArg updateAuthArg = new UpdateAuthArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", authArg);
        Disposable disposable = EosChainManager.getInstance().push(updateAuthArg.getPrivateKey(), updateAuthArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    /**
     * 强制刷新CPU资源等，将会消耗资源
     */
    public void openChain() {
        EosAccount eosAccount = queryAccount();
        if (eosAccount == null || EmptyUtils.isEmpty(eosAccount.getPrivateKey())) {
            return;
        }
        OpenChainArg openChainArg = new OpenChainArg(eosAccount.getName(), eosAccount.getPrivateKey(), "owner", "4,EOS");
        Disposable disposable = EosChainManager.getInstance().push(openChainArg.getPrivateKey(), openChainArg.getAbis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> mvpView.transactionCallback(response, null), throwable -> mvpView.transactionCallback(null, throwable));

    }

    public void signProvider() {

    }
}