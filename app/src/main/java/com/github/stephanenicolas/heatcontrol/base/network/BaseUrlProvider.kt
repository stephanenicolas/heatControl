package com.github.stephanenicolas.heatcontrol.base.network

import okhttp3.HttpUrl

@FunctionalInterface
interface BaseUrlProvider {
    fun getBaseUrl(): HttpUrl
}