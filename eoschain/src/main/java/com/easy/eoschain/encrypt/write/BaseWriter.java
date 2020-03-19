package com.easy.eoschain.encrypt.write;

import com.easy.eoschain.encrypt.compression.CompressionFactory;
import com.easy.eoschain.encrypt.compression.CompressionType;
import com.easy.eoschain.encrypt.crypto.hex.DefaultHexWriter;
import com.easy.eoschain.encrypt.crypto.hex.HexWriter;

public class BaseWriter {
    public final ByteWriter byteWriter;
    public final HexWriter hexWriter;
    public final CompressionType compressionType;

    public BaseWriter() {
        this(CompressionType.NONE);
    }

    public BaseWriter(CompressionType compressionType) {
        this(new DefaultByteWriter(512), new DefaultHexWriter(), compressionType);
    }

    public BaseWriter(ByteWriter byteWriter, HexWriter hexWriter, CompressionType compressionType) {
        this.byteWriter = byteWriter;
        this.hexWriter = hexWriter;
        this.compressionType = compressionType;
    }

    public String toHex() {
        byte[] compressedBytes = this.toBytes();
        return this.hexWriter.bytesToHex(compressedBytes, 0, compressedBytes.length, (String) null);
    }

    public byte[] toBytes() {
        return (new CompressionFactory(this.compressionType)).create().compress(this.byteWriter.toBytes());
    }
}
