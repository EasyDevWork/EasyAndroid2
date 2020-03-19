package com.easy.eoschain.action.args;

import com.easy.eoschain.encrypt.abi.ActionAbi;
import com.easy.eoschain.encrypt.abi.TransactionAuthorizationAbi;

import java.util.Arrays;
import java.util.List;

public abstract class BaseArg {
    String privateKey;//私钥
    String actor;//账号名称
    String permission;//"owner" ,"active"

    public abstract List<ActionAbi> getAbis();

    public BaseArg(String actor, String privateKey, String permission) {
        this.privateKey = privateKey;
        this.actor = actor;
        this.permission = permission;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<TransactionAuthorizationAbi> getTransactionAuthorizationAbi() {
        return Arrays.asList(new TransactionAuthorizationAbi(actor, permission));
    }
}
