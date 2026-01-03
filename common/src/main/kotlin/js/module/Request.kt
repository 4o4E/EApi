package top.e404.eapi.common.js.module

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.async
import org.openjdk.nashorn.api.scripting.AbstractJSObject
import top.e404.eapi.common.EApiCommon
import top.e404.eapi.common.js.JsObjectMap
import top.e404.eapi.common.js.ScriptBizException
import top.e404.eapi.common.js.scope
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI

object Request : AbstractJSObject() {
    object HttpProxySelector : ProxySelector() {
        private val config get() = EApiCommon.instance.config.proxy
        override fun select(uri: URI): List<Proxy> {
            if (!config.enable) {
                return listOf(Proxy.NO_PROXY)
            }
            // 忽略机器名字
            if ("." !in uri.host && config.ignoreSimpleHost) {
                return listOf(Proxy.NO_PROXY)
            }
            // 如果主机在 "no proxy" 列表, 返回 "无代理"
            if (config.noProxy.any { uri.host.equals(it, true) }) {
                return listOf(Proxy.NO_PROXY)
            }
            // 否则，返回你的宿主机代理
            return listOf(config.proxy)
        }

        override fun connectFailed(uri: URI, sa: SocketAddress?, ioe: IOException?) {
            // 可以添加连接失败的逻辑
        }
    }

    private var client: HttpClient = HttpClient(OkHttp) {
        engine {
            config {
                proxySelector(HttpProxySelector)
            }
        }
    }

    override fun call(thiz: Any?, vararg args: Any?): Any {
        val url = args[0] as String
        val methodString = args.getOrNull(1)?.toString()?.uppercase() ?: "GET"
        val bodyString = args.getOrNull(2)?.toString()

        val httpMethod = when (methodString) {
            "GET" -> HttpMethod.Get
            "POST" -> HttpMethod.Post
            "PUT" -> HttpMethod.Put
            "DELETE" -> HttpMethod.Delete
            "PATCH" -> HttpMethod.Patch
            else -> throw ScriptBizException(400, "Unsupported HTTP method: $methodString")
        }

        return scope.async {
            val resp = client!!.request(url) {
                method = httpMethod
                bodyString?.let { setBody(it) }
            }
            JsObjectMap(
                mapOf(
                    "status" to resp.status.value,
                    "body" to resp.bodyAsText()
                )
            )
        }
    }
}