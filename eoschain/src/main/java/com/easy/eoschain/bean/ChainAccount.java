package com.easy.eoschain.bean;

import com.easy.store.bean.eoschain.CpuLimit;
import com.easy.store.bean.eoschain.NetLimit;
import com.easy.store.bean.eoschain.Permissions;
import com.easy.store.bean.eoschain.RefundRequest;
import com.easy.store.bean.eoschain.SelfDelegatedBandwidthBean;
import com.easy.store.bean.eoschain.TotalResources;
import com.easy.store.bean.eoschain.VoterInfo;

import java.util.List;

public class ChainAccount {

    /**
     * account_name : johntrump121
     * head_block_num : 61480248
     * head_block_time : 2019-12-05T02:02:28.500
     * privileged : false
     * last_code_update : 1970-01-01T00:00:00.000
     * created : 2019-12-04T08:38:18.500
     * ram_quota : 5494
     * net_weight : 100
     * cpu_weight : 100
     * net_limit : {"used":0,"available":24185,"max":24185}
     * cpu_limit : {"used":0,"available":4500,"max":4500}
     * ram_usage : 3454
     * permissions : [{"perm_name":"active","parent":"owner","required_auth":{"threshold":1,"keys":[{"key":"EOS7ukaArHrh4uLVXuauRC1Mjr2zbu1QTv7WQrFEV8rzbaMvv5aPd","weight":1}],"accounts":[],"waits":[]}},{"perm_name":"owner","parent":"","required_auth":{"threshold":1,"keys":[{"key":"EOS7ukaArHrh4uLVXuauRC1Mjr2zbu1QTv7WQrFEV8rzbaMvv5aPd","weight":1}],"accounts":[],"waits":[]}}]
     * total_resources : {"owner":"johntrump121","net_weight":"0.0100 TLOS","cpu_weight":"0.0100 TLOS","ram_bytes":4094}
     * self_delegated_bandwidth : {"from":"johntrump121","to":"johntrump121","net_weight":"0.0100 TLOS","cpu_weight":"0.0100 TLOS"}
     * refund_request : null
     * voter_info : {"owner":"johntrump121","proxy":"","producers":[],"staked":200,"last_stake":0,"last_vote_weight":"0.00000000000000000","proxied_vote_weight":"0.00000000000000000","is_proxy":0,"flags1":0,"reserved2":0,"reserved3":"0 "}
     */

    private String account_name;
    private int head_block_num;
    private String head_block_time;
    private boolean privileged;
    private String last_code_update;
    private String core_liquid_balance;
    private String created;
    private int ram_quota;
    private int net_weight;
    private int cpu_weight;
    private NetLimit net_limit;
    private CpuLimit cpu_limit;
    private int ram_usage;
    private TotalResources total_resources;
    private SelfDelegatedBandwidthBean self_delegated_bandwidth;
    private RefundRequest refund_request;
    private VoterInfo voter_info;
    private List<Permissions> permissions;

    public String getCore_liquid_balance() {
        return core_liquid_balance;
    }

    public void setCore_liquid_balance(String core_liquid_balance) {
        this.core_liquid_balance = core_liquid_balance;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public int getHead_block_num() {
        return head_block_num;
    }

    public void setHead_block_num(int head_block_num) {
        this.head_block_num = head_block_num;
    }

    public String getHead_block_time() {
        return head_block_time;
    }

    public void setHead_block_time(String head_block_time) {
        this.head_block_time = head_block_time;
    }

    public boolean isPrivileged() {
        return privileged;
    }

    public void setPrivileged(boolean privileged) {
        this.privileged = privileged;
    }

    public String getLast_code_update() {
        return last_code_update;
    }

    public void setLast_code_update(String last_code_update) {
        this.last_code_update = last_code_update;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getRam_quota() {
        return ram_quota;
    }

    public void setRam_quota(int ram_quota) {
        this.ram_quota = ram_quota;
    }

    public int getNet_weight() {
        return net_weight;
    }

    public void setNet_weight(int net_weight) {
        this.net_weight = net_weight;
    }

    public int getCpu_weight() {
        return cpu_weight;
    }

    public void setCpu_weight(int cpu_weight) {
        this.cpu_weight = cpu_weight;
    }

    public NetLimit getNet_limit() {
        return net_limit;
    }

    public void setNet_limit(NetLimit net_limit) {
        this.net_limit = net_limit;
    }

    public CpuLimit getCpu_limit() {
        return cpu_limit;
    }

    public void setCpu_limit(CpuLimit cpu_limit) {
        this.cpu_limit = cpu_limit;
    }

    public int getRam_usage() {
        return ram_usage;
    }

    public void setRam_usage(int ram_usage) {
        this.ram_usage = ram_usage;
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

    public RefundRequest getRefund_request() {
        return refund_request;
    }

    public void setRefund_request(RefundRequest refund_request) {
        this.refund_request = refund_request;
    }

    public VoterInfo getVoter_info() {
        return voter_info;
    }

    public void setVoter_info(VoterInfo voter_info) {
        this.voter_info = voter_info;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "ChainAccount{" +
                "account_name='" + account_name + '\'' +
                ", head_block_num=" + head_block_num +
                ", head_block_time='" + head_block_time + '\'' +
                ", privileged=" + privileged +
                ", last_code_update='" + last_code_update + '\'' +
                ", created='" + created + '\'' +
                ", ram_quota=" + ram_quota +
                ", net_weight=" + net_weight +
                ", cpu_weight=" + cpu_weight +
                ", net_limit=" + net_limit +
                ", cpu_limit=" + cpu_limit +
                ", ram_usage=" + ram_usage +
                ", total_resources=" + total_resources +
                ", self_delegated_bandwidth=" + self_delegated_bandwidth +
                ", refund_request=" + refund_request +
                ", voter_info=" + voter_info +
                ", permissions=" + permissions +
                '}';
    }
}
