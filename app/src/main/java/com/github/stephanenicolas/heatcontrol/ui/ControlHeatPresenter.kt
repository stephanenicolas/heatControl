package com.github.stephanenicolas.heatcontrol.ui

import com.github.stephanenicolas.heatcontrol.network.BaseUrlProvider
import com.github.stephanenicolas.heatcontrol.network.HeatControlApi
import com.github.stephanenicolas.heatcontrol.network.THE_KEY
import com.github.stephanenicolas.heatcontrol.network.ToStringConverterFactory
import com.github.stephanenicolas.heatcontrol.usecases.ControlHeatStateStore
import com.github.stephanenicolas.heatcontrol.usecases.RefreshAmbientTemperatureAction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Inject


class ControlHeatPresenter @Inject constructor(baseUrlProvider: BaseUrlProvider, val controlHeatStore: ControlHeatStateStore) {

    var view: ControlHeatView? = null
    private val heatControlApi: HeatControlApi
    private val subscriptions: CompositeDisposable = CompositeDisposable()

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrlProvider.getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ToStringConverterFactory())
                .build()
        heatControlApi = retrofit.create(HeatControlApi::class.java)
    }

    fun attach(view: ControlHeatView) {
        this.view = view
        view.setControlHeatState(controlHeatStore.getState())
        val disposable = controlHeatStore.getStateObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::setControlHeatState)
        subscriptions.add(disposable)
    }

    fun detach() {
        subscriptions.dispose()
        this.view = null
    }

    fun refresh() {
        val disposable = heatControlApi.getAmbientTemperature(THE_KEY)
                .subscribeOn(Schedulers.io())
                .subscribe(this::onSuccessGetTargetTemp)
        subscriptions.add(disposable)
    }

    fun onSuccessGetTargetTemp(tempString: String) {
        val action = RefreshAmbientTemperatureAction(tempString.toFloat())
        controlHeatStore.dispatch(action)
    }
}
