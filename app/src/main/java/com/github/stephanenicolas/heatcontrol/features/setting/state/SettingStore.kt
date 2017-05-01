package com.github.stephanenicolas.heatcontrol.features.control.state

import javax.inject.Singleton

@Singleton
class SettingStore {

    private val subject: io.reactivex.subjects.PublishSubject<SettingState> = io.reactivex.subjects.PublishSubject.create<SettingState>()
    private var state: SettingState = initState()

    private fun initState(): SettingState {
        val newState = SettingState()
        newState.key = "foo"
        addMockHost(newState)
        assignState(newState)
        return newState
    }

    private fun addMockHost(state: SettingState) {
        val mockHost = Host(MOCK, MOCK)
        state.hostList.add(0, mockHost)
    }

    private fun assignState(newState: SettingState) {
        this.state = newState
        subject.onNext(newState)
    }

    fun dispatch(settingAction: SettingAction) {
        assignState(reduce(settingAction, state))
    }

    private fun reduce(settingAction: SettingAction, inState: SettingState): SettingState {
        var newState: SettingState?
        when (settingAction) {
            is SetKeyAction -> {
                val newHostList = ArrayList(inState.hostList)
                newState = SettingState(settingAction.key, newHostList)
            }
            is AddHostAction -> {
                val newHostList = ArrayList(inState.hostList)
                newHostList.add(Host("local","http://localhost:80"))
                newState = SettingState(inState.key, newHostList)
            }
            is SelectHostAction -> {
                val newHostList = ArrayList(inState.hostList)
                for ((index, host) in newHostList.withIndex()) {
                    host.isSelected = index == settingAction.hostIndex
                }
                newState = SettingState(inState.key, newHostList)
            }
            is RenameHostAction -> {
                val newHostList = ArrayList(inState.hostList)
                newHostList.get(settingAction.hostIndex).name = settingAction.name
                newState = SettingState(inState.key, newHostList)
            }
            is SetHostAction -> {
                val newHostList = ArrayList(inState.hostList)
                newHostList.get(settingAction.hostIndex).address = settingAction.address
                newState = SettingState(inState.key, newHostList)
            }
            is DeleteHostAction -> {
                val newHostList = ArrayList(inState.hostList)
                newHostList.removeAt(settingAction.hostIndex)
                newState = SettingState(inState.key, newHostList)
            }
            is ResetAction -> {
                newState = settingAction.settingState
                if( newState == null) {
                    newState = initState()
                }
                if( newState.hostList.isEmpty() || !newState.hostList.get(0).name.equals(MOCK)) {
                    addMockHost(newState)
                }
            }
            else -> newState = inState
        }
        assignState(newState)
        return newState
    }

    fun getStateObservable(): io.reactivex.Observable<SettingState> {
        return subject
    }

    fun getState(): SettingState {
        return state
    }
}


