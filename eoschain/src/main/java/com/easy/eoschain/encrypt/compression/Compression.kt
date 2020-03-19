
package com.easy.eoschain.encrypt.compression

interface Compression {

    fun compress(uncompressedBytes: ByteArray): ByteArray

    fun decompress(compressedBytes: ByteArray): ByteArray
}