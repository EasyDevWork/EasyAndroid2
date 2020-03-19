
package com.easy.eoschain.bean.response

data class TransactionAct(
        val account: String,
        val name: String,
        val authorization: List<TransactionAuthorization>,
        val data: Any,
        val hex_data: String?
)