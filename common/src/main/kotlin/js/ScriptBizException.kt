package top.e404.eapi.common.js

class ScriptBizException(
    val code: Int,
    override val message: String
) : RuntimeException(message)