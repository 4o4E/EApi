package top.e404.eapi

import org.bukkit.Bukkit
import top.e404.eapi.command.Commands
import top.e404.eapi.config.Config
import top.e404.eapi.config.Lang
import top.e404.eapi.db.DbManager
import top.e404.eapi.server.HttpServer
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
    }

    override fun onEnable() {
        bstats()
        Lang.load(null)
        Config.load(null)
        Commands.register()
        DbManager.loadAll()
        HttpServer.start()
    }

    override fun onDisable() {
        HttpServer.stop()
        DbManager.stopAll()
        Bukkit.getScheduler().cancelTasks(this)
    }
}

lateinit var PL: EPlugin
    private set
