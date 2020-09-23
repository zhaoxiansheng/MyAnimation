package com.example.zhaoy.eyepetizer.net

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created on 2018/2/25.
 * 自定义网络拦截器
 *
 * @author zhaoy
 */

class NetworkInterceptor : Interceptor {

//    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        var tryCount = 0
        while (!response.isSuccessful && tryCount < MAX_TRY_COUNT) {
            tryCount++
            response = chain.proceed(request)
        }
        return response
    }

    companion object {

        private val MAX_TRY_COUNT = 5
    }
}