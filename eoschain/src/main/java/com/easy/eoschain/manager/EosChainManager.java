package com.easy.eoschain.manager;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easy.eoschain.action.ChainTransaction;
import com.easy.eoschain.base.ChainApi;
import com.easy.eoschain.bean.AbiContract;
import com.easy.eoschain.bean.ChainAccount;
import com.easy.eoschain.bean.ChainInfo;
import com.easy.eoschain.bean.CurrencyInfo;
import com.easy.eoschain.bean.ProducerInfo;
import com.easy.eoschain.bean.TableRows;
import com.easy.eoschain.bean.response.ChainResponse;
import com.easy.eoschain.bean.response.TransactionCommitted;
import com.easy.eoschain.encrypt.abi.ActionAbi;
import com.easy.eoschain.encrypt.crypto.signature.PrivateKeySigning;
import com.easy.eoschain.utils.EosUtils;
import com.easy.net.RxHttp;
import com.easy.net.beans.Response;
import com.easy.net.callback.HttpCallback;
import com.easy.net.retrofit.RetrofitHelp;
import com.easy.store.bean.eoschain.RamPrice;
import com.easy.store.bean.eoschain.RexBean;
import com.easy.store.bean.eoschain.RexFund;
import com.easy.store.bean.eoschain.RexPrice;
import com.easy.store.bean.eoschain.StakeBean;
import com.easy.utils.EmptyUtils;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class EosChainManager {
    TreeMap<String, Object> heads = new TreeMap<>();

    public static final class Holder {
        public static final EosChainManager INSTANCE = new EosChainManager();
    }

    private EosChainManager() {
        heads.put("host_group", "EosHost");
    }

    public static EosChainManager getInstance() {
        return EosChainManager.Holder.INSTANCE;
    }

    /**
     * 获取链信息
     */
    public Observable<Response<ChainInfo>> requestChainInfo() {
        return RxHttp.get("v1/chain/get_info")
                .addHeader(heads)
                .request(ChainInfo.class);
    }

    /**
     * 查询合约账号信息
     */
    public Observable<Response<AbiContract>> requestContract(String accountName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account_name", accountName);
        return RxHttp.post("v1/chain/get_abi")
                .addHeader(heads)
                .setBodyJson(JSON.toJSONString(jsonObject))
                .request(AbiContract.class);
    }

    /**
     * 查询投票列表
     */
    public Observable<Response<ProducerInfo>> requestVoteList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("json", true);
        jsonObject.put("lower_bound", "");
        jsonObject.put("limit", 999);
        return RxHttp.post("v1/chain/get_producers")
                .addHeader(heads)
                .setBodyJson(JSON.toJSONString(jsonObject))
                .request(ProducerInfo.class);
    }

    /**
     * 获取公私钥对
     *
     * @return
     */
    public String[] createPrivateAndPublicKey() {
        String[] keys = new String[]{"", ""};
        EosPrivateKey eosPrivateKey = new EosPrivateKey();
        keys[0] = eosPrivateKey.getPublicKey().toString();
        keys[1] = eosPrivateKey.toString();
        return keys;
    }

    /**
     * 获取USDT价格
     */
    public Observable<Response<String>> requestUsdtPrice() {
        return RxHttp.get("https://www.ethte.com/eos_price/eos_usd")
                .addHeader(heads)
                .request(String.class);
    }

    /**
     * 获取单个货币数量
     */
    public Observable<Response<String>> requestCurrencyBalance(CurrencyInfo balance) {
        return RxHttp.post("v1/chain/get_currency_balance")
                .setBodyJson(JSON.toJSONString(balance))
                .addHeader(heads)
                .request(String.class);
    }

    /**
     * 获取多个货币数量---requestCurrencyBalance的扩展
     *
     * @param currencyInfos
     * @return
     */
    public Observable<List<CurrencyInfo>> requestCurrencyListBalance(List<CurrencyInfo> currencyInfos) {
        return Observable.create((ObservableOnSubscribe<List<CurrencyInfo>>) emitter -> {
            emitter.onNext(currencyInfos);
            emitter.onComplete();
        }).flatMap((Function<List<CurrencyInfo>, ObservableSource<List<CurrencyInfo>>>) currencyInfoList -> {
            List<Observable<CurrencyInfo>> observableList = new ArrayList<>();
            for (CurrencyInfo balance : currencyInfoList) {
                observableList.add(getCurrencyBalance(balance).toObservable());
            }
            return Observable.zip(observableList, objects -> currencyInfos);
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 获取TOken 金额--已验证
     *
     * @param currencyInfo
     * @return
     */
    public Single<CurrencyInfo> getCurrencyBalance(CurrencyInfo currencyInfo) {
        return RetrofitHelp.get().getRetrofit().create(ChainApi.class).getCurrencyBalance(currencyInfo).map(listResponse -> {
            List<String> list = listResponse.body();
            if (!EmptyUtils.isEmpty(list)) {
                currencyInfo.setBalance(EosUtils.getNumOfData(list.get(0)));
            } else {
                currencyInfo.setBalance("0.0000");
            }
            return currencyInfo;
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 获取Ram价格
     */
    public Observable<Response<RamPrice>> requestRamPrice() {
        TableRows tableRows = new TableRows();
        tableRows.setScope("eosio");
        tableRows.setCode("eosio");
        tableRows.setTable("rammarket");
        tableRows.setTable_key("");
        tableRows.setJson(true);
        tableRows.setLimit(1);
        tableRows.setLower_bound("");
        tableRows.setUpper_bound("");
        tableRows.setKey_type("");
        tableRows.setIndex_position("");
        tableRows.setEncode_type("dec");
        return RxHttp.post("v1/chain/get_table_rows")
                .addHeader(heads)
                .setBodyJson(JSON.toJSONString(tableRows))
                .request(RamPrice.class);
    }

    /**
     * 获取账号信息
     */
    public Observable<Response<ChainAccount>> requestAccountInfo(String accountName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account_name", accountName);
        return RxHttp.post("v1/chain/get_account")
                .setBodyJson(jsonObject.toJSONString())
                .addHeader(heads)
                .request(ChainAccount.class);
    }

    /**
     * 获取账号下有金额的货币列表
     */
    public Observable<Response<String>> requestTokenData(String accountName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account", accountName);
        return RxHttp.post("https://www.ethte.com/account_info/v1/get_account")
                .setBodyJson(jsonObject.toJSONString())
                .addHeader(heads)
                .request(String.class);
    }

    /**
     * 获取token当前价格
     */
    public Observable<Response<String>> requestTokenPrice() {
        return RxHttp.get("https://www.ethte.com/eos_price/tokens_eos")
                .addHeader(heads)
                .request(String.class);
    }

    /**
     * 请求抵押列表
     *
     * @param accountName
     */
    public Observable<Response<StakeBean>> requestStakeData(String accountName) {
        TableRows tableRows = new TableRows();
        tableRows.setScope(accountName);
        tableRows.setCode("eosio");
        tableRows.setTable("delband");
        tableRows.setTable_key("");
        tableRows.setJson(true);
        tableRows.setLimit(1000);
        tableRows.setLower_bound("");
        tableRows.setUpper_bound("");
        tableRows.setKey_type("");
        tableRows.setIndex_position("");
        tableRows.setEncode_type("dec");
        return RxHttp.post("v1/chain/get_table_rows")
                .addHeader(heads)
                .setBodyJson(JSON.toJSONString(tableRows))
                .request(StakeBean.class);
    }

    /**
     * 获取RexFund数据
     *
     * @param accountName
     */
    public Observable<Response<RexFund>> requestRexFundData(String accountName) {
        TableRows tableRows = new TableRows();
        tableRows.setScope("eosio");
        tableRows.setCode("eosio");
        tableRows.setTable("rexfund");
        tableRows.setTable_key("");
        tableRows.setJson(true);
        tableRows.setLimit(1);
        tableRows.setLower_bound(accountName);
        tableRows.setUpper_bound(accountName);
        tableRows.setKey_type("");
        tableRows.setIndex_position("1");
        tableRows.setEncode_type("");
        return RxHttp.post("v1/chain/get_table_rows")
                .addHeader(heads)
                .setBodyJson(JSON.toJSONString(tableRows))
                .request(RexFund.class);
    }

    /**
     * 获取Rex数据
     *
     * @param accountName
     */
    public Observable<Response<RexBean>> requestRexData(String accountName) {
        TableRows tableRows = new TableRows();
        tableRows.setScope("eosio");
        tableRows.setCode("eosio");
        tableRows.setTable("rexbal");
        tableRows.setTable_key("");
        tableRows.setJson(true);
        tableRows.setLimit(1);
        tableRows.setLower_bound(accountName);
        tableRows.setUpper_bound(accountName);
        tableRows.setKey_type("");
        tableRows.setIndex_position("");
        tableRows.setEncode_type("");
        return RxHttp.post("v1/chain/get_table_rows")
                .addHeader(heads)
                .setBodyJson(JSON.toJSONString(tableRows))
                .request(RexBean.class);
    }

    /**
     * 获取Rex价值
     */
    public Observable<Response<RexPrice>> requestRexPriceData() {
        TableRows tableRows = new TableRows();
        tableRows.setScope("eosio");
        tableRows.setCode("eosio");
        tableRows.setTable("rexpool");
        tableRows.setTable_key("");
        tableRows.setJson(true);
        tableRows.setLimit(1);
        tableRows.setLower_bound("");
        tableRows.setUpper_bound("");
        tableRows.setKey_type("");
        tableRows.setIndex_position("");
        tableRows.setEncode_type("");
        return RxHttp.post("v1/chain/get_table_rows")
                .addHeader(heads)
                .setBodyJson(JSON.toJSONString(tableRows))
                .request(RexPrice.class);
    }

    /**
     * 推送到链上
     *
     * @param privateKey
     * @param actions
     * @return
     */
    public Observable<ChainResponse<TransactionCommitted>> push(String privateKey, List<ActionAbi> actions) {
        ChainTransaction transaction = new ChainTransaction(RetrofitHelp.get().getRetrofit().create(ChainApi.class));
        return transaction.push(privateKey, actions).toObservable();
    }

    /**
     * 签名
     *
     * @param data
     * @param isSignProvider
     * @return
     */
    public String signature(String privateKey, String data, boolean isSignProvider) {
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(privateKey)) {
            return null;
        }
//       data eg. {"isArbitrary":true,"data":"芝士就是力量","isHash":true,"whatfor":"meet-dev-tools"}
        JSONObject jsonObject = JSON.parseObject(data);
        byte[] byteArray;
        if (isSignProvider) {
            JSONArray jsonArray = jsonObject.getJSONArray("buf");
            byteArray = new byte[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                byteArray[i] = jsonArray.getInteger(i).byteValue();
            }
        } else {
            String buf = jsonObject.getString("data");
            byteArray = buf.getBytes();
        }
        PrivateKeySigning signing = new PrivateKeySigning();
        return signing.sign(byteArray, new EosPrivateKey(privateKey));
    }
}
