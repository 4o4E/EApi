plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.gradleup.shadow")
}

dependencies {
    // spigot
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    // eplugin
    implementation(eplugin("core"))
    implementation(eplugin("serialization"))
    // Bstats
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation(project(":common"))
}

tasks {
    build {
        finalizedBy(shadowJar)
    }

    shadowJar {
        val archiveName = "EApi-${project.version}.jar"
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
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
}
