package top.e404.eapi.common.js.module

import top.e404.eapi.common.EApiCommon

@Suppress("UNUSED")
object ScriptConsole {
    fun log(vararg args: Any?) = info(*args)
    fun info(vararg args: Any?) = EApiCommon.instance.debug(format("info", *args))
    fun warn(vararg args: Any?) = EApiCommon.instance.debug(format("warn", *args))
    fun error(vararg args: Any?) = EApiCommon.instance.debug(format("error", *args))
    fun debug(vararg args: Any?) = EApiCommon.instance.debug(format("debug", *args))
    private fun format(vararg args: Any?) = args.joinToString(separator = " ") { it.toString() }
}