import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = Versions.GROUP
    version = Versions.VERSION

    repositories {
        // papermc
        maven("https://repo.papermc.io/repository/maven-public/")
        // spigot
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        // sonatype
        maven("https://oss.sonatype.org/content/groups/public/")
        // nexus
        maven("https://nexus.e404.top:3443/repository/maven-snapshots/")
        mavenCentral()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}
