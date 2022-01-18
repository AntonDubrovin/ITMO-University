package org.csc.kotlin2021

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RegistryApi {
    @POST("/v1/users")
    fun register(@Body newUserInfo: UserInfo): Call<Map<String, String>>

    @PUT("/v1/users/{user}")
    fun update(@Body address: UserAddress): Call<Map<String, String>>

    @GET("/v1/users")
    fun list(): Call<Map<String, UserAddress>>

    @DELETE("/v1/users/{user}")
    fun unregister(@Path("user") user: String): Call<Map<String, String>>
}

interface HttpApi {
    @GET("/v1/ping")
    fun ping(): Call<Map<String, String>>

    @POST("/v1/message")
    fun sendMessage(@Body message: Message): Call<Map<String, String>>
}

fun <T> Response<T>.getOrNull(): T? {
    if (!this.isSuccessful) {
        return null
    }
    return this.body()
}
