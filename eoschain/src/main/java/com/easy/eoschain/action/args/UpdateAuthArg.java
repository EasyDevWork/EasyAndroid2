package com.easy.eoschain.action.args;

import com.easy.eoschain.action.Writer;
import com.easy.eoschain.encrypt.abi.ActionAbi;

import java.util.ArrayList;
import java.util.List;

public class UpdateAuthArg extends BaseArg {
    private AuthArg auth;

    public UpdateAuthArg(String actor, String privateKey, String permission, AuthArg auth) {
        super(actor, privateKey, permission);
        this.auth = auth;
    }

    public AuthArg getAuth() {
        return auth;
    }

    public void setAuth(AuthArg auth) {
        this.auth = auth;
    }

    @Override
    public List<ActionAbi> getAbis() {
        List<ActionAbi> actionAbis = new ArrayList<>();
        String updateAuth = new Writer().updateAuth(auth).toHex();
        actionAbis.add(new ActionAbi("eosio", "updateauth", getTransactionAuthorizationAbi(), updateAuth));
        return actionAbis;
    }
}
