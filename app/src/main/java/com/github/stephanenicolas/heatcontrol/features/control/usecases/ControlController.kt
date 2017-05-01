package com.github.stephanenicolas.heatcontrol.features.control.usecases

import com.github.stephanenicolas.heatcontrol.base.network.BaseUrlProvider
import com.github.stephanenicolas.heatcontrol.base.network.HeatControlApi
import com.github.stephanenicolas.heatcontrol.base.network.HeatResponse
import com.github.stephanenicolas.heatcontrol.base.network.THE_KEY
import com.github.stephanenicolas.heatcontrol.features.control.state.ControlStore
import com.github.stephanenicolas.heatcontrol.features.control.state.ErrorControlAction
import com.github.stephanenicolas.heatcontrol.features.control.state.RefreshTemperaturesControlAction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class ControlController @Inject constructor(val baseUrlProvider: BaseUrlProvider,
                                            val controlHeatStore: ControlStore) {

    private val subscriptions: CompositeDisposable = CompositeDisposable()

    fun detach() {
        subscriptions.dispose()
    }

    fun refreshTemperatures() {
        val heatControlApi = createRetrofit()
        if (heatControlApi != null) {
            val disposable = heatControlApi.getAmbientTemperature(THE_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccessGetTargetTemp,
                            this::onFailureGetTargetTemp)
            subscriptions.add(disposable)
        }
    }

    fun setTargetTemperature(targetTemp: String) {
        val heatControlApi = createRetrofit()
        if (heatControlApi != null) {
            val disposable = heatControlApi.setTargetTemperature(targetTemp, THE_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccessGetTargetTemp,
                            this::onFailureGetTargetTemp)
            subscriptions.add(disposable)
        }
    }

    private fun onSuccessGetTargetTemp(heatResponse: HeatResponse) {
        val action = RefreshTemperaturesControlAction(heatResponse.ambientTemp.toFloat(), heatResponse.targetTemp.toFloat())
        controlHeatStore.dispatch(action)
    }

    private fun onFailureGetTargetTemp(throwable: Throwable) {
        val action = ErrorControlAction(throwable)
        controlHeatStore.dispatch(action)
    }

    private fun createRetrofit(): HeatControlApi? {
        if (baseUrlProvider.httpUrl == null) {
            onFailureGetTargetTemp(IllegalStateException("No base url defined"))
            return null
        }

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrlProvider.httpUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(HeatControlApi::class.java)
    }
}
