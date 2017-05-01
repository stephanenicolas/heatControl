package com.github.stephanenicolas.heatcontrol

import android.app.Application
import com.github.stephanenicolas.heatcontrol.network.BaseUrlProvider
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.configuration.Configuration
import java.util.concurrent.TimeUnit

class HeatControlApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(Configuration.forDevelopment().enableReflection())
        val appScope: Scope = Toothpick.openScope(this)

        //Mock webserver for now
        val mockWebServer: MockWebServer = MockWebServer()
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8")
        mockResponse.setBody("{\"ambientTemp\": \"10\", \"targetTemp\": \"37.5\"}")
        mockResponse.throttleBody(10, 1, TimeUnit.SECONDS)
        mockWebServer.enqueue(mockResponse)
        appScope.installModules(MockModule(mockWebServer))
        Observable.fromCallable { mockWebServer.start() }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}

class MockModule : Module {
    constructor(mockWebServer: MockWebServer) {
        bind(BaseUrlProvider::class.java).toInstance(object : BaseUrlProvider {
            override fun getBaseUrl(): HttpUrl = mockWebServer.url("/")
        })
    }
}