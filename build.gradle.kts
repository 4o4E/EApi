import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

group = "top.e404"
version = "1.3.0"
val epluginVer = "1.4.0-SNAPSHOT"

fun eplugin(module: String, version: String = epluginVer) = "top.e404.eplugin:eplugin-$module:$version"

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

dependencies {
    // spigot
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    // eplugin
    implementation(eplugin("core"))
    implementation(eplugin("serialization"))
    // Bstats
    implementation("org.bstats:bstats-bukkit:3.0.2")

    // ktor
    implementation("io.ktor:ktor-server-core-jvm:3.3.3")
    implementation("io.ktor:ktor-server-cio-jvm:3.3.3")
    implementation("io.ktor:ktor-server-call-logging-jvm:3.3.3")
    implementation("io.ktor:ktor-client-core-jvm:3.3.3")
    implementation("io.ktor:ktor-client-cio-jvm:3.3.3")
    // nashorn
    compileOnly("org.openjdk.nashorn:nashorn-core:15.7")
    // hikari
    compileOnly("com.zaxxer:HikariCP:7.0.2")
}

kotlin {
    jvmToolchain(8)
}

tasks {
    build {
        finalizedBy(shadowJar)
    }

    shadowJar {
        val archiveName = "${project.name}-${project.version}.jar"
        archiveFileName.set(archiveName)

        relocate("org.bstats", "top.e404.eapi.relocate.bstats")
        relocate("kotlin", "top.e404.eapi.relocate.kotlin")
        relocate("top.e404.eplugin", "top.e404.eapi.relocate.eplugin")
        relocate("com.charleskorn.kaml", "top.e404.eapi.relocate.kaml")
        exclude("META-INF/**")

        doLast {
            val archiveFile = archiveFile.get().asFile
            println(archiveFile.parentFile.absolutePath)
            println(archiveFile.absolutePath)
        }
    }

    withType<KotlinCompile> {
        dependsOn(clean)
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
}
