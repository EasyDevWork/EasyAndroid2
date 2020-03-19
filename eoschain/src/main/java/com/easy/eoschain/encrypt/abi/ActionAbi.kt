package com.easy.eoschain.encrypt.abi

data class ActionAbi(
        var account: String,
        var name: String,
        var authorization: List<TransactionAuthorizationAbi>,
        var data: String?
)
