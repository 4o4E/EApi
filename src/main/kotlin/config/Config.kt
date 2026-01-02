package top.e404.eapi.config

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.e404.eapi.PL
import top.e404.eplugin.config.JarConfigDefault
import top.e404.eplugin.config.KtxConfig

object Config : KtxConfig<ConfigData>(
    plugin = PL,
    path = "config.yml",
    default = JarConfigDefault(PL, "config.yml"),
    serializer = ConfigData.serializer(),
    format = Yaml(configuration = YamlConfiguration(strictMode = false))
)

@Serializable
data class ConfigData(
    var debug: Boolean,
    var server: HttpServerConfig,
    @SerialName("nashorn_params")
    var nashornParams: List<String>,
    val databases: Map<String, DatabaseConfig>,
    var routers: List<RouterConfig>,
)

@Serializable
data class HttpServerConfig(
    val enable: Boolean,
    val port: Int,
)

@Serializable
data class RouterConfig(
    val method: String,
    val path: String,
    val script: String
)

@Serializable
data class DatabaseConfig(
    val url: String,
    val driver: String,
    val username: String,
    val password: String,
    val pool: PoolConfig,
    val init: List<String>,
)

@Serializable
data class PoolConfig(
    val maximumPoolSize: Int,
    val minimumIdle: Int,
    val idleTimeout: Long,
    val maxLifetime: Long,
    val connectionTimeout: Long,
)