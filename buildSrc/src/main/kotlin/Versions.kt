object Versions {
    const val GROUP = "top.e404"
    const val VERSION = "1.3.0"

    const val EPLUGIN = "1.4.0-SNAPSHOT"
    const val KTOR = "3.3.3"
}


fun eplugin(module: String, version: String = Versions.EPLUGIN) = "top.e404.eplugin:eplugin-$module:$version"
fun ktor(module: String, version: String = Versions.KTOR) = "io.ktor:ktor-$module:$version"
