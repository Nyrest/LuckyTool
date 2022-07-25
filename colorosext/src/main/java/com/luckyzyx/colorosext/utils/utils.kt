package com.luckyzyx.colorosext.utils

import android.os.Build
import com.highcapable.yukihookapi.hook.factory.classOf
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.StringType

/**
 * 忽略异常返回值
 * @param default 异常返回值
 * @param result 正常回调值
 * @return [T] 发生异常时返回设定值否则返回正常值
 */
private inline fun <T> safeOf(default: T, result: () -> T) = try {
    result()
} catch (_: Throwable) {
    default
}


/**
 * 获取 ColorOS 版本
 * @return [String]
 */
val colorOSVersion get() = "$colorOSNumberVersion ${Build.DISPLAY}"

/**
 * 获取 ColorOS 数字版本
 * @return [String]
 */
val colorOSNumberVersion
    get() = safeOf(default = "无法获取") {
        (classOf(name = "com.oplus.os.OplusBuild").let {
            it.field { name = "VERSIONS" }.ignoredError().get().array<String>().takeIf { e -> e.isNotEmpty() }
                ?.get(it.method { name = "getOplusOSVERSION" }.ignoredError().get().int() - 1)
        } ?: findPropString(
            key = "ro.system.build.fingerprint", default = "无法获取"
        ).split("ssi:")[1].split("/")[0].trim())
    }

/**
 * 获取系统 Prop 值
 * @param key Key
 * @param default 默认值
 * @return [String]
 */
private fun findPropString(key: String, default: String = "") = safeOf(default) {
    (classOf(name = "android.os.SystemProperties").method {
        name = "get"
        param(StringType, StringType)
    }.get().invoke(key, default)) ?: default
}
