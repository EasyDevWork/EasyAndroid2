
package com.easy.eoschain.bean.response


data class TransactionReceipt(
    val receiver: String,
    val act_digest: String,
    val global_sequence: Long,
    val recv_sequence: Long,
    val auth_sequence: List<Any>,
    val code_sequence: Long,
    val abi_sequence: Long
)