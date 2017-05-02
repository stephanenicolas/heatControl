package com.github.stephanenicolas.heatcontrol.base.network;

import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

interface HeatControlApi {
    @POST("/?action=getAmbiantTemp")
    fun getAmbientTemperature(): Single<HeatResponse>

    @POST("/?action=getTargetTemp")
    fun getTargetTemperature(): Single<HeatResponse>

    @POST("/?action=setTargetTemp")
    fun setTargetTemperature(@Query("param") targetTemp: String): Single<HeatResponse>
}