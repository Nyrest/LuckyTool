package com.luckyzyx.colorosext.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import com.highcapable.yukihookapi.hook.factory.classOf
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method

/**
 * try/catch函数
 * @param default 异常返回值
 * @param result 正常回调值
 * @return [T] 发生异常时返回设定值否则返回正常值
 */
inline fun <T> safeOf(default: T, result: () -> T) = try {
    result()
} catch (_: Throwable) {
    default
}

/**
 * 获取ColorOS版本
 * @return [String]
 */
val getColorOSVersion get() = safeOf(default = "无法获取") {
        classOf(name = "com.oplus.os.OplusBuild").let {
            it.field { name = "VERSIONS" }.ignoredError().get().array<String>().takeIf { e -> e.isNotEmpty() }
                ?.get(it.method { name = "getOplusOSVERSION" }.ignoredError().get().int() - 1)
        }
    }


fun Number.dpToPx(context: Context? = null): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )