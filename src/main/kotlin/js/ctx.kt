package top.e404.eapi.js

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import top.e404.eapi.PL

val ctx = Dispatchers.IO + SupervisorJob() + CoroutineName("RequestJS") + CoroutineExceptionHandler { _, exception ->
    PL.warn("http请求异常", exception)
}
val scope = CoroutineScope(ctx + SupervisorJob() + CoroutineName("RequestJS"))