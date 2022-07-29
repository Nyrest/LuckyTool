package com.luckyzyx.colorosext.utils

import android.content.Context
import android.content.pm.PackageManager
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
val getColorOSVersion
    get() = safeOf(default = "无法获取") {
        classOf(name = "com.oplus.os.OplusBuild").let {
            it.field { name = "VERSIONS" }.ignoredError().get().array<String>()
                .takeIf { e -> e.isNotEmpty() }
                ?.get(it.method { name = "getOplusOSVERSION" }.ignoredError().get().int() - 1)
        }
    }

/**
 * 获取APP版本/版本号/Commit
 * 写入SP xml文件内
 * @return [String]
 */
fun getAppVersion(context: Context, packName: String): String = safeOf(default = "无法获取") {
    val packageManager = context.packageManager
    val packageInfo = packageManager.getPackageInfo(packName, 0)
    val versionName = safeOf(default = "无法获取") { packageInfo.versionName }
    val versionCode = safeOf(default = "无法获取") { packageInfo.longVersionCode }
    val versionCommit = if (packName != "android") {
        safeOf(default = "无法获取") {
            packageManager.getApplicationInfo(
                packName,
                PackageManager.GET_META_DATA
            ).metaData.getString("versionCommit")
        }
    } else "null"
    SPUtils.putString(context, XposedPrefs, packName, versionCommit)
    return "$versionName($versionCode)[$versionCommit]"
}



