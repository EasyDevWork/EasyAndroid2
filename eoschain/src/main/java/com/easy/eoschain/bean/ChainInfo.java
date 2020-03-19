package com.easy.eoschain.bean;

public class ChainInfo {

    /**
     * server_version : a2317d38
     * chain_id : aca376f206b8fc25a6ed44dbdc66547c36c6c33e3a119ffbeaef943642f0e906
     * head_block_num : 93223994
     * last_irreversible_block_num : 93223670
     * last_irreversible_block_id : 058e7af6f484428404c1f4a70cac1cbe46a9055e11391d2e2811b505a74b8b12
     * head_block_id : 058e7c3ae94391483c273efd0a808943c0d576306e0bd2957ccacf3c018828d2
     * head_block_time : 2019-12-04T09:57:23.500
     * head_block_producer : eosflytomars
     * virtual_block_cpu_limit : 200000
     * virtual_block_net_limit : 1048576000
     * block_cpu_limit : 180
     * block_net_limit : 1010488
     * server_version_string : v1.8.5
     * fork_db_head_block_num : 93223994
     * fork_db_head_block_id : 058e7c3ae94391483c273efd0a808943c0d576306e0bd2957ccacf3c018828d2
     */

    private String server_version;
    private String chain_id;
    private int head_block_num;
    private int last_irreversible_block_num;
    private String last_irreversible_block_id;
    private String head_block_id;
    private String head_block_time;
    private String head_block_producer;
    private int virtual_block_cpu_limit;
    private int virtual_block_net_limit;
    private int block_cpu_limit;
    private int block_net_limit;
    private String server_version_string;
    private int fork_db_head_block_num;
    private String fork_db_head_block_id;

    public String getServer_version() {
        return server_version;
    }

    public void setServer_version(String server_version) {
        this.server_version = server_version;
    }

    public String getChain_id() {
        return chain_id;
    }

    public void setChain_id(String chain_id) {
        this.chain_id = chain_id;
    }

    public int getHead_block_num() {
        return head_block_num;
    }

    public void setHead_block_num(int head_block_num) {
        this.head_block_num = head_block_num;
    }

    public int getLast_irreversible_block_num() {
        return last_irreversible_block_num;
    }

    public void setLast_irreversible_block_num(int last_irreversible_block_num) {
        this.last_irreversible_block_num = last_irreversible_block_num;
    }

    public String getLast_irreversible_block_id() {
        return last_irreversible_block_id;
    }

    public void setLast_irreversible_block_id(String last_irreversible_block_id) {
        this.last_irreversible_block_id = last_irreversible_block_id;
    }

    public String getHead_block_id() {
        return head_block_id;
    }

    public void setHead_block_id(String head_block_id) {
        this.head_block_id = head_block_id;
    }

    public String getHead_block_time() {
        return head_block_time;
    }

    public void setHead_block_time(String head_block_time) {
        this.head_block_time = head_block_time;
    }

    public String getHead_block_producer() {
        return head_block_producer;
    }

    public void setHead_block_producer(String head_block_producer) {
        this.head_block_producer = head_block_producer;
    }

    public int getVirtual_block_cpu_limit() {
        return virtual_block_cpu_limit;
    }

    public void setVirtual_block_cpu_limit(int virtual_block_cpu_limit) {
        this.virtual_block_cpu_limit = virtual_block_cpu_limit;
    }

    public int getVirtual_block_net_limit() {
        return virtual_block_net_limit;
    }

    public void setVirtual_block_net_limit(int virtual_block_net_limit) {
        this.virtual_block_net_limit = virtual_block_net_limit;
    }

    public int getBlock_cpu_limit() {
        return block_cpu_limit;
    }

    public void setBlock_cpu_limit(int block_cpu_limit) {
        this.block_cpu_limit = block_cpu_limit;
    }

    public int getBlock_net_limit() {
        return block_net_limit;
    }

    public void setBlock_net_limit(int block_net_limit) {
        this.block_net_limit = block_net_limit;
    }

    public String getServer_version_string() {
        return server_version_string;
    }

    public void setServer_version_string(String server_version_string) {
        this.server_version_string = server_version_string;
    }

    public int getFork_db_head_block_num() {
        return fork_db_head_block_num;
    }

    public void setFork_db_head_block_num(int fork_db_head_block_num) {
        this.fork_db_head_block_num = fork_db_head_block_num;
    }

    public String getFork_db_head_block_id() {
        return fork_db_head_block_id;
    }

    public void setFork_db_head_block_id(String fork_db_head_block_id) {
        this.fork_db_head_block_id = fork_db_head_block_id;
    }

    @Override
    public String toString() {
        return "ChainInfo{" +
                "server_version='" + server_version + '\'' +
                ", chain_id='" + chain_id + '\'' +
                ", head_block_num=" + head_block_num +
                ", last_irreversible_block_num=" + last_irreversible_block_num +
                ", last_irreversible_block_id='" + last_irreversible_block_id + '\'' +
                ", head_block_id='" + head_block_id + '\'' +
                ", head_block_time='" + head_block_time + '\'' +
                ", head_block_producer='" + head_block_producer + '\'' +
                ", virtual_block_cpu_limit=" + virtual_block_cpu_limit +
                ", virtual_block_net_limit=" + virtual_block_net_limit +
                ", block_cpu_limit=" + block_cpu_limit +
                ", block_net_limit=" + block_net_limit +
                ", server_version_string='" + server_version_string + '\'' +
                ", fork_db_head_block_num=" + fork_db_head_block_num +
                ", fork_db_head_block_id='" + fork_db_head_block_id + '\'' +
                '}';
    }
}
