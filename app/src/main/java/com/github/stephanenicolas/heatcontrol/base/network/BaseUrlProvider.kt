package com.github.stephanenicolas.heatcontrol.base.network

import com.github.stephanenicolas.heatcontrol.features.control.state.Host
import com.github.stephanenicolas.heatcontrol.features.control.state.SettingState
import com.github.stephanenicolas.heatcontrol.features.control.state.SettingStore
import okhttp3.HttpUrl
import java.net.URI
import javax.inject.Singleton

@Singleton
class BaseUrlProvider(val settingStore: SettingStore) {

    var httpUrl: HttpUrl? = null

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
        return HttpUrl.get(URI.create(selectedHost.address))
    }
}