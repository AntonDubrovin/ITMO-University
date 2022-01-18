package org.csc.kotlin2021.client

import org.csc.kotlin2021.HttpApi
import org.csc.kotlin2021.Message
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class HttpChatClient(host: String, port: Int) {
    private val httpApi: HttpApi = Retrofit.Builder()
        .baseUrl("http://$host:$port")
        .addConverterFactory(JacksonConverterFactory.create())
        .build().create(HttpApi::class.java)

    fun sendMessage(message: Message) {
        val response = httpApi.sendMessage(message).execute()
        if (!response.isSuccessful) {
            println("${response.code()} ${response.message()}}")
        }
    }
}
