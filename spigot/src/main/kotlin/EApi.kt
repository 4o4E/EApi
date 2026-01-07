package top.e404.eapi

import org.bukkit.Bukkit
import top.e404.eapi.command.Commands
import top.e404.eapi.common.EApiCommon
import top.e404.eapi.config.Config
import top.e404.eapi.config.Lang
import top.e404.eplugin.EPlugin

@Suppress("UNUSED")
open class EApi : EPlugin() {
    override val debugPrefix get() = langManager["debug_prefix"]
    override val prefix get() = langManager["prefix"]

    override val bstatsId = 28561
    override var debug: Boolean
        get() = Config.config.debug
        set(value) {
            Config.config.debug = value
        }
    override val langManager by lazy { Lang }

    init {
        PL = this
        EApiCommon.instance = object : EApiCommon {
            override val config get() = Config.config
            override val dataFolder = getDataFolder()
            override val isPrimaryThread get() = Bukkit.isPrimaryThread()
            override fun debug(message: String) = PL.debug(message)
            override fun debug(message: () -> String) = PL.debug(true, message)
            override fun warn(message: String, throwable: Throwable?) = PL.warn(message, throwable)
            override fun runTask(task: () -> Unit) {
                PL.runTask { task() }
            }

            override fun runTaskLater(delayMillis: Long, task: () -> Unit) {
                PL.runTaskLater(delayMillis) { task() }
            }

            override val jsModules = EApiCommon.jsModules
        }
    }

    override fun onEnable() {
        bstats()
        Lang.load(null)
        Config.load(null)
        Commands.register()
        EApiCommon.instance.load()
    }

    override fun onDisable() {
        EApiCommon.instance.stop()
        Bukkit.getScheduler().cancelTasks(this)
    }


}

lateinit var PL: EPlugin
    private set
