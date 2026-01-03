plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    // ktor
    implementation(ktor("server-core"))
    implementation(ktor("server-cio"))
    implementation(ktor("server-call-logging"))
    implementation(ktor("client-core"))
    implementation(ktor("client-okhttp"))
    // nashorn
    compileOnly("org.openjdk.nashorn:nashorn-core:15.7")
    // hikari
    compileOnly("com.zaxxer:HikariCP:7.0.2")
}
