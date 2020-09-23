package com.example.zhaoy.eyepetizer.net

import com.example.zy.myanimation.bean.Test
import io.reactivex.Observable
import retrofit2.http.GET

interface IApi {

    @GET
    fun getBaiduShitu(): Observable<Test>
}