package com.easy.eoschain.action;

import com.easy.eoschain.bean.response.ChainResponse;
import com.easy.eoschain.bean.response.TransactionCommitted;

public interface ActionCallback {
    void callback(ChainResponse<TransactionCommitted> chainResponse,Throwable throwable);
}
