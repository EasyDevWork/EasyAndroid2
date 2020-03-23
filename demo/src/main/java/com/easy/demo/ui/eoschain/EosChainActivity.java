package com.easy.demo.ui.eoschain;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestEosChainBinding;
import com.easy.eoschain.action.args.AuthArg;
import com.easy.eoschain.bean.AbiContract;
import com.easy.eoschain.bean.ChainAccount;
import com.easy.eoschain.bean.ChainInfo;
import com.easy.eoschain.bean.CurrencyInfo;
import com.easy.eoschain.bean.ProducerInfo;
import com.easy.eoschain.bean.response.ChainResponse;
import com.easy.eoschain.bean.response.TransactionCommitted;
import com.easy.eoschain.manager.EosChainManager;
import com.easy.framework.base.BaseActivity;
import com.easy.net.beans.Response;
import com.easy.net.event.ActivityEvent;
import com.easy.store.bean.EosAccount;
import com.easy.store.bean.eoschain.Eos2UsdtPrice;
import com.easy.store.bean.eoschain.KeysBean;
import com.easy.store.bean.eoschain.RamPrice;
import com.easy.store.bean.eoschain.RequiredAuth;
import com.easy.store.bean.eoschain.RexBean;
import com.easy.store.bean.eoschain.RexPrice;
import com.easy.store.bean.eoschain.StakeBean;
import com.easy.store.bean.eoschain.Token;
import com.easy.utils.ClipBoardUtils;
import com.easy.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@ActivityInject
@Route(path = "/demo/EosChainActivity", name = "eos相关")
public class EosChainActivity extends BaseActivity<EosChainPresenter, TestEosChainBinding> implements EosChainView<ActivityEvent> {

    private String domain = "https://mainnet.meet.one";
    private String netType = "EOS";
    private String testPrivateKey = "5JeLoyLLWEPCmu9jPzBd9bGDyHAJQNY77CdCo5rTqa2dvwp7GdF";

    @Override
    public int getLayoutId() {
        return R.layout.test_eos_chain;
    }

    @Override
    public void initView() {
        EosChainManager.getInstance().init(this, domain);
        viewBind.tvScreen.setOnLongClickListener(view -> {
            ClipBoardUtils.copyToClipBoard(this, "", viewBind.tvScreen.getText().toString());
            return true;
        });
    }

    public void createWallet(View v) {
        String accountName = viewBind.editName.getText().toString();
        if (Utils.isEmpty(accountName)) {
            accountName = viewBind.editName.getHint().toString();
        }
        presenter.createWallet(accountName, netType);
    }

    public void deleteWallet(View v) {
        String accountName = viewBind.editName.getText().toString();
        if (Utils.isEmpty(accountName)) {
            accountName = viewBind.editName.getHint().toString();
        }
        presenter.deleteWallet(accountName, netType);
    }

    public void createPrivateAndPublicKey(View v) {
        String[] keys = presenter.createPrivateAndPublicKey();
        viewBind.tvScreen.setText("公:" + keys[0] + " \n私:" + keys[1]);
    }

    public void requestChainInfo(View v) {
        presenter.requestChainInfo(getRxLifecycle());
    }

    public void requestCurrencyBalance(View v) {
        CurrencyInfo balance = new CurrencyInfo();
        balance.setAccount("johntrump123");
        balance.setCode("eosio.token");
        balance.setSymbol("eos");
        presenter.requestCurrencyBalance(balance, getRxLifecycle());
    }

    public void requestCurrencyListBalance(View v) {
        List<CurrencyInfo> currencyInfos = new ArrayList<>();
        CurrencyInfo eos = new CurrencyInfo();
        eos.setAccount("johntrump123");
        eos.setCode("eosio.token");
        eos.setSymbol("eos");
        currencyInfos.add(eos);

        CurrencyInfo meetone = new CurrencyInfo();
        meetone.setAccount("johntrump123");
        meetone.setCode("eosiomeetone");
        meetone.setSymbol("meetone");
        currencyInfos.add(meetone);

        presenter.requestCurrencyListBalance(currencyInfos, getRxLifecycle());
    }

    public void queryContract(View v) {
        presenter.requestContract("whaleextrust", getRxLifecycle());
    }

    public void queryVoteInfo(View v) {
        presenter.requestVoteList(getRxLifecycle());
    }

    public void requestUsdtPrice(View v) {
        presenter.requestUsdtPrice(getRxLifecycle());
    }

    public void requestRamPrice(View v) {
        presenter.requestRamPrice(getRxLifecycle());
    }

    public void requestAccountInfo(View v) {
        presenter.requestAccountInfo(getRxLifecycle());
    }

    public void requestCurrencyList(View v) {
        presenter.requestCurrencyList(getRxLifecycle());
    }

    public void requestCurrencyPrice(View v) {
        presenter.requestCurrencyPrice(getRxLifecycle());
    }

    public void requestStakeData(View v) {
        presenter.requestStakeData(getRxLifecycle());
    }

    public void requestRexData(View v) {
        presenter.requestRexData(getRxLifecycle());
    }

    public void requestRexPrice(View v) {
        presenter.requestRexPrice(getRxLifecycle());
    }

    public void requestRexFund(View v) {
        presenter.requestRexFundData(getRxLifecycle());
    }

    public void buyRam(View v) {
        presenter.buyRam("0.0001 EOS", getRxLifecycle());
    }

    public void sellRam(View v) {
        presenter.sellRam(1, getRxLifecycle());
    }

    public void parseAccountInfo(View v) {
        presenter.parseAccountInfo();
    }

    public void openChain(View v) {
        presenter.openChain(getRxLifecycle());
    }

    public void refund(View v) {
        presenter.refund(getRxLifecycle());
    }

    public void vote(View v) {
        presenter.vote(getRxLifecycle());
    }

    public void voteProxy(View v) {
        presenter.voteProxy(getRxLifecycle());
    }

    public void signProvider(View v) {
        presenter.signProvider(getRxLifecycle());
    }

    public void updateAuth(View v) {
        //新的权限数据
        List<KeysBean> keys = new ArrayList<>();
        KeysBean key = new KeysBean("EOS55uX2S6zeN1seLiJRAiP2M79o99GkZcoYA3FeWzknoHyc5EanK", Short.valueOf("1"));
        keys.add(key);
        RequiredAuth auth = new RequiredAuth(1, keys, new ArrayList<>(), new ArrayList<>());
        AuthArg authArg = new AuthArg("johntrump123", "active", "owner", auth);
        presenter.updateAuth(authArg, getRxLifecycle());
    }

    public void inputPrivateKey(View v) {
        String privateKey = viewBind.editPrivateKey.getText().toString();
        if (Utils.isEmpty(privateKey)) {
            privateKey = testPrivateKey;
        }
        presenter.setPrivateKey(privateKey);
    }

    public void rexRent(View v) {
        showLoading();
        presenter.rexRent("0.0001 EOS", "0.0001 EOS", getRxLifecycle());
    }

    public void buyRexWithStake(View v) {
        showLoading();
        presenter.unStakeToRex("0.0001 EOS", "0.0001 EOS", "johntrump123", getRxLifecycle());
    }

    public void stake(View v) {
        showLoading();
        presenter.stake("0.0001 EOS", "0.0000 EOS", "googlehenlan", 0, getRxLifecycle());
    }

    public void unstake(View v) {
        showLoading();
        presenter.unStake("0.0001 EOS", "0.0000 EOS", "googlehenlan", getRxLifecycle());
    }

    public void buyRex(View v) {
        showLoading();
        presenter.buyRex("0.0001 EOS", getRxLifecycle());
    }

    public void sellRex(View v) {
        showLoading();
        presenter.sellRex("0.0001 EOS", getRxLifecycle());
    }

    public void withdrawRex(View v) {
        showLoading();
        presenter.withdrawRex("0.0001 EOS", getRxLifecycle());
    }

    @Override
    public void chainInfoCallback(Response<ChainInfo> response) {
        if (response.getCode() == Response.SUCCESS_STATE) {
            ChainInfo chainInfo = response.getResultObj();
            viewBind.tvScreen.setText(chainInfo.toString());
        } else {
            viewBind.tvScreen.setText(response.toString());
        }
    }

    @Override
    public void chainAccountInfoCallback(Response<ChainAccount> response, EosAccount eosAccount) {
        if (response.getCode() == Response.SUCCESS_STATE) {
            ChainAccount chainAccount = response.getResultObj();
            viewBind.tvScreen.setText(response.getOriData());
        } else {
            viewBind.tvScreen.setText(response.toString());
        }
    }

    @Override
    public void accountParseCallback(String result) {
        viewBind.tvScreen.setText(result);
    }

    @Override
    public void stakeDataCallback(Response<StakeBean> response) {
        if (response.getCode() == Response.SUCCESS_STATE) {
            StakeBean stakeBean = response.getResultObj();
            viewBind.tvScreen.setText(response.getOriData());
        } else {
            viewBind.tvScreen.setText(response.toString());
        }
    }

    @Override
    public void rexDataCallback(Response<RexBean> response) {
        if (response.getCode() == Response.SUCCESS_STATE) {
            RexBean rexBean = response.getResultObj();
            viewBind.tvScreen.setText(response.getOriData());
        } else {
            viewBind.tvScreen.setText(response.toString());
        }
    }

    @Override
    public void rexPriceCallback(Response<RexPrice> response) {
        if (response.getCode() == Response.SUCCESS_STATE) {
            RexPrice rexPrice = response.getResultObj();
            viewBind.tvScreen.setText(response.getOriData());
        } else {
            viewBind.tvScreen.setText(response.toString());
        }
    }

    @Override
    public void tokenCallback(Response<List<Token>> response) {
        if (response.getCode() == Response.SUCCESS_STATE) {
            List<Token> tokens = response.getResultObj();
            viewBind.tvScreen.setText(tokens.toString());
        } else {
            viewBind.tvScreen.setText(response.toString());
        }
    }

    @Override
    public void usdtPriceCallback(Response<Eos2UsdtPrice> response) {
        if (response.getCode() == Response.SUCCESS_STATE) {
            Eos2UsdtPrice eos2UsdtPrice = response.getResultObj();
            viewBind.tvScreen.setText(response.getOriData());
        } else {
            viewBind.tvScreen.setText(response.toString());
        }
    }

    @Override
    public void transactionCallback(ChainResponse<TransactionCommitted> chainResponse, Throwable throwable) {
        hideLoading();
        if (chainResponse != null) {
            TransactionCommitted committed = chainResponse.getBody();
            if (committed != null) {
                viewBind.tvScreen.setText("transaction_id:==> " + committed.transaction_id);
            } else {
                viewBind.tvScreen.setText(JSON.toJSONString(chainResponse));
            }
        } else {
            viewBind.tvScreen.setText(throwable.getMessage());
        }
    }

    @Override
    public void ramPriceCallback(Response response) {
        if (response.getCode() == Response.SUCCESS_STATE) {
            RamPrice ramPrice = (RamPrice) response.getResultObj();
            viewBind.tvScreen.setText(response.getOriData());
        } else {
            viewBind.tvScreen.setText(response.toString());
        }
    }

    @Override
    public void currencyBalanceCallback(Response<String> response) {
        if (response.getCode() == Response.SUCCESS_STATE) {
            String balance = response.getResultObj();
            viewBind.tvScreen.setText(response.getOriData());
        } else {
            viewBind.tvScreen.setText(response.toString());
        }
    }

    @Override
    public void currencyListBalanceCallback(List<CurrencyInfo> currencyInfos) {
        if (currencyInfos != null) {
            viewBind.tvScreen.setText(currencyInfos.toString());
        }
    }

    @Override
    public void requestContractCallback(Response<AbiContract> response) {
        if (response.getCode() == Response.SUCCESS_STATE) {
            AbiContract abiContract = response.getResultObj();
            viewBind.tvScreen.setText(response.getOriData());
        } else {
            viewBind.tvScreen.setText(response.toString());
        }
    }

    @Override
    public void requestVoteListCallback(Response<ProducerInfo> response) {
        if (response.getCode() == Response.SUCCESS_STATE) {
            ProducerInfo producerInfo = response.getResultObj();
            viewBind.tvScreen.setText(response.getOriData());
        } else {
            viewBind.tvScreen.setText(response.toString());
        }
    }
}
