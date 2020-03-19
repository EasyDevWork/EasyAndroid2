package com.easy.eoschain.encrypt.signing


data class CpuRequest(
        val account: String,
        val packed_trx: String,
        val signature: String
)