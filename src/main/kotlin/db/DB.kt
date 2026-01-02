package top.e404.eapi.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import top.e404.eapi.PL
import top.e404.eapi.config.Config
import top.e404.eapi.config.DatabaseConfig
import java.sql.Connection

object DbManager {
    private val dbs = mutableMapOf<String, DB>()
    operator fun get(dbName: String) = dbs[dbName]

    fun loadAll() {
        for ((name, config) in Config.config.databases) {
            val db = DB(name)
            db.load(config)
            dbs[name] = db
        }
    }

    fun stopAll() {
        for ((_, db) in dbs) {
            db.stop()
        }
        dbs.clear()
    }


}

class DB(val name: String) {
    @Volatile
    private lateinit var datasource: HikariDataSource

    // 延迟关闭旧数据源
    fun stop() {
        if (::datasource.isInitialized) {
            val ds = datasource
            PL.runTaskLater(200) {
                ds.close()
            }
        }
    }

    fun load(config: DatabaseConfig) {
        datasource = HikariConfig().apply {
            jdbcUrl = config.url
            driverClassName = config.driver
            username = config.username
            password = config.password
            isAutoCommit = true
            maximumPoolSize = config.pool.maximumPoolSize
            minimumIdle = config.pool.minimumIdle
            idleTimeout = config.pool.idleTimeout
            maxLifetime = config.pool.maxLifetime
            connectionTimeout = config.pool.connectionTimeout
            poolName = "EStatsHikariCP-$name"
        }.let(::HikariDataSource)
        // init
        useDb {
            it.createStatement().use { stmt ->
                for (sql in config.init) {
                    PL.debug { "执行初始化sql: $sql" }
                    stmt.execute(sql)
                }
            }
        }
    }

    fun <T> useDb(block: (Connection) -> T) = datasource.connection.use(block)

}