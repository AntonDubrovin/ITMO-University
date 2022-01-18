package org.csc.kotlin2021

data class UserAddress(
    val host: String,
    val port: Int = 8080
) {
    override fun toString(): String {
        return "http://$host:$port"
    }
}

data class UserInfo(val name: String, val address: UserAddress)

data class Message(val user: String, val text: String)

fun checkUserName(name: String) = """^[a-zA-Z0-9-_.]+$""".toRegex().find(name)
