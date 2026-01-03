plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.gradleup.shadow")
}

dependencies {
    // spigot
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    // eplugin
    implementation(eplugin("velocity-core"))
    // Bstats
    implementation("org.bstats:bstats-velocity:3.0.2")
    implementation(project(":common"))
    // nashorn
    implementation("org.openjdk.nashorn:nashorn-core:15.7")
    // hikari
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("com.mysql:mysql-connector-j:9.5.0")
}

tasks {
    build {
        finalizedBy(shadowJar)
    }

    shadowJar {
        val archiveName = "EApiVelocity-${project.version}.jar"
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

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("velocity-plugin.json") {
            expand("version" to project.version)
        }
    }
}
