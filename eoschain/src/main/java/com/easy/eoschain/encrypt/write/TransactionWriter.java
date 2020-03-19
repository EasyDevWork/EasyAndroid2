package com.easy.eoschain.encrypt.write;



import com.easy.eoschain.encrypt.abi.ActionAbi;
import com.easy.eoschain.encrypt.abi.SignedTransactionAbi;
import com.easy.eoschain.encrypt.abi.TransactionAbi;
import com.easy.eoschain.encrypt.abi.TransactionAuthorizationAbi;
import com.easy.eoschain.encrypt.compression.CompressionType;
import com.easy.eoschain.encrypt.crypto.hex.DefaultHexWriter;

import java.util.List;


public class TransactionWriter extends BaseWriter {

    public TransactionWriter() {
        super(new DefaultByteWriter(1048), new DefaultHexWriter(), CompressionType.NONE);
    }

    public BaseWriter writer(SignedTransactionAbi signedtransactionabi) {
        byteWriter.putChainId(signedtransactionabi.getGetChainId());
        squishTransactionAbi(signedtransactionabi.getGetTransaction());
        byteWriter.putHexCollection(signedtransactionabi.getGetContextFreeData());
        return this;
    }

    public BaseWriter squishTransactionAbi(TransactionAbi transactionabi) {
        byteWriter.putTimestampMs(transactionabi.getGetExpiration());
        byteWriter.putBlockNum(transactionabi.getGetRefBlockNum());
        byteWriter.putBlockPrefix(transactionabi.getGetRefBlockPrefix());
        byteWriter.putVariableUInt(transactionabi.getGetMaxNetUsageWords());
        byteWriter.putVariableUInt(transactionabi.getGetMaxCpuUsageMs());
        byteWriter.putVariableUInt(transactionabi.getGetDelaySec());
        squishCollectionActionAbi(transactionabi.getGetContextFreeActions());
        squishCollectionActionAbi(transactionabi.getGetActions());
        byteWriter.putStringCollection(transactionabi.getGetTransactionExtensions());
        return this;
    }

    private void squishCollectionActionAbi(List<ActionAbi> actionabiList) {
        byteWriter.putVariableUInt((long) actionabiList.size());
        for (ActionAbi actionAbi : actionabiList) {
            actionAbi(actionAbi);
        }
    }

    public void actionAbi(ActionAbi actionabi) {
        byteWriter.putAccountName(actionabi.getAccount());
        byteWriter.putName(actionabi.getName());
        squishCollectionTransactionAuthorizationAbi(actionabi.getAuthorization());
        byteWriter.putData(actionabi.getData());
    }

    public void squishCollectionTransactionAuthorizationAbi(List<TransactionAuthorizationAbi> transactionauthorizationabiList) {
        byteWriter.putVariableUInt((long) transactionauthorizationabiList.size());
        for (TransactionAuthorizationAbi transactionAuthorizationAbi : transactionauthorizationabiList) {
            authorizationAbi(transactionAuthorizationAbi);
        }
    }

    public void authorizationAbi(TransactionAuthorizationAbi transactionauthorizationabi) {
        byteWriter.putAccountName(transactionauthorizationabi.getGetActor());
        byteWriter.putAccountName(transactionauthorizationabi.getGetPermission());
    }
}
