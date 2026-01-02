package top.e404.eapi.js.module

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.openjdk.nashorn.api.scripting.AbstractJSObject
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
import top.e404.eapi.js.ctx

object AwaitAll : AbstractJSObject() {
    override fun call(thiz: Any?, vararg args: Any?): Any {
        val deferredList = args.flatMap {
            when (it) {
                is ScriptObjectMirror ->
                    if (it.isArray) it.values
                    else error("AwaitAll 仅支持传入数组或单个对象")

                is List<*> -> it
                is Iterable<*> -> it.toList()
                else -> listOf(it)
            }
        }.filterIsInstance<Deferred<*>>()
        return runBlocking(ctx) {
            deferredList.awaitAll()
        }
    }
}
