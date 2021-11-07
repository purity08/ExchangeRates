package com.exchangerates.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppServices {
    private const val BASE_URL = "https://www.nbrb.by/api/exrates/"

    val exchangesService: ExchangesService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangesService::class.java)
    }
}