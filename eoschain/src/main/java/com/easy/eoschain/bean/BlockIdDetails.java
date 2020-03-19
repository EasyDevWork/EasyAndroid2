package com.easy.eoschain.bean;

import com.easy.eoschain.encrypt.crypto.hex.DefaultHexWriter;
import com.easy.eoschain.encrypt.crypto.hex.HexWriter;

import org.bitcoinj.core.Utils;

import java.math.BigInteger;

public class BlockIdDetails {
    private String blockId;
    private HexWriter hexWriter = new DefaultHexWriter();
    private int blockNum;
    private Long blockPrefix;

    public BlockIdDetails(String blockId) {
        this.blockId = blockId;
        blockNum = new BigInteger(1, hexWriter.hexToBytes(blockId.substring(0, 8))).intValue();
        blockPrefix = Utils.readUint32(hexWriter.hexToBytes(blockId.substring(16, 24)), 0);
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public HexWriter getHexWriter() {
        return hexWriter;
    }

    public void setHexWriter(HexWriter hexWriter) {
        this.hexWriter = hexWriter;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public Long getBlockPrefix() {
        return blockPrefix;
    }

    public void setBlockPrefix(Long blockPrefix) {
        this.blockPrefix = blockPrefix;
    }
}
