package com.github.stephanenicolas.heatcontrol.features.control.usecases

import com.github.stephanenicolas.heatcontrol.base.network.*
import com.github.stephanenicolas.heatcontrol.features.control.state.ControlStore
import com.github.stephanenicolas.heatcontrol.features.control.state.ErrorControlAction
import com.github.stephanenicolas.heatcontrol.features.control.state.RefreshTemperaturesControlAction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class ControlController @Inject constructor(val baseUrlProvider: BaseUrlProvider,
                                            val controlHeatStore: ControlStore,
                                            val ketRequestInterceptor: KeyRequestInterceptor,
                                            val ipRequestInterceptor: IPRequestInterceptor) {

    private val subscriptions: CompositeDisposable = CompositeDisposable()

    fun detach() {
        subscriptions.dispose()
    }

    fun refreshTemperatures() {
        val heatControlApi = createRetrofit()
        if (heatControlApi != null) {
            val disposable = heatControlApi.getTargetTemperature()
                    .zipWith(heatControlApi.getAmbientTemperature(), BiFunction<HeatResponse,HeatResponse,Pair<String,String>> { t1, t2 -> combineTemperatures(t1, t2) })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccessGetTemperatures,
                            this::onFailureGetTemp)
            subscriptions.add(disposable)
        }
    }

    fun combineTemperatures(ambientHeatResponse: HeatResponse, targetHeatResponse: HeatResponse): Pair<String, String> {
        return Pair(ambientHeatResponse.data, targetHeatResponse.data)
    }

    fun setTargetTemperature(targetTemp: String) {
        val heatControlApi = createRetrofit()
        if (heatControlApi != null) {
            val disposable = heatControlApi.setTargetTemperature(targetTemp)
                    .flatMap({heatControlApi.getTargetTemperature()})
                    .zipWith(heatControlApi.getAmbientTemperature(), BiFunction<HeatResponse,HeatResponse,Pair<String,String>> { t1, t2 -> combineTemperatures(t1, t2) })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccessGetTemperatures,
                            this::onFailureGetTemp)
            subscriptions.add(disposable)
        }
    }

    private fun onSuccessGetTemperatures(temperaturePair: Pair<String, String>) {
        val action = RefreshTemperaturesControlAction(temperaturePair.first.toFloat(), temperaturePair.second.toFloat())
        controlHeatStore.dispatch(action)
    }

    private fun onFailureGetTemp(throwable: Throwable) {
        val action = ErrorControlAction(throwable)
        controlHeatStore.dispatch(action)
    }

    private fun createRetrofit(): HeatControlApi? {
        if (baseUrlProvider.httpUrl == null) {
            onFailureGetTemp(IllegalStateException("No base url defined"))
            return null
        }

        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.interceptors().add(ketRequestInterceptor)
        clientBuilder.interceptors().add(ipRequestInterceptor)

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrlProvider.httpUrl)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(HeatControlApi::class.java)
    }
}
