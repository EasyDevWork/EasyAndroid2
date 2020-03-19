package com.easy.store.dao;


import android.text.TextUtils;

import com.easy.store.bean.EosAccount;
import com.easy.store.bean.EosAccount_;

import javax.inject.Inject;

import io.objectbox.Box;

public class EosAccountDao extends BaseDao {

    Box<EosAccount> eosAccountBox;

    @Inject
    public EosAccountDao() {
        if (boxStore != null) {
            eosAccountBox = boxStore.boxFor(EosAccount.class);
        }
    }

    public void insertOrUpdate(EosAccount eosAccount) {
        if (eosAccount == null) {
            return;
        }
        eosAccountBox.put(eosAccount);
    }

    public EosAccount query(String accountName, String netType) {
        if (TextUtils.isEmpty(accountName) || TextUtils.isEmpty(netType)) {
            return null;
        }
        return eosAccountBox.query().equal(EosAccount_.name, accountName).equal(EosAccount_.netType, netType).build().findFirst();
    }

    public EosAccount query() {
        return eosAccountBox.query().build().findFirst();
    }

    public int delete(EosAccount eosAccount) {
        if (eosAccount == null || eosAccount.getId() == 0) {
            return 0;
        } else {
            eosAccountBox.remove(eosAccount);
            return 1;
        }
    }
}
