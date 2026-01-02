package top.e404.eapi.js.module

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.openjdk.nashorn.api.scripting.AbstractJSObject
import kotlinx.coroutines.async
import top.e404.eapi.js.JsObjectMap
import top.e404.eapi.js.ScriptBizException
import top.e404.eapi.js.scope

object Request : AbstractJSObject() {
    private val client = HttpClient(CIO)
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
            val resp = client.request(url) {
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