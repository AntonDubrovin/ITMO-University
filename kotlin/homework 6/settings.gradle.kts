pluginManagement {
    plugins {
        val kotlin_version: String by settings
        kotlin("jvm").version(kotlin_version)
        id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    }
}

include("shared")
include("client")
include("registry")
