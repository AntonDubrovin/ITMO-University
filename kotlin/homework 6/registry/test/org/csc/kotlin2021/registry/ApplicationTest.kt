package org.csc.kotlin2021.registry

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.csc.kotlin2021.UserAddress
import org.csc.kotlin2021.UserInfo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.fail

fun Application.testModule() {
    (environment.config as MapApplicationConfig).apply {
        // define test environment here
    }
    module(testing = true)
}

class ApplicationTest {
    private val objectMapper = jacksonObjectMapper()
    private val testUserName = "pupkin"
    private val testHttpAddress = UserAddress("127.0.0.1", 9999)
    private val userData = UserInfo(testUserName, testHttpAddress)

    @BeforeEach
    fun clearRegistry() {
        Registry.users.clear()
    }

    @Test
    fun `health endpoint`() {
        withTestApplication({ testModule() }) {
            handleRequest(HttpMethod.Get, "/v1/ping").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("OK", response.content)
            }
        }
    }

    @Test
    fun `register user`() = withRegisteredTestUser { }

    @Test
    fun `list users`() = withRegisteredTestUser {
        handleRequest {
            method = HttpMethod.Get
            uri = "/v1/users"
            addHeader("Content-type", "application/json")
        }.apply {
            assertContent(mapOf(testUserName to testHttpAddress))
        }
    }

    @Test
    fun `delete user`() = withRegisteredTestUser {
        handleRequest {
            method = HttpMethod.Delete
            uri = "/v1/users/$testUserName"
            addHeader("Content-type", "application/json")
        }.apply {
            assertOk()
        }
    }

    private fun withRegisteredTestUser(block: TestApplicationEngine.() -> Unit) {
        withTestApplication({ testModule() }) {
            handleRequest {
                method = HttpMethod.Post
                uri = "/v1/users"
                addHeader("Content-type", "application/json")
                setBody(objectMapper.writeValueAsString(userData))
            }.apply {
                assertOk()
                this@withTestApplication.block()
            }
        }
    }

    private inline fun <reified T> TestApplicationCall.assertContent(expectedContent: T) {
        assertEquals(HttpStatusCode.OK, response.status())
        val rawContent = response.content ?: fail("No response content")
        val actualContent = objectMapper.readValue<T>(rawContent)
        assertEquals(expectedContent, actualContent)
    }

    private fun TestApplicationCall.assertOk() = assertContent(mapOf("status" to "ok"))
}
