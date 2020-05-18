package com.easy.eoschain.base


import com.easy.eoschain.bean.CurrencyInfo
import com.easy.eoschain.bean.response.TransactionCommitted
import com.easy.eoschain.encrypt.info.Info
import com.easy.eoschain.encrypt.signing.PushTransaction
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChainApi {

    @Headers("host_group:EosHost")
    @GET("v1/chain/get_info")
    fun getChainInfo(): Single<Response<Info>>

    @Headers("host_group:EosHost")
    @POST("v1/chain/push_transaction")
    fun pushTransaction(@Body body: PushTransaction): Single<Response<TransactionCommitted>>

    @Headers("host_group:EosHost")
    @POST("v1/chain/get_currency_balance")
    fun getCurrencyBalance(@Body body: CurrencyInfo): Single<Response<List<String>>>

    //    @POST("v1/chain/get_producers")
//    fun getProducers(@Body body: GetProducers): Single<Response<ProducerList>>
//
//    @POST("v1/chain/get_account")
//    fun getAccount(@Body body: AccountName): Single<Response<Account>>
//
//    @POST("v1/chain/get_abi")
//    fun getAbi(@Body body: AccountName): Single<Response<AbiForAccount>>
//
//    @POST("v1/chain/get_code")
//    fun getCode(@Body body: GetCodeByAccountName): Single<Response<CodeForAccount>>
//
//    @POST("v1/chain/get_table_rows")
//    fun getTableRows(@Body body: GetTableRows): Single<Response<ContractTableRows>>
//
//    @POST("v1/chain/get_table_rows")
//    fun getRamTableRows(@Body body: GetTableRows): Single<Response<RamTableRows>>
//
//    @POST("v1/chain/get_table_rows")
//    fun getTableRowsRex(@Body body: GetTableRows): Single<Response<TableRows>>
//
//    @POST("v1/chain/get_table_rows")
//    fun getTableRowsRexpool(@Body body: GetTableRows): Single<Response<TableRowsRexPool>>
//
//    @POST("v1/chain/get_table_rows")
//    fun getTableRowsRexbal(@Body body: GetTableRows): Single<Response<TableRowsRexbal>>
//


//    @POST("v1/chain/abi_json_to_bin")
//    fun abiJsonToBin(@Body body: RequestBody): Single<Response<BinaryHex>>
//
//    @POST("v1/chain/abi_bin_to_json")
//    fun abiBinToJson(@Body body: AbiBinToJson): Single<Response<ResponseBody>>
//

//
//    @GET("https://more.ethte.com/collect/cpupayinfo")
//    fun cpuPayInfo(@Query("account") account: String): Observable<Response<CpuPayInfoResult>>
//
//    @POST("https://more.ethte.com/collect/addcpuorder")
//    fun addcpuorder2(@Header("lang") lang: String, @Body body: CpuRequest): Single<Response<SignaturesResult>>
}