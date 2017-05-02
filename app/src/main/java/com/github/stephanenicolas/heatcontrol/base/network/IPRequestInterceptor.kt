package com.github.stephanenicolas.heatcontrol.base.network

import okhttp3.Interceptor
import okhttp3.Response
import java.net.InetAddress
import javax.inject.Inject


class IPRequestInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
                .addHeader("clientIP", InetAddress.getLocalHost().hostAddress)
                .build()
        val response = chain.proceed(request)
        return response
    }

}