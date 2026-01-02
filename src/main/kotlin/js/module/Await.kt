package top.e404.eapi.js.module

import org.openjdk.nashorn.api.scripting.AbstractJSObject
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import top.e404.eapi.js.ctx

object Await : AbstractJSObject() {
    override fun call(thiz: Any?, vararg args: Any?): Any? {
        val deferred = args[0] as Deferred<*>
        return runBlocking(ctx) {
            deferred.await()
        }
    }
}