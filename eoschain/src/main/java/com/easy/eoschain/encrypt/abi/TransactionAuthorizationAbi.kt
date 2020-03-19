package com.easy.eoschain.encrypt.abi

data class TransactionAuthorizationAbi(
        val actor: String,
        val permission: String
) {

    val getActor: String
        get() = actor

    val getPermission: String
        get() = permission
}