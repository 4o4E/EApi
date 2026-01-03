package top.e404.eapi.command

import org.bukkit.command.CommandSender
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

    override fun onCommand(sender: CommandSender, args: Array<out String>) {
        plugin.runTaskAsync {
            Lang.load(sender)
            Config.load(sender)
            EApiCommon.instance.reload()
            sender.sendMessage(Lang["command.reload_done"])
        }
    }
}
