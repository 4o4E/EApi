package top.e404.eapi.common

import top.e404.eapi.common.config.ConfigData
import top.e404.eapi.common.db.DbManager
import top.e404.eapi.common.js.module.Await
import top.e404.eapi.common.js.module.AwaitAll
import top.e404.eapi.common.js.module.Fail
import top.e404.eapi.common.js.module.Request
import top.e404.eapi.common.js.module.Require
import top.e404.eapi.common.js.module.ScriptConsole
import top.e404.eapi.common.js.module.Sql
import top.e404.eapi.common.js.module.js.module.Sync
import top.e404.eapi.common.server.HttpServer
import java.io.File

interface EApiCommon {
    companion object {
        lateinit var instance: EApiCommon
        val jsModules = arrayOf(
            "console" to ScriptConsole,
            "fail" to Fail,
            "require" to Require,
            "sql" to Sql,
            "request" to Request,
            "await" to Await,
            "awaitAll" to AwaitAll,
            "sync" to Sync,
        )
    }

    val config: ConfigData
    val dataFolder: File
    val jsModules: Array<Pair<String, Any>>
    val isPrimaryThread: Boolean

    fun debug(message: String)
    fun debug(message: () -> String)
    fun warn(message: String, throwable: Throwable? = null)
    fun runTask(task: () -> Unit)
    fun runTaskLater(delayMillis: Long, task: () -> Unit)
    fun reload() {
        stop()
        load()
    }

    fun load() {
        DbManager.loadAll()
        HttpServer.start()
    }

    fun stop() {
        HttpServer.stop()
        DbManager.stopAll()
    }
}
