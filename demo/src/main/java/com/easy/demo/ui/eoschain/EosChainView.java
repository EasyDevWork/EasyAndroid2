package com.easy.demo.ui.eoschain;

import com.easy.eoschain.bean.AbiContract;
import com.easy.eoschain.bean.ChainAccount;
import com.easy.eoschain.bean.ChainInfo;
import com.easy.eoschain.bean.CurrencyInfo;
import com.easy.eoschain.bean.ProducerInfo;
import com.easy.eoschain.bean.response.ChainResponse;
import com.easy.eoschain.bean.response.TransactionCommitted;
import com.easy.framework.base.BaseView;
import com.easy.net.beans.Response;
import com.easy.store.bean.EosAccount;
import com.easy.store.bean.eoschain.Eos2UsdtPrice;
import com.easy.store.bean.eoschain.RexBean;
import com.easy.store.bean.eoschain.RexFund;
import com.easy.store.bean.eoschain.RexPrice;
import com.easy.store.bean.eoschain.StakeBean;
import com.easy.store.bean.eoschain.Token;

import java.util.List;

public interface EosChainView extends BaseView {

    void chainInfoCallback(Response<ChainInfo> response);

    void chainAccountInfoCallback(Response<ChainAccount> response, EosAccount eosAccount);

    void accountParseCallback(String result);

    void stakeDataCallback(Response<StakeBean> response);

    void rexDataCallback(Response<RexFund> response);

    void rexBeanDataCallback(Response<RexBean> response);

    void rexPriceCallback(Response<RexPrice> response);

    void tokenCallback(Response<String> response);

    void usdtPriceCallback(Response<String> response);

    void transactionCallback(ChainResponse<TransactionCommitted> chainResponse, Throwable throwable);

    void ramPriceCallback(Response response);

    void currencyBalanceCallback(Response<String> response);

    void currencyListBalanceCallback(List<CurrencyInfo> currencyInfos);

    void requestContractCallback(Response<AbiContract> response);

    void requestVoteListCallback(Response<ProducerInfo> response);
}
