package top.e404.eapi.js

import top.e404.eapi.js.module.Await
import top.e404.eapi.js.module.AwaitAll
import top.e404.eapi.js.module.Fail
import top.e404.eapi.js.module.Request
import top.e404.eapi.js.module.Require
import top.e404.eapi.js.module.ScriptConsole
import top.e404.eapi.js.module.Sql

val jsModules = arrayOf(
    "console" to ScriptConsole,
    "fail" to Fail,
    "require" to Require,
    "sql" to Sql,
    "request" to Request,
    "await" to Await,
    "awaitAll" to AwaitAll,
)