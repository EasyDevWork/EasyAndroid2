package com.easy.store.bean;

import com.easy.store.bean.eoschain.CpuLimit;
import com.easy.store.bean.eoschain.Eos2UsdtPrice;
import com.easy.store.bean.eoschain.NetLimit;
import com.easy.store.bean.eoschain.Permissions;
import com.easy.store.bean.eoschain.RamPrice;
import com.easy.store.bean.eoschain.RefundRequest;
import com.easy.store.bean.eoschain.RexFund;
import com.easy.store.bean.eoschain.RexPrice;
import com.easy.store.bean.eoschain.SelfDelegatedBandwidthBean;
import com.easy.store.bean.eoschain.Token;
import com.easy.store.bean.eoschain.TokenPrice;
import com.easy.store.bean.eoschain.TotalResources;
import com.easy.store.bean.eoschain.VoterInfo;
import com.easy.store.converter.eoschain.CpuLimitConverter;
import com.easy.store.converter.eoschain.Eos2UsdtConverter;
import com.easy.store.converter.eoschain.NetLimitConverter;
import com.easy.store.converter.eoschain.PermissionsConverter;
import com.easy.store.converter.eoschain.RamPriceConverter;
import com.easy.store.converter.eoschain.RefundRequestConverter;
import com.easy.store.bean.eoschain.RexBean;
import com.easy.store.converter.eoschain.RexBeanConverter;
import com.easy.store.converter.eoschain.RexFundConverter;
import com.easy.store.converter.eoschain.RexPriceConverter;
import com.easy.store.converter.eoschain.SelfDelegatedBandwidthConverter;
import com.easy.store.bean.eoschain.StakeBean;
import com.easy.store.converter.eoschain.StakeBeanConverter;
import com.easy.store.converter.eoschain.TokenConverter;
import com.easy.store.converter.eoschain.TokenPriceConverter;
import com.easy.store.converter.eoschain.TotalResourcesConverter;
import com.easy.store.converter.eoschain.VoterInfoConverter;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class EosAccount {
    @Id
    private long id;
    private String netType;
    private String name;
    private String privateKey;
    private String publicKey;

    private String ramQuota;//内存总量 ram_quota/1024
    private String ramUsage;//内存已用 ram_usage/1024；
    private String coreLiquidBalance;

    @Convert(converter = PermissionsConverter.class, dbType = String.class)
    private List<Permissions> ownPermissions;//私钥所拥有的权限
    @Convert(converter = NetLimitConverter.class, dbType = String.class)
    private NetLimit netLimit;
    @Convert(converter = CpuLimitConverter.class, dbType = String.class)
    private CpuLimit cpuLimit;
    @Convert(converter = PermissionsConverter.class, dbType = String.class)
    private List<Permissions> permissions;
    @Convert(converter = VoterInfoConverter.class, dbType = String.class)
    private VoterInfo voterInfo;
    @Convert(converter = RefundRequestConverter.class, dbType = String.class)
    private RefundRequest refund_request;
    @Convert(converter = TotalResourcesConverter.class, dbType = String.class)
    private TotalResources total_resources;
    @Convert(converter = SelfDelegatedBandwidthConverter.class, dbType = String.class)
    private SelfDelegatedBandwidthBean self_delegated_bandwidth;
    @Convert(converter = StakeBeanConverter.class, dbType = String.class)
    private StakeBean stakeBean;
    @Convert(converter = RamPriceConverter.class, dbType = String.class)
    private RamPrice ramPrice;
    @Convert(converter = RexBeanConverter.class, dbType = String.class)
    private RexBean rexBean;
    @Convert(converter = RexPriceConverter.class, dbType = String.class)
    private RexPrice rexPrice;
    @Convert(converter = RexFundConverter.class, dbType = String.class)
    RexFund rexFund;
    @Convert(converter = TokenConverter.class, dbType = String.class)
    List<Token> tokenList;
    @Convert(converter = TokenPriceConverter.class, dbType = String.class)
    List<TokenPrice> tokenPriceList;
    @Convert(converter = Eos2UsdtConverter.class, dbType = String.class)
    Eos2UsdtPrice usdtPrice;

    public List<Permissions> getOwnPermissions() {
        return ownPermissions;
    }

    public void setOwnPermissions(List<Permissions> ownPermissions) {
        this.ownPermissions = ownPermissions;
    }

    public Eos2UsdtPrice getUsdtPrice() {
        return usdtPrice;
    }

    public void setUsdtPrice(Eos2UsdtPrice usdtPrice) {
        this.usdtPrice = usdtPrice;
    }

    public List<TokenPrice> getTokenPriceList() {
        return tokenPriceList;
    }

    public void setTokenPriceList(List<TokenPrice> tokenPriceList) {
        this.tokenPriceList = tokenPriceList;
    }

    public List<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public RexFund getRexFund() {
        return rexFund;
    }

    public void setRexFund(RexFund rexFund) {
        this.rexFund = rexFund;
    }

    public RexPrice getRexPrice() {
        return rexPrice;
    }

    public void setRexPrice(RexPrice rexPrice) {
        this.rexPrice = rexPrice;
    }

    public RamPrice getRamPrice() {
        return ramPrice;
    }

    public void setRamPrice(RamPrice ramPrice) {
        this.ramPrice = ramPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getRamQuota() {
        return ramQuota;
    }

    public void setRamQuota(String ramQuota) {
        this.ramQuota = ramQuota;
    }

    public String getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(String ramUsage) {
        this.ramUsage = ramUsage;
    }

    public NetLimit getNetLimit() {
        return netLimit;
    }

    public void setNetLimit(NetLimit netLimit) {
        this.netLimit = netLimit;
    }

    public CpuLimit getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(CpuLimit cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    public VoterInfo getVoterInfo() {
        return voterInfo;
    }

    public void setVoterInfo(VoterInfo voterInfo) {
        this.voterInfo = voterInfo;
    }

    public RefundRequest getRefund_request() {
        return refund_request;
    }

    public void setRefund_request(RefundRequest refund_request) {
        this.refund_request = refund_request;
    }

    public TotalResources getTotal_resources() {
        return total_resources;
    }

    public void setTotal_resources(TotalResources total_resources) {
        this.total_resources = total_resources;
    }

    public SelfDelegatedBandwidthBean getSelf_delegated_bandwidth() {
        return self_delegated_bandwidth;
    }

    public void setSelf_delegated_bandwidth(SelfDelegatedBandwidthBean self_delegated_bandwidth) {
        this.self_delegated_bandwidth = self_delegated_bandwidth;
    }

    public StakeBean getStakeBean() {
        return stakeBean;
    }

    public void setStakeBean(StakeBean stakeBean) {
        this.stakeBean = stakeBean;
    }

    public RexBean getRexBean() {
        return rexBean;
    }

    public void setRexBean(RexBean rexBean) {
        this.rexBean = rexBean;
    }

    public String getCoreLiquidBalance() {
        return coreLiquidBalance;
    }

    public void setCoreLiquidBalance(String coreLiquidBalance) {
        this.coreLiquidBalance = coreLiquidBalance;
    }
}
