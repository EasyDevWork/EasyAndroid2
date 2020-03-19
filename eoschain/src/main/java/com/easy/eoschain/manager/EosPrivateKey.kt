package com.easy.eoschain.manager

import com.easy.eoschain.encrypt.crypto.signature.SecP256K1KeyCurve
import one.block.eosiojava.error.utilities.Base58ManipulationError
import org.bitcoinj.core.Base58
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import java.math.BigInteger
import java.util.*

class EosPrivateKey internal constructor(
        private val key: ECKey,
        private val base58: String = base58Encode(key)
) {

    val publicKey: EosPublicKey = EosPublicKey(key.pubKey)
    val keyCurve: SecP256K1KeyCurve = SecP256K1KeyCurve()

    val bytes: ByteArray
        get() = key.privKeyBytes

    val bigInteger: BigInteger
        get() = key.privKey

    constructor() : this(ECKey())

    constructor(base58: String) : this(getBase58Bytes(base58))

    constructor(bytes: ByteArray) : this(ECKey.fromPrivate(bytes))

    override fun toString(): String = base58

    companion object {
        private fun getBase58Bytes(strKey: String): ByteArray {
            if (strKey.isEmpty()) {
                throw IllegalArgumentException("Input key to decode can't be empty!")
            } else {
                try {
                    val base58Decoded = Base58.decode(strKey)
                    val firstCheckSum = Arrays.copyOfRange(base58Decoded, base58Decoded.size - 4, base58Decoded.size)
                    var decodedKey = Arrays.copyOfRange(base58Decoded, 0, base58Decoded.size - 4)
                    if (invalidSha256x2CheckSum(decodedKey, firstCheckSum)) {
                        throw IllegalArgumentException("Input key has invalid checksum!")
                    }
                    if (decodedKey.size > 32) {
                        decodedKey = Arrays.copyOfRange(decodedKey, 1, decodedKey.size)
                        if (decodedKey.size > 32 && decodedKey[32] == Integer.valueOf(1).toByte()) {
                            decodedKey = Arrays.copyOfRange(decodedKey, 0, decodedKey.size - 1)
                        }
                    }
                    return decodedKey
                } catch (var7: Exception) {
                    throw Base58ManipulationError("An error occurred while Base58 decoding the EOS key!", var7)
                }
            }
        }

        private fun invalidSha256x2CheckSum(inputKey: ByteArray, checkSumToValidate: ByteArray): Boolean {
            if (inputKey.size != 0 && checkSumToValidate.size != 0) {
                val sha256x2 = Sha256Hash.hashTwice(inputKey)
                val checkSumFromInputKey = Arrays.copyOfRange(sha256x2, 0, 4)
                return !Arrays.equals(checkSumToValidate, checkSumFromInputKey)
            } else {
                throw IllegalArgumentException("Input key, checksum and key type to validate can't be empty!")
            }
        }

        private fun base58Encode(key: ECKey): String {
            val privateKeyBytes = key.privKeyBytes
            val resultWIFBytes = ByteArray(1 + 32 + 4)
            resultWIFBytes[0] = 0x80.toByte()
            System.arraycopy(privateKeyBytes, if (privateKeyBytes.size > 32) 1 else 0, resultWIFBytes, 1, 32)
            val hash = Sha256Hash.hashTwice(resultWIFBytes, 0, 33)
            System.arraycopy(hash, 0, resultWIFBytes, 33, 4)
            return Base58.encode(resultWIFBytes)
        }
    }
}