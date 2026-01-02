package top.e404.eapi.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import top.e404.eapi.PL
import top.e404.eapi.config.Config
import top.e404.eapi.config.RouterConfig
import top.e404.eapi.js.ScriptBizException
import top.e404.eapi.js.jsModules
import javax.script.ScriptEngine
import javax.script.ScriptException
import javax.script.SimpleBindings

object HttpServer {
    private val factory = NashornScriptEngineFactory()
    var server: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>? = null

    // http server

    fun stop() {
        server?.stop(1000, 2000)
        server = null
    }

    fun start() {
        val config = Config.config
        if (!config.server.enable) return
        val scriptEngine = factory.getScriptEngine(
            config.nashorn.params.toTypedArray(),
            PL.javaClass.classLoader
        )
        server = embeddedServer(CIO, config.server.port) {
            install(CallLogging) {
                level = Level.INFO
                logger = LoggerFactory.getLogger("Ktor")
            }
            routing {
                for (routerConfig in config.routers) {
                    val method = when (routerConfig.method) {
                        "GET" -> HttpMethod.Get
                        "POST" -> HttpMethod.Post
                        "PATCH" -> HttpMethod.Patch
                        "PUT" -> HttpMethod.Put
                        else -> {
                            log.warn("跳过不支持的路由方法: ${routerConfig.method}")
                            continue
                        }
                    }
                    PL.debug { "注册 ${routerConfig.path}" }
                    configureRoute(routerConfig, method, scriptEngine)
                }
            }
        }.also { it.start(false) }
    }

    // script exec

    private fun Routing.configureRoute(
        routerConfig: RouterConfig,
        method: HttpMethod,
        scriptEngine: ScriptEngine
    ) = route(routerConfig.path, method) {
        handle {
            try {
                // 注入查询参数
                val bindings = SimpleBindings(
                    mutableMapOf<String, Any>(
                        *jsModules,
                        *Config.config.nashorn.varArray,
                        "pathParameters" to call.pathParameters.entries().associate {
                            it.key to it.value.firstOrNull()
                        },
                        "queryParameters" to call.queryParameters.entries().associate {
                            it.key to it.value.firstOrNull()
                        },
                    )
                )
                val result = scriptEngine.eval(routerConfig.script, bindings).toString()
                call.respondText(result, ContentType.Application.Json)
            } catch (e: ScriptBizException) {
                call.respondText(
                    e.message,
                    ContentType.Text.Plain,
                    HttpStatusCode.fromValue(e.code)
                )
            } catch (e: ScriptException) {
                PL.warn("执行路由脚本时发生错误", e)
                call.respondText(
                    "执行脚本发生错误: ${e.message}",
                    ContentType.Text.Plain,
                    HttpStatusCode.InternalServerError
                )
            }
        }
    }
}