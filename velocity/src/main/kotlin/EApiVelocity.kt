package top.e404.eapi

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import org.bstats.velocity.Metrics
import org.slf4j.Logger
import top.e404.eapi.command.Commands
import top.e404.eapi.common.EApiCommon
import top.e404.eapi.config.Config
import top.e404.eapi.config.Lang
import top.e404.eplugin.EPluginVelocity
import java.nio.file.Path

@Suppress("UNUSED")
class EApiVelocity @Inject constructor(
    server: ProxyServer,
    logger: Logger,
    container: PluginContainer,
    @DataDirectory dataDir: Path,
    metricsFactory: Metrics.Factory
) : EPluginVelocity(server, logger, container, dataDir, metricsFactory) {
    override val debugPrefix get() = langManager["debug_prefix"]
    override val prefix get() = langManager["prefix"]

    override val bstatsId = 28568
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
            override val dataFolder = PL.dataFolder
            override fun debug(message: String) = PL.debug(message)
            override fun debug(message: () -> String) = PL.debug(true, message)
            override fun warn(message: String, throwable: Throwable?) = PL.warn(message, throwable)
            override fun runTaskLater(delayMillis: Long, task: () -> Unit) {
                PL.runTaskLater(delayMillis) {
                    task()
                }
            }
            override val jsModules = arrayOf(
                *EApiCommon.jsModules,
                "server" to server
            )
        }
    }

    @Subscribe
    fun onEnable(event: ProxyInitializeEvent) {
        bstats()
        Lang.load(null)
        Config.load(null)
        Commands.register()
        EApiCommon.instance.load()
    }

    @Subscribe
    fun onDisable(event: ProxyShutdownEvent) {
        EApiCommon.instance.stop()
        server.scheduler.tasksByPlugin(this).forEach { it.cancel() }
    }
}

lateinit var PL: EApiVelocity
    private set
