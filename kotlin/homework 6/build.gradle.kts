import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    id("org.jetbrains.kotlin.jvm") apply false
    id("org.jlleitschuh.gradle.ktlint") apply true
}

subprojects {
    version = "0.0.1"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "application")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<KotlinJvmProjectExtension> {
        sourceSets {
            getByName("main") {
                kotlin.setSrcDirs(listOf("src"))
                resources.setSrcDirs(listOf("resources"))
            }
            getByName("test") {
                kotlin.setSrcDirs(listOf("test"))
                resources.setSrcDirs(listOf("testresources"))
            }
        }
        target.compilations.all {
            javaSourceSet.java.setSrcDirs(defaultSourceSet.kotlin.sourceDirectories)
            javaSourceSet.resources.setSrcDirs(defaultSourceSet.resources.sourceDirectories)
        }
    }

    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        val junit_version: String by project
        "testImplementation"(kotlin("test-junit5"))
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:$junit_version")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:$junit_version")
    }

    tasks.withType<Test>().all {
        useJUnitPlatform()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile> {
        kotlinOptions {
            jvmTarget = "11"
            languageVersion = "1.5"
            apiVersion = "1.5"
        }
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        enableExperimentalRules.set(true)
        filter {
            exclude("**/resources/**")
        }
    }
}

