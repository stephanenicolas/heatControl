package com.github.stephanenicolas.heatcontrol.features.control.usecases

import android.content.SharedPreferences
import com.github.stephanenicolas.heatcontrol.features.control.state.*
import com.google.gson.Gson
import javax.inject.Inject

const val KEY_SETTING:String = "KEY_SETTING"

class SettingController @Inject constructor(val settingStore: SettingStore, val sharedPrefs: SharedPreferences) {

    fun setApiKey(key: String) {
        settingStore.dispatch(SetKeyAction(key))
    }

    fun addHost() {
        settingStore.dispatch(AddHostAction())
    }

    fun removeHost(position: Int) {
        settingStore.dispatch(DeleteHostAction(position))
    }

    fun setlectHost(position: Int) {
        settingStore.dispatch(SelectHostAction(position))
    }

    fun renameHost(position: Int, name: String) {
        settingStore.dispatch(RenameHostAction(position, name))
    }

    fun changeAddressHost(position: Int, address: String) {
        settingStore.dispatch(SetHostAction(position, address))
    }

    fun reset() {
        val settingSaved = sharedPrefs.getString(KEY_SETTING, null)
        if(settingSaved != null) {
            val gson: Gson = Gson()
            val settingState: SettingState = gson.fromJson(settingSaved, SettingState::class.java)
            settingStore.dispatch(ResetAction(settingState))
        } else {
            settingStore.dispatch(ResetAction(null))
        }
    }

    fun save(settingState: SettingState) {
        val gson: Gson = Gson()
        val settingSaved = gson.toJson(settingState)
        sharedPrefs.edit().putString(KEY_SETTING, settingSaved).apply()
        settingStore.dispatch(SaveAction())
    }

}
