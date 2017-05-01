package com.github.stephanenicolas.heatcontrol.base.network

import com.github.stephanenicolas.heatcontrol.features.control.state.Host
import com.github.stephanenicolas.heatcontrol.features.control.state.MOCK
import com.github.stephanenicolas.heatcontrol.features.control.state.SettingState
import com.github.stephanenicolas.heatcontrol.features.control.state.SettingStore
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.net.URI
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseUrlProvider @Inject constructor(val settingStore: SettingStore) {

    var httpUrl: HttpUrl? = null
    var mockWebServer: MockWebServer? = null

    init {
        update()
    }

    fun update() {
        val settingState = settingStore.getState()
        httpUrl = createBaseUrl(settingState)
    }

    private fun createBaseUrl(settingState: SettingState): HttpUrl? {
        val selectedHost:Host? = settingState.hostList.firstOrNull { it.isSelected }

        if(selectedHost == null) {
            return null
        }

        if(selectedHost.name.equals(MOCK)) {
            startMockWebServer()
            return null
        } else {
            stopMockWebServer()
        }
        return HttpUrl.get(URI.create(selectedHost.address))
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
            Observable.fromCallable {
                mockWebServer!!.start()
                httpUrl = mockWebServer!!.url("/")
            }
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