package com.aph.willywonka.data.network

import okhttp3.Interceptor
import okhttp3.Response

//NOTA: Generic class for adding headers to calls
class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

            .build()

        return chain.proceed(request)
    }
}