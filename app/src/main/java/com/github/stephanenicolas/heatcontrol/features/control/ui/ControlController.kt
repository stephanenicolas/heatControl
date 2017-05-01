package com.github.stephanenicolas.heatcontrol.features.control.ui

import android.util.Log
import com.github.stephanenicolas.heatcontrol.network.BaseUrlProvider
import com.github.stephanenicolas.heatcontrol.network.HeatResponse
import com.github.stephanenicolas.heatcontrol.network.HeatControlApi
import com.github.stephanenicolas.heatcontrol.network.THE_KEY
import com.github.stephanenicolas.heatcontrol.features.control.usecases.ControlStore
import com.github.stephanenicolas.heatcontrol.features.control.usecases.ErrorControlAction
import com.github.stephanenicolas.heatcontrol.features.control.usecases.RefreshTemperaturesControlAction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class ControlController @Inject constructor(baseUrlProvider: BaseUrlProvider, val controlHeatStore: ControlStore) {

    private val heatControlApi: HeatControlApi
    private val subscriptions: CompositeDisposable = CompositeDisposable()

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrlProvider.getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        heatControlApi = retrofit.create(HeatControlApi::class.java)
    }

    fun detach() {
        subscriptions.dispose()
    }

    fun refreshTemperatures() {
        val disposable = heatControlApi.getAmbientTemperature(THE_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessGetTargetTemp,
                        this::onFailureGetTargetTemp)
        subscriptions.add(disposable)
    }

    fun setTargetTemperature(targetTemp: String) {
        val disposable = heatControlApi.setTargetTemperature(targetTemp, THE_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessGetTargetTemp,
                        this::onFailureGetTargetTemp)
        subscriptions.add(disposable)
    }

    private fun onSuccessGetTargetTemp(heatResponse: HeatResponse) {
        val action = RefreshTemperaturesControlAction(heatResponse.ambientTemp.toFloat(), heatResponse.targetTemp.toFloat())
        controlHeatStore.dispatch(action)
    }

    private fun onFailureGetTargetTemp(throwable: Throwable) {
        val action = ErrorControlAction(throwable)
        controlHeatStore.dispatch(action)
    }
}
