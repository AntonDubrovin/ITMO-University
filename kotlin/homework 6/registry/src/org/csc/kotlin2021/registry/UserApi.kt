package org.csc.kotlin2021.registry

import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    @GET("/v1/ping")
    fun ping(): Call<Map<String, String>>
}
