package com.easy.eoschain.action;


import com.alibaba.fastjson.JSON;
import com.easy.eoschain.base.ChainApi;
import com.easy.eoschain.bean.BlockIdDetails;
import com.easy.eoschain.bean.response.ChainError;
import com.easy.eoschain.bean.response.ChainResponse;
import com.easy.eoschain.bean.response.TransactionCommitted;
import com.easy.eoschain.encrypt.abi.ActionAbi;
import com.easy.eoschain.encrypt.abi.SignedTransactionAbi;
import com.easy.eoschain.encrypt.abi.TransactionAbi;
import com.easy.eoschain.encrypt.crypto.signature.PrivateKeySigning;
import com.easy.eoschain.encrypt.info.Info;
import com.easy.eoschain.encrypt.signing.PushTransaction;
import com.easy.eoschain.encrypt.write.TransactionWriter;
import com.easy.eoschain.manager.EosPrivateKey;
import com.easy.framework.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;

public class ChainTransaction {
    ChainApi chainApi;

    public ChainTransaction(ChainApi chainApi) {
        this.chainApi = chainApi;
    }

    private Date transactionDefaultExpiry() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 2);
        return calendar.getTime();
    }

    private byte[] getWriter(Response<Info> info, TransactionAbi transaction) {
        return new TransactionWriter().writer(new SignedTransactionAbi(info.body().getChain_id(), transaction, new ArrayList<>())).toBytes();
    }

    private TransactionAbi transaction(Date expirationDate, BlockIdDetails blockIdDetails, List<ActionAbi> actions) {
        return new TransactionAbi(
                expirationDate,
                blockIdDetails.getBlockNum(),
                blockIdDetails.getBlockPrefix(),
                0,
                0,
                0,
                new ArrayList<>(),
                actions,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>());
    }

    public Single<ChainResponse<TransactionCommitted>> push(String privateKey, List<ActionAbi> actions) {
        Date expirationDate = transactionDefaultExpiry();
        return chainApi.getChainInfo().flatMap(info -> {
            if (info.isSuccessful()) {
                TransactionAbi transaction = transaction(expirationDate, new BlockIdDetails(info.body().getHead_block_id()), actions);
                String signature = new PrivateKeySigning().sign(getWriter(info, transaction), new EosPrivateKey(privateKey));
                PushTransaction pushTransaction = new PushTransaction(
                        Arrays.asList(signature),
                        "none",
                        "",
                        new TransactionWriter().squishTransactionAbi(transaction).toHex());
                return chainApi.pushTransaction(pushTransaction);
            } else {
                Response<TransactionCommitted> response = Response.error(info.code(), info.errorBody());
                return Single.just(response);
            }
        }).map(response -> getChainResponse(response));
    }

    public ChainResponse<TransactionCommitted> getChainResponse(Response<TransactionCommitted> response) {
        ChainError chainError = null;
        try {
            String errorBody = response.errorBody().string();
            if (Utils.isNotEmpty(errorBody)) {
                chainError = JSON.parseObject(errorBody, ChainError.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ChainResponse<>(
                response.isSuccessful(),
                response.code(),
                response.body(),
                chainError);
    }
}
