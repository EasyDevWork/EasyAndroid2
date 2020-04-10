package com.easy.store.dao;

import com.easy.store.bean.Accounts;

import javax.inject.Inject;

import io.objectbox.Box;
import io.objectbox.android.ObjectBoxLiveData;

public class AccountsDao extends BaseDao {

    Box<Accounts> accountsBox;
    private ObjectBoxLiveData<Accounts> accountsLiveData;

    @Inject
    public AccountsDao() {
        if (boxStore != null) {
            accountsBox = boxStore.boxFor(Accounts.class);
        }
    }

    public ObjectBoxLiveData<Accounts> getAccountsLiveData() {
        if (accountsLiveData == null) {
            accountsLiveData = new ObjectBoxLiveData<>(accountsBox.query().build());
        }
        return accountsLiveData;
    }

    public void add(Accounts accounts) {
        accountsBox.removeAll();
        accountsBox.put(accounts);
    }

    public void update(Accounts accounts) {
        accountsBox.put(accounts);
    }
}
