package com.github.stephanenicolas.heatcontrol.ui

import com.github.stephanenicolas.heatcontrol.usecases.ControlHeatStateStore
import javax.inject.Inject


class ControlHeatPresenter {

    var view: ControlHeatView? = null

    @Inject
    lateinit var controlHeatStore: ControlHeatStateStore

    @Inject
    constructor() {

    }

    fun attach(view: ControlHeatView) {
        this.view = view
        val state = controlHeatStore.getState()
        view.setTargetTemp(state.targetTemp)
        view.setAmbientTemp(state.ambiantTemp)
    }

    fun detach() {
        this.view = null
    }

}
