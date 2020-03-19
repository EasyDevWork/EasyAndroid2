package com.easy.eoschain.encrypt.abi

import java.util.*

class TransactionAbi(
    val expiration: Date,
    val ref_block_num: Int,
    val ref_block_prefix: Long,
    val max_net_usage_words: Long,
    val max_cpu_usage_ms: Long,
    val delay_sec: Long,
    val context_free_actions: List<ActionAbi>,
    val actions: List<ActionAbi>,
    val transaction_extensions: List<String>,
    val signatures: List<String>,
    val context_free_data: List<String>
) {

    val getExpiration: Long
        get() = expiration.time

    val getRefBlockNum: Int
        get() = ref_block_num

    val getRefBlockPrefix: Long
        get() = ref_block_prefix

    val getMaxNetUsageWords: Long
        get() = max_net_usage_words

    val getMaxCpuUsageMs: Long
        get() = max_cpu_usage_ms

    val getDelaySec: Long
        get() = delay_sec

    val getContextFreeActions: List<ActionAbi>
        get() = context_free_actions

    val getActions: List<ActionAbi>
        get() = actions

    val getTransactionExtensions: List<String>
        get() = transaction_extensions
}