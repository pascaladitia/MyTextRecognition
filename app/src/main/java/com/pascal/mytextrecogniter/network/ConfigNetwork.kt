package com.pascal.mytextrecogniter.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ConfigNetwork {

    companion object {
        fun getNetwork(): ConfigService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://amm-api-translate.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()

            val service = retrofit.create(ConfigService::class.java)
            return service
        }
    }
}