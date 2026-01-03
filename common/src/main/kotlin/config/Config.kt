package top.e404.eapi.common.config

import io.ktor.client.engine.*
import kotlinx.serialization.Serializable
import java.net.InetSocketAddress
import java.net.Proxy

@Serializable
data class ConfigData(
    var debug: Boolean,
    val server: HttpServerConfig,
    val proxy: HttpProxyConfig,
    val nashorn: NashornConfig,
    val databases: Map<String, DatabaseConfig>,
    val routers: List<RouterConfig>,
)

@Serializable
data class NashornConfig(
    val params: List<String>,
    val variables: Map<String, String>
) {
    val varArray by lazy {
        variables.map { (k, v) -> k to v }.toTypedArray()
    }
}

@Serializable
data class HttpServerConfig(
    val enable: Boolean,
    val port: Int,
)

@Serializable
data class HttpProxyConfig(
    val enable: Boolean,
    val type: ProxyType,
    val host: String,
    val port: Int,
    val ignoreSimpleHost: Boolean,
    val noProxy: List<String>
) {
    val proxy by lazy {
        val proxyType = when (type) {
            ProxyType.HTTP -> Proxy.Type.HTTP
            ProxyType.SOCKS -> Proxy.Type.SOCKS
            else -> Proxy.Type.DIRECT
        }
        Proxy(proxyType, InetSocketAddress(host, port))
    }
}

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