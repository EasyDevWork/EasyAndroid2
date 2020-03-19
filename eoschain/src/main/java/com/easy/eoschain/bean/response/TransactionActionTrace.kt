
package com.easy.eoschain.bean.response


data class TransactionActionTrace(
    val receipt: TransactionReceipt,
    val act: TransactionAct,
    val elapsed: Int,
    val cpu_usage: Int = -1,
    val console: String,
    val total_cpu_usage: Int = -1,
    val trx_id: String,
    val inline_traces: List<TransactionActionTrace>
)
