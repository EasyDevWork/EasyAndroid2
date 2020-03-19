package com.easy.eoschain.encrypt.hash

import org.spongycastle.crypto.digests.RIPEMD160Digest;
class RIPEMD160Digest {

    companion object {
        fun hash(data: ByteArray): ByteArray {
            val digest = RIPEMD160Digest()
            digest.update(data, 0, data.size)
            val out = ByteArray(20)
            digest.doFinal(out, 0)
            return out
        }
    }
}