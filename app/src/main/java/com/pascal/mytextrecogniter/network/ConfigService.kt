package com.pascal.mytextrecogniter.network

import com.pascal.mytextrecogniter.model.ResponseTranslate
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface ConfigService {

    @GET("translate?")
    fun getTranslate(
        @Query("engine") engine: String,
        @Query("text") text: String,
        @Query("to") to: String
    ): Flowable<ResponseTranslate>
}