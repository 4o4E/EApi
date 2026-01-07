# [EApi](https://github.com/4o4E/EApi)

[![Release](https://img.shields.io/github/v/release/4o4E/EApi?label=Release)](https://github.com/4o4E/EApi/releases/latest)
[![Downloads](https://img.shields.io/github/downloads/4o4E/EApi/total?label=Download)](https://github.com/4o4E/EApi/releases)

在服务器中通过js脚本定义http接口, 支持sql查询和http请求。

[![bstats](https://bstats.org/signatures/bukkit/EApi.svg)](https://bstats.org/plugin/bukkit/EApi)
[![bstats](https://bstats.org/signatures/velocity/EApiVelocity.svg)](https://bstats.org/plugin/velocity/EApiVelocity)

## 内置函数

- `console.log` (以及debug info warn error) 输出调试信息到控制台 前提是打开 debug 选项
- `fail(code: number, message: string)` 可用来抛出错误响应
- `require(fileName: string)` 用于加载外部js文件 (文件内容无缓存)
- `sql(dbName: string, sql: string)` 用于执行数据库操作 返回值为 List<Map<String, Object>>
- `sync<T>(() => T)` 用于执行同步操作 并返回结果 应该仅在调用检查调用线程的函数时使用
- `request(url: string, method?: string, body?: string)` 用于发送HTTP请求 返回值为 Deferred<{status: number, body: string}> 需要用 await(deferred) 接受
- `await(Deferred)` 用来等待异步操作完成
- `awaitAll(Deferred[])` 用来等待多个异步操作完成 主要用于并行查询数据库

## 内置变量

- pathParameters 路径参数 类型: Map<String, String> 使用示例: `pathParameters.get('key')`
- queryParameters 查询参数 类型: Map<String, String> 使用示例: `queryParameters.get('key')`

## 示例配置

[spigot示例配置](spigot/src/main/resources/config.yml)

[velocity示例配置](velocity/src/main/resources/config.yml)