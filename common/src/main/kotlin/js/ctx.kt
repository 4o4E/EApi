package top.e404.eapi.common.js

import kotlinx.coroutines.*
import top.e404.eapi.common.EApiCommon

val ctx = Dispatchers.IO + SupervisorJob() + CoroutineName("RequestJS") + CoroutineExceptionHandler { _, exception ->
    EApiCommon.instance.warn("http请求异常", exception)
}
val scope = CoroutineScope(ctx + SupervisorJob() + CoroutineName("RequestJS"))