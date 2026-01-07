package top.e404.eapi.common.js.module.js.module

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.openjdk.nashorn.api.scripting.AbstractJSObject
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
import top.e404.eapi.common.EApiCommon
import top.e404.eapi.common.js.ScriptBizException
import top.e404.eapi.common.js.ctx

object Sync : AbstractJSObject() {
    override fun call(thiz: Any?, vararg args: Any?): Any? {
        val arg0 = args.getOrNull(0)
        val func = arg0 as? ScriptObjectMirror
            ?: throw ScriptBizException(500, "sync 需要传入lambda或函数, 但传入了 ${arg0?.javaClass?.name ?: "null"}")

        require(func.isFunction) { "sync 需要传入lambda或函数, 但是传入的对象不可执行" }

        // 主线程直接执行
        if (EApiCommon.instance.isPrimaryThread) {
            return try {
                func.call(func)
            } catch (e: ScriptBizException) {
                throw e
            } catch (e: Exception) {
                throw ScriptBizException(500, "主线程脚本执行异常: ${e.message}", e)
            }
        }

        val deferred = CompletableDeferred<Any?>()
        EApiCommon.instance.runTask {
            try {
                deferred.complete(func.call(null))
            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }
        }
        return runBlocking(ctx) {
            try {
                deferred.await()
            } catch (e: ScriptBizException) {
                throw e
            } catch (e: Exception) {
                throw ScriptBizException(500, "主线程脚本执行异常: ${e.message}", e)
            }
        }
    }
}