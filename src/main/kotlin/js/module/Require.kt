package top.e404.eapi.js.module

import org.openjdk.nashorn.api.scripting.AbstractJSObject
import org.openjdk.nashorn.internal.objects.Global
import org.openjdk.nashorn.internal.runtime.ConsString
import org.openjdk.nashorn.internal.runtime.ECMAErrors
import org.openjdk.nashorn.internal.runtime.ScriptRuntime
import top.e404.eapi.PL
import java.io.File

object Require : AbstractJSObject() {
    private fun defineJsFile(src: String): File {
        val target = PL.dataFolder.resolve(src)
        if (!target.exists() && !src.endsWith(".js")) {
            // 让用户可选输入 .js 后缀名
            val alternative = PL.dataFolder.resolve("$src.js")
            if (alternative.exists()) {
                return alternative
            }
        }
        return target
    }
    override fun call(thiz: Any, vararg args: Any): Any {
        val from = args[0]
        val src = if (from is ConsString) from.toString() else from
        if (src is String) {
            val file = defineJsFile(src)
            return Global.load(thiz, file)
        }
        throw ECMAErrors.typeError("not.a.string", ScriptRuntime.safeToString(from))
    }
}