
package com.easy.eoschain.bean.response

data class Action(
        val account: String,
        val name: String,
        val authorization: List<TransactionAuthorization>,
        val data: String?
)