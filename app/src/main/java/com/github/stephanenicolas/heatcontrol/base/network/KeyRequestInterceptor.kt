package com.github.stephanenicolas.heatcontrol.base.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class KeyRequestInterceptor @Inject constructor(var baseUrlProvider: BaseUrlProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
                .addHeader("cle", baseUrlProvider.key)
                .build()
        val response = chain.proceed(request)
        return response
    }

}