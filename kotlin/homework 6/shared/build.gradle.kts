group = "org.csc.kotlin2021"

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.10.2")

    val ktor_version: String by project
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}
