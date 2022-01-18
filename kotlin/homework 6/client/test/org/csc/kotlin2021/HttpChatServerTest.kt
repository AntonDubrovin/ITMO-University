package org.csc.kotlin2021

import org.csc.kotlin2021.server.ChatMessageListener
import org.csc.kotlin2021.server.HttpChatServer
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import kotlin.concurrent.thread
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HttpChatServerTest {

    @Test
    fun goodMessage() {
        val response = httpApi.sendMessage(message).execute()
        assertTrue(response.isSuccessful)
    }

    @Test
    fun badMessage() {
        val response = httpApi.sendMessage(Message("Bad name", "bad text")).execute()
        assertEquals(errorCode, response.code())
    }

    class TestChatMessageListener : ChatMessageListener {
        override fun messageReceived(userName: String, text: String) {
            assertEquals(message.user, userName)
            assertEquals(message.text, text)
        }
    }

    companion object {

        const val errorCode = 500
        private val message = Message("testUserName", "testText")
        private val httpApi = Retrofit.Builder()
            .baseUrl("http://127.0.0.1:9999")
            .addConverterFactory(JacksonConverterFactory.create())
            .build().create(HttpApi::class.java)

        @JvmStatic
        @BeforeAll
        fun createHttpChatServer() {
            thread {
                HttpChatServer("127.0.0.1", 9999).apply {
                    setMessageListener(TestChatMessageListener())
                    start()
                }
            }
            while (true) {
                try {
                    val response = httpApi.ping().execute()
                    if (response.isSuccessful) {
                        return
                    }
                } catch (e: Exception) {
                }
                Thread.sleep(100)
            }
        }
    }
}
