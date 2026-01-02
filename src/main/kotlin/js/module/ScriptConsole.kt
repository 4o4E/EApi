package top.e404.eapi.js.module

import top.e404.eapi.PL

@Suppress("UNUSED")
object ScriptConsole {
    fun log(vararg args: Any?) = info(*args)
    fun info(vararg args: Any?) = PL.debug(format("info", *args))
    fun warn(vararg args: Any?) = PL.debug(format("warn", *args))
    fun error(vararg args: Any?) = PL.debug(format("error", *args))
    fun debug(vararg args: Any?) = PL.debug(format("debug", *args))
    private fun format(vararg args: Any?) = args.joinToString(separator = " ") { it.toString() }
}