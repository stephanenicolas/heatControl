package com.github.stephanenicolas.heatcontrol.network

import okhttp3.HttpUrl

@FunctionalInterface
interface BaseUrlProvider {
    fun getBaseUrl(): HttpUrl
}