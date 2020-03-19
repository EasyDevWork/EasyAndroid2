
package com.easy.eoschain.bean.response

data class TransactionProcessed(
    val id: String,
    val receipt: TransactionParentReceipt,
    val elapsed: Int,
    val net_usage: Int,
    val scheduled: Boolean,
    val action_traces: List<TransactionActionTrace>,
    val except: Any?,
    val block_num: Int?

)