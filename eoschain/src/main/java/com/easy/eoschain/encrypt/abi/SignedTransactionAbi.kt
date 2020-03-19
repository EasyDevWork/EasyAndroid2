package com.easy.eoschain.encrypt.abi


data class SignedTransactionAbi(
    val chainId: String,
    val transaction: TransactionAbi,
    val context_free_data: List<String>
) {
    val getChainId: String
        get() = chainId

    val getTransaction: TransactionAbi
        get() = transaction

    val getContextFreeData: List<String>
        get() = context_free_data
}