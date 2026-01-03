package top.e404.eapi.command

import com.velocitypowered.api.command.CommandSource
import top.e404.eapi.PL
import top.e404.eapi.common.EApiCommon
import top.e404.eapi.config.Config
import top.e404.eapi.config.Lang
import top.e404.eplugin.command.ECommand

object Reload : ECommand(
    PL,
    "reload",
    "(?i)r|reload",
    false,
    "eapi.admin"
) {
    override val usage get() = Lang["command.usage.reload"]

    override fun onCommand(sender: CommandSource, args: Array<out String>) {
        plugin.runTaskAsync {
            Lang.load(sender)
            Config.load(sender)
            EApiCommon.instance.reload()
            PL.sendMsgWithPrefix(sender, Lang["command.reload_done"])
        }
    }
}
