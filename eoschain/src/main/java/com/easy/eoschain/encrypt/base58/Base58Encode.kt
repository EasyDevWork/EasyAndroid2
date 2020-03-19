
package com.easy.eoschain.encrypt.base58


import com.easy.eoschain.encrypt.hash.RIPEMD160Digest
import org.bitcoinj.core.Base58

class Base58Encode {

    fun encodeSignature(prefix: String, data: ByteArray): String {
        return encodeWithChecksum(prefix, "K1", data)
    }

    fun encodeKey(prefix: String, data: ByteArray): String {
        return encodeWithChecksum(prefix, "", data)
    }

    private fun encodeWithChecksum(prefix: String, signaturePrefix: String, data: ByteArray): String {

        val dataWithChecksum = ByteArray(data.size + 4)

        System.arraycopy(data, 0, dataWithChecksum, 0, data.size)
        System.arraycopy(encodeChecksum(data, signaturePrefix), 0, dataWithChecksum, data.size, 4)

        return if (signaturePrefix.isEmpty()) {
            prefix + Base58.encode(dataWithChecksum)
        } else {
            prefix + "_" + signaturePrefix + "_" + Base58.encode(dataWithChecksum)
        }
    }

    private fun encodeChecksum(data: ByteArray, vararg extras: String): ByteArray {

        val toHashData = ByteArray(data.size + extras.sumBy { it.length })

        System.arraycopy(data, 0, toHashData, 0, data.size)

        extras.filter { extra ->
            extra.isNotEmpty()
        }.forEach { extra ->
            System.arraycopy(extra.toByteArray(), 0, toHashData, data.size, extra.length)
        }

        return RIPEMD160Digest.hash(toHashData)
    }
}