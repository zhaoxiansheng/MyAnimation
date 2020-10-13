package com.example.zy.myanimation.net

import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created on 2018/2/25.
 * retrofit单例
 *
 * @author zhaoy
 */

object RetrofitFactory {

    private val TIMEOUT: Long = 30
    private val BASE_URL: String = "http://image.baidu.com/"

    private val httpClient = OkHttpClient.Builder()
            .addInterceptor(NetworkInterceptor())
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                Logger.d(chain.request().url().toString())
                chain.proceed(builder.build())
            }
            .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Logger.d(message) }))
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

    val retrofitGsonService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()

    fun <T> doHttpRequest(pObservable: Observable<T>): Observable<T> {
        val observable = pObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        return observable
    }
}