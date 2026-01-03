package top.e404.eapi.common.js

import org.openjdk.nashorn.api.scripting.AbstractJSObject

class JsObjectMap(private val map: Map<String, Any?>) : AbstractJSObject() {
    override fun getMember(name: String): Any? = map[name]
    override fun hasMember(name: String): Boolean = map.containsKey(name)
    override fun keySet(): Set<String> = map.keys
    override fun getClassName(): String = "Object"
}
