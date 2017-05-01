package com.github.stephanenicolas.heatcontrol.features.control.usecases

@javax.inject.Singleton
class ControlStore {

    private val subject: io.reactivex.subjects.PublishSubject<ControlState> = io.reactivex.subjects.PublishSubject.create<ControlState>()
    private var state: ControlState = initState()

    private fun initState(): ControlState {
        val newState = ControlState()
        assignState(newState)
        return newState
    }

    private fun assignState(newState: ControlState) {
        this.state = newState
        subject.onNext(newState)
    }

    fun dispatch(controlAction: ControlAction) {
        assignState(reduce(controlAction, state))
    }

    private fun reduce(controlAction: ControlAction, inState: ControlState): ControlState {
        var newState: ControlState?
        when (controlAction) {
            is SetTargetTempControlAction -> {
                return ControlState(inState.ambiantTemp, controlAction.targetTemp)
            }
            is RefreshTemperaturesControlAction -> {
                newState = ControlState(controlAction.ambientTemp, controlAction.targetTemp)
            }
            is ErrorControlAction -> {
                newState = ControlState(throwable = controlAction.throwable)
            }
            else -> newState = inState
        }
        assignState(newState)
        return newState
    }

    fun getStateObservable(): io.reactivex.Observable<ControlState> {
        return subject
    }

    fun getState(): ControlState {
        return state
    }
}


