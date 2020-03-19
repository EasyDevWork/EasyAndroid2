
package com.easy.eoschain.encrypt.compression

internal class NoCompression : Compression {

    override fun compress(uncompressedBytes: ByteArray): ByteArray = uncompressedBytes

    override fun decompress(compressedBytes: ByteArray): ByteArray = compressedBytes
}