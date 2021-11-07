package com.exchangerates.service


import com.exchangerates.model.Exchange
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangesService {

    @GET("rates")
    fun getAllExchanges(
        @Query("ondate") date: String,
        @Query("periodicity") periodicity: String = "0")
    : Call<List<Exchange>>
}