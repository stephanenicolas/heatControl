package com.github.stephanenicolas.heatcontrol.usecases

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ControlHeatStateStore {

    private var state: ControlHeatState = initState()

    private fun initState(): ControlHeatState {
        return ControlHeatState()
    }

    public fun reduce(action: Action, inState: ControlHeatState): ControlHeatState {
        when( action ) {
            is SetTargetTempAction ->  {
                return ControlHeatState(action.temp, inState.ambiantTemp)
            }
            is GetTargetTempAction ->  {
                return ControlHeatState(inState.targetTemp, inState.ambiantTemp)
            }
            is GetAmbientAction ->  {
                return ControlHeatState(inState.targetTemp, inState.ambiantTemp)
            }
        }
        return inState
    }

    fun getState(): ControlHeatState {
        return state
    }
}


