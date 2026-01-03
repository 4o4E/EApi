package top.e404.eapi.command

import top.e404.eapi.PL
import top.e404.eapi.config.Lang
import top.e404.eplugin.command.AbstractDebugCommand

/**
 * debug指令
 */
object Debug : AbstractDebugCommand(
    PL,
    "eapi.admin"
) {
    override val usage get() = Lang["command.usage.debug"]
}
