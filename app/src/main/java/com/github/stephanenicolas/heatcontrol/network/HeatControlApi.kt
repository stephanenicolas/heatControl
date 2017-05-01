package com.github.stephanenicolas.heatcontrol.network;

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

const val THE_KEY: String = "thekey"
const val THE_URL: String = "www.raytrex.com"

interface HeatControlApi {
    @GET("/getAmbientTemperature")
    fun getAmbientTemperature(@Query("cle") key: String): Single<String>

    @PUT("/setTargetTemperature")
    fun setTargetTemperature(@Query("cle") key: String): Call<Void>
}