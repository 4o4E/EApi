package top.e404.eapi.common.js.module

import org.openjdk.nashorn.api.scripting.AbstractJSObject
import top.e404.eapi.common.db.DbManager
import top.e404.eapi.common.js.JsObjectMap
import top.e404.eapi.common.js.ScriptBizException

object Sql : AbstractJSObject() {
    override fun call(thiz: Any?, vararg args: Any?): Any {
        val dbName = args[0] as String
        val sql = args[1] as String
        val result = DbManager[dbName] ?: throw ScriptBizException(500, "数据库 $dbName 未配置")
        return result.useDb {
            it.createStatement().use { statement ->
                val resultSet = statement.executeQuery(sql)
                val list = mutableListOf<JsObjectMap>()
                while (resultSet.next()) {
                    val map = mutableMapOf<String, Any?>()
                    for (i in 1..resultSet.metaData.columnCount) {
                        val columnName = resultSet.metaData.getColumnName(i)
                        val columnValue = resultSet.getObject(i)
                        map[columnName] = columnValue
                        map.keys
                    }
                    list.add(JsObjectMap(map))
                }
                list
            }
        }
    }
}