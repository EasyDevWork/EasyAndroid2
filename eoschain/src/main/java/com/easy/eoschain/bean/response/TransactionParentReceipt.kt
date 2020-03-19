
package com.easy.eoschain.bean.response

data class TransactionParentReceipt(
    val status: String,
    val cpu_usage_us: Int,
    val net_usage_words: Int
)