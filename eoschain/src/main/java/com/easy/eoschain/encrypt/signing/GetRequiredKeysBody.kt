
package com.easy.eoschain.encrypt.signing

import org.bitcoinj.core.Transaction


data class GetRequiredKeysBody(
        val transaction: Transaction,
        val available_keys: List<String>
)