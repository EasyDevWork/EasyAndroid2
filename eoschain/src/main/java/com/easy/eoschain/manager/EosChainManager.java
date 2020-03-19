package com.easy.eoschain.manager;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easy.eoschain.action.ChainTransaction;
import com.easy.eoschain.base.ChainApi;
import com.easy.eoschain.bean.CurrencyInfo;
import com.easy.eoschain.bean.TableRows;
import com.easy.eoschain.bean.response.ChainResponse;
import com.easy.eoschain.bean.response.TransactionCommitted;
import com.easy.eoschain.encrypt.abi.ActionAbi;
import com.easy.eoschain.encrypt.crypto.signature.PrivateKeySigning;
import com.easy.eoschain.utils.EosUtils;
import com.easy.framework.Http.RetrofitConfig;
import com.easy.framework.Http.RxHttp;
import com.easy.framework.Http.callback.HttpCallback;
import com.easy.framework.Http.retrofit.RetrofitUtils;
import com.easy.framework.rxlifecycle.LifecycleProvider;
import com.easy.framework.utils.Utils;

import org.greenrobot.essentials.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EosChainManager {
    Retrofit retrofit;

    public static final class Holder {
        public static final EosChainManager INSTANCE = new EosChainManager();
    }

    private EosChainManager() {

    }

    public static EosChainManager getInstance() {
        return EosChainManager.Holder.INSTANCE;
    }

    public void init(Context context, String baseUrl) {
        RetrofitConfig.Builder builder = new RetrofitConfig.Builder(context);
        builder.baseUrl(baseUrl);
        retrofit = RetrofitUtils.get().createRetrofit(builder.build());
    }

    /**
     * 获取链信息
     */
    public void requestChainInfo(LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
        RxHttp.get("v1/chain/get_info")
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
    }

    /**
     * 查询合约账号信息
     */
    public void requestContract(String accountName, LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account_name", accountName);
        RxHttp.post("v1/chain/get_abi")
                .setBodyJson(JSON.toJSONString(jsonObject))
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
    }

    /**
     * 查询投票列表
     */
    public void requestVoteList(LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("json", true);
        jsonObject.put("lower_bound", "");
        jsonObject.put("limit", 999);
        RxHttp.post("v1/chain/get_producers")
                .setBodyJson(JSON.toJSONString(jsonObject))
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
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
    public void requestUsdtPrice(LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
        RxHttp.get("https://www.ethte.com/eos_price/eos_usd")
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
    }

    /**
     * 获取单个货币数量
     *
     * @param lifecycleProvider
     * @param httpCallback
     */
    public void requestCurrencyBalance(CurrencyInfo balance, LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
        RxHttp.post("v1/chain/get_currency_balance")
                .setBodyJson(JSON.toJSONString(balance))
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
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
        return retrofit.create(ChainApi.class).getCurrencyBalance(currencyInfo).map(listResponse -> {
            List<String> list = listResponse.body();
            if (!Utils.isEmpty(list)) {
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
    public void requestRamPrice(LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
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
        RxHttp.post("v1/chain/get_table_rows")
                .setBodyJson(JSON.toJSONString(tableRows))
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
    }

    /**
     * 获取账号信息
     */
    public void requestAccountInfo(String accountName, LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account_name", accountName);
        RxHttp.post("v1/chain/get_account")
                .setBodyJson(jsonObject.toJSONString())
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
    }

    /**
     * 获取账号下有金额的货币列表
     */
    public void requestTokenData(String accountName, LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account", accountName);
        RxHttp.post("https://www.ethte.com/account_info/v1/get_account")
                .setBodyJson(jsonObject.toJSONString())
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
    }

    /**
     * 获取token当前价格
     */
    public void requestTokenPrice(LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
        RxHttp.get("https://www.ethte.com/eos_price/tokens_eos")
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
    }

    /**
     * 请求抵押列表
     *
     * @param accountName
     * @param lifecycleProvider
     * @param httpCallback
     */
    public void requestStakeData(String accountName, LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
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
        RxHttp.post("v1/chain/get_table_rows")
                .setBodyJson(JSON.toJSONString(tableRows))
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
    }

    /**
     * 获取RexFund数据
     *
     * @param accountName
     * @param lifecycleProvider
     * @param httpCallback
     */
    public void requestRexFundData(String accountName, LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
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
        RxHttp.post("v1/chain/get_table_rows")
                .setBodyJson(JSON.toJSONString(tableRows))
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
    }

    /**
     * 获取Rex数据
     *
     * @param accountName
     * @param lifecycleProvider
     * @param httpCallback
     */
    public void requestRexData(String accountName, LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
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
        RxHttp.post("v1/chain/get_table_rows")
                .setBodyJson(JSON.toJSONString(tableRows))
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
    }

    /**
     * 获取Rex价值
     *
     * @param lifecycleProvider
     * @param httpCallback
     */
    public void requestRexPriceData(LifecycleProvider lifecycleProvider, HttpCallback httpCallback) {
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
        RxHttp.post("v1/chain/get_table_rows")
                .setBodyJson(JSON.toJSONString(tableRows))
                .lifecycle(lifecycleProvider)
                .request(retrofit, httpCallback);
    }

    /**
     * 推送到链上
     *
     * @param privateKey
     * @param actions
     * @return
     */
    public Observable<ChainResponse<TransactionCommitted>> push(String privateKey, List<ActionAbi> actions) {
        ChainTransaction transaction = new ChainTransaction(retrofit.create(ChainApi.class));
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
