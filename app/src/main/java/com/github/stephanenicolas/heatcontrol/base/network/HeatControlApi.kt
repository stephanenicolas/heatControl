package com.github.stephanenicolas.heatcontrol.base.network;

import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

const val THE_KEY: String = "thekey"
const val THE_URL: String = "www.raytrex.com"

interface HeatControlApi {
    @POST("/?action=getTemperature")
    fun getAmbientTemperature(@Query("cle") key: String): Single<HeatResponse>

    @POST("/?action=setTemperature")
    fun setTargetTemperature(@Query("temperature") targetTemp: String, @Query("cle") key: String): Single<HeatResponse>
}