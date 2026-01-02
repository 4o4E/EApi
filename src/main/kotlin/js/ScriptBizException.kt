package top.e404.eapi.js

class ScriptBizException(
    val code: Int,
    override val message: String
) : RuntimeException(message)