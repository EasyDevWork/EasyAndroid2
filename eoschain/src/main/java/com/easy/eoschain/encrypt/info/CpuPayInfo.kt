package com.easy.eoschain.encrypt.info

data class CpuPayInfo(
        val cpu_account: String,
        val cpu_access: String,
        val available: Boolean,
        val block_id: String,
        val url: String,
        val open: Boolean
)