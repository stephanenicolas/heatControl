package com.github.stephanenicolas.heatcontrol.usecases

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

@Singleton
class ControlHeatStateStore {

    private val subject: PublishSubject<ControlHeatState> = PublishSubject.create<ControlHeatState>()
    private var state: ControlHeatState = initState()

    private fun initState(): ControlHeatState {
        val newState = ControlHeatState()
        assignState(newState)
        return newState
    }

    private fun assignState(newState: ControlHeatState) {
        this.state = newState
        subject.onNext(newState)
    }

    fun dispatch(action: Action) {
        assignState(reduce(action, state))
    }

    private fun reduce(action: Action, inState: ControlHeatState): ControlHeatState {
        var newState:ControlHeatState?
        when (action) {
            is SetTargetTempAction -> {
                return ControlHeatState(action.temp, inState.ambiantTemp)
            }
            is RefreshAmbientTemperatureAction -> {
                var targetTemp = inState.targetTemp
                if (targetTemp == 0F) {
                    targetTemp = action.temp
                }
                newState = ControlHeatState(targetTemp, action.temp)
            }
            else -> newState = inState
        }
        assignState(newState)
        return newState
    }

    fun getStateObservable(): Observable<ControlHeatState> {
        return subject
    }

    fun getState(): ControlHeatState {
        return state
    }
}


