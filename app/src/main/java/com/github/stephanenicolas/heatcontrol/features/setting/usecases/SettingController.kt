package com.github.stephanenicolas.heatcontrol.features.control.usecases

import android.content.SharedPreferences
import com.github.stephanenicolas.heatcontrol.base.network.BaseUrlProvider
import com.github.stephanenicolas.heatcontrol.features.control.state.*
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val KEY_SETTING:String = "KEY_SETTING"

class SettingController @Inject constructor(val settingStore: SettingStore,
                                            val sharedPrefs: SharedPreferences,
                                            val baseUrlProvider: BaseUrlProvider) {

    var mockWebServer: MockWebServer? = null

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
        if(!settingStore.getState().hostList.isEmpty()
                && settingStore.getState().hostList.get(0).name.equals(MOCK)
                && settingStore.getState().hostList.get(0).isSelected) {
            startMockWebServer()
        } else {
            stopMockWebServer()
        }
        baseUrlProvider.update()
    }

    fun renameHost(position: Int, name: String) {
        settingStore.dispatch(RenameHostAction(position, name))
    }

    fun changeAddressHost(position: Int, address: String) {
        settingStore.dispatch(SetHostAction(position, address))
        baseUrlProvider.update()
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
        baseUrlProvider.update()
    }

    fun save(settingState: SettingState) {
        val gson: Gson = Gson()
        val settingSaved = gson.toJson(settingState)
        sharedPrefs.edit().putString(KEY_SETTING, settingSaved).apply()
        settingStore.dispatch(SaveAction())
    }

    private fun startMockWebServer() {
        if(mockWebServer == null) {
            mockWebServer = MockWebServer()
            val mockResponse = MockResponse()
            mockResponse.setResponseCode(200)
            mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
            mockResponse.setBody("{\"ambientTemp\": \"10\", \"targetTemp\": \"37.5\"}")
            mockResponse.throttleBody(10, 1, TimeUnit.SECONDS)
            mockWebServer!!.enqueue(mockResponse)
            Observable.fromCallable { mockWebServer!!.start()}
                    .subscribeOn(Schedulers.io())
                    .subscribe()
        }
    }

    private fun stopMockWebServer() {
        if(mockWebServer != null) {
            mockWebServer!!.shutdown()
            mockWebServer = null
        }
    }
}
