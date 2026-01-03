package top.e404.eapi.common.js.module

import org.openjdk.nashorn.api.scripting.AbstractJSObject
import top.e404.eapi.common.js.ScriptBizException

object Fail : AbstractJSObject() {
    override fun call(thiz: Any?, vararg args: Any?) = throw ScriptBizException(
        args[0] as Int,
        args[1] as String
    )
}