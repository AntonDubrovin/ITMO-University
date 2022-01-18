package org.csc.kotlin2021.registry

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import org.csc.kotlin2021.UserAddress
import org.csc.kotlin2021.UserInfo
import org.csc.kotlin2021.checkUserName
import org.slf4j.event.Level
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

const val checkDurationSeconds = 120

fun main(args: Array<String>) {
    thread(isDaemon = true) {
        while (true) {
            try {
                Thread.sleep(checkDurationSeconds * 1000L)
                val users = Registry.users.toMap()
                users.forEach { (name, userWithApi) ->
                    val response = userWithApi.userApi.ping().execute()
                    if (!response.isSuccessful || response.body()?.get("status") != "OK") {
                        Registry.users.remove(name)
                    }
                }
            } catch (e: Exception) {
                println("Failed to poll users")
            }
        }
    }
    EngineMain.main(args)
}

object Registry {
    val users = ConcurrentHashMap<String, UserWithApi>()

    fun addUser(userInfo: UserInfo) {
        val userApi = Retrofit.Builder()
            .baseUrl(userInfo.address.toString())
            .addConverterFactory(JacksonConverterFactory.create())
            .build().create(UserApi::class.java)
        val userWithApi = UserWithApi(userInfo.address, userApi)
        users.computeIfAbsent(userInfo.name) { userWithApi }
    }
}

@Suppress("UNUSED_PARAMETER")
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(StatusPages) {
        exception<IllegalArgumentException> { cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "invalid argument")
        }
        exception<UserAlreadyRegisteredException> { cause ->
            call.respond(HttpStatusCode.Conflict, cause.message ?: "user already registered")
        }
        exception<IllegalUserNameException> { cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "illegal user name")
        }
    }
    routing {
        get("/v1/ping") {
            call.respondText("OK", contentType = ContentType.Text.Plain)
        }

        post("/v1/users") {
            val user = call.receive<UserInfo>()
            val name = user.name
            checkUserName(name) ?: throw IllegalUserNameException()
            if (Registry.users.containsKey(name)) {
                throw UserAlreadyRegisteredException()
            }
            Registry.addUser(user)
            call.respond(mapOf("status" to "ok"))
        }

        get("/v1/users") {
            call.respond(Registry.users.mapValues { it.value.userAddress })
        }

        put("/v1/users/{name}") {
            val address = call.receive<UserAddress>()
            val name = call.parameters["name"] ?: throw IllegalArgumentException()
            checkUserName(name) ?: throw IllegalUserNameException()
            Registry.addUser(UserInfo(name, address))
            call.respond(mapOf("status" to "ok"))
        }

        delete("/v1/users/{name}") {
            val name = call.parameters["name"] ?: throw IllegalArgumentException()
            checkUserName(name) ?: throw IllegalUserNameException()
            Registry.users.remove(name)
            call.respond(mapOf("status" to "ok"))
        }
    }
}

class UserAlreadyRegisteredException : RuntimeException("User already registered")
class IllegalUserNameException : RuntimeException("Illegal user name")
