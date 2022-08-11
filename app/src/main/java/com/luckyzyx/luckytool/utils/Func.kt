@file:Suppress("unused")

package com.luckyzyx.luckytool.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import com.highcapable.yukihookapi.hook.factory.classOf
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.BuildConfig.*
import com.luckyzyx.luckytool.R

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
fun getAppVersion(context: Context, packName: String, isCommit: Boolean): String = safeOf(default = "无法获取") {
    val packageManager = context.packageManager
    val packageInfo = packageManager.getPackageInfo(packName, 0)
    val versionName = safeOf(default = "无法获取") { packageInfo.versionName }
    val versionCode = safeOf(default = "无法获取") { packageInfo.longVersionCode }
    if (isCommit) {
        val versionCommit = safeOf(default = "无法获取") {
            packageManager.getApplicationInfo(packName, PackageManager.GET_META_DATA).metaData.getString("versionCommit")
        }
        SPUtils.putString(context, XposedPrefs, packName, versionCommit)
        return "$versionName($versionCode)[$versionCommit]"
    }
    return "$versionName($versionCode)"
}

/**
 * 获取构建版本名/版本号
 * @return [String]
 */
val getBuildVersion get() = "${VERSION_NAME}(${VERSION_CODE})"

/**
 * 查询包名是否存在
 */
fun checkPackName(context: Context, packageName: String): Boolean {
    val packageManager = context.packageManager
    val applist = packageManager.getInstalledPackages(0)
    for (i in applist.indices) {
        if (applist[i].packageName.equals(packageName, ignoreCase = true)) return true
    }
    return false
}

/**
 * 检查隐藏活动是否存在
 */
fun checkResolveActivity(context: Context, intent: Intent): Boolean {
    return context.packageManager.resolveActivity(intent, 0) != null
}

/**
 * Toast快捷方法
 * @param context Context
 * @param name 字符串
 * @param long 显示时长
 * @return [Toast]
 */
internal fun toast(context: Context, name: String, long: Boolean? = null): Any = if (long == true) {
    Toast.makeText(context, name, Toast.LENGTH_LONG).show()
} else {
    Toast.makeText(context, name, Toast.LENGTH_LONG).show()
}

/**
 * 获取支持的刷新率
 * @return [List]
 */
fun getFpsMode(context: Context): Array<String> {
    val command =
        "dumpsys display | grep -A 1 'mSupportedModesByDisplay' | tail -1 | tr '}' '\\n' | cut -f2 -d '{' | while read row; do\n" +
        "  if [[ -n \$row ]]; then\n" +
        "    echo \$row | tr ',' '\\n' | while read col; do\n" +
        "      case \$col in\n" +
        "      'width='*)\n" +
        "        echo -n \$(echo \${col:6})\n" +
        "        ;;\n" +
        "      'height='*)\n" +
        "        echo -n x\$(echo \${col:7})\n" +
        "        ;;\n" +
        "      'fps='*)\n" +
        "        echo ' '\$(echo \${col:4} | cut -f1 -d '.')Hz\n" +
        "        ;;\n" +
        "      esac\n" +
        "    done\n" +
        "    echo -e '@'\n" +
        "  fi\n" +
        "done"
    val result = ShellUtils.execCommand(command, true, true).successMsg
    return result.substring(0,result.length - 1).split("@").toMutableList().apply {
        add(context.getString(R.string.Restore_default_refresh_rate))
    }.toTypedArray()
}

/**
 * 跳转工程模式
 */
fun jumpEngineermode(context: Context){
    if (checkPackName(context, "com.oppo.engineermode")) {
        ShellUtils.execCommand("am start -n com.oppo.engineermode/.aftersale.AfterSalePage", true)
    }else if (checkPackName(context, "com.oplus.engineermode")) {
        ShellUtils.execCommand("am start -n com.oplus.engineermode/.aftersale.AfterSalePage", true)
    }
}
/**
 * 跳转充电测试
 */
fun jumpBatteryInfo(context: Context){
    if (checkPackName(context, "com.oppo.engineermode")) {
        ShellUtils.execCommand("am start -n com.oppo.engineermode/.charge.modeltest.BatteryInfoShow", true)
    }else if (checkPackName(context, "com.oplus.engineermode")) {
        ShellUtils.execCommand("am start -n com.oplus.engineermode/.charge.modeltest.BatteryInfoShow", true)
    }
}
/**
 * 跳转进程管理
 */
fun jumpRunningApp(context: Context){
    val isoppoRunning = Intent().setClassName(
        "com.android.settings",
        "com.coloros.settings.feature.process.RunningApplicationActivity"
    )
    val isoplusRunning = Intent().setClassName(
        "com.android.settings",
        "com.oplus.settings.feature.process.RunningApplicationActivity"
    )
    if (checkResolveActivity(context,isoppoRunning)) {
        ShellUtils.execCommand("am start -n com.android.settings/com.coloros.settings.feature.process.RunningApplicationActivity", true)
    } else if (checkResolveActivity(context,isoplusRunning)) {
        ShellUtils.execCommand("am start -n com.android.settings/com.oplus.settings.feature.process.RunningApplicationActivity", true)
    }
}

/**
 * 显示/隐藏APP图标
 */
fun setDesktopIcon(context: Context,value : Boolean){
    context.packageManager.setComponentEnabledSetting(ComponentName(APPLICATION_ID, "${APPLICATION_ID}.Hide"),
        if (value) PackageManager.COMPONENT_ENABLED_STATE_DISABLED else PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )
}