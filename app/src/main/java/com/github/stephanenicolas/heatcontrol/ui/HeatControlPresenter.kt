package com.github.stephanenicolas.heatcontrol.ui

import com.github.stephanenicolas.heatcontrol.usecases.ControlHeatStateStore
import javax.inject.Inject


class HeatControlPresenter {

    @Inject
    lateinit var controlHeatStore: ControlHeatStateStore

    @Inject
    constructor() {

    }

}
