@file:Suppress("unused")

package com.luckyzyx.luckytool.utils.tools

import android.content.*
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.ArraySet
import android.util.Base64
import android.widget.Toast
import com.highcapable.yukihookapi.hook.factory.classOf
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.BuildConfig.*
import com.luckyzyx.luckytool.R
import java.util.regex.Pattern


val SDK get() = Build.VERSION.SDK_INT

/**
 * 获取ColorOS版本
 * @return [String]
 */
val getColorOSVersion
    get() = safeOf(default = "null") {
        classOf(name = "com.oplus.os.OplusBuild").let {
            it.field { name = "VERSIONS" }.ignoredError().get().array<String>()
                .takeIf { e -> e.isNotEmpty() }
                ?.get(it.method { name = "getOplusOSVERSION" }.ignoredError().get().int() - 1)
        }
    }

/**
 * 获取APP版本/版本号/Commit
 * 写入SP xml文件内
 * @return [ArraySet]
 */
fun Context.getAppVersion(packName: String): ArrayList<String> = safeOf(default = ArrayList()) {
    val arrayList = ArrayList<String>()
    val arraySet = ArraySet<String>()
    val packageInfo = PackageUtils(packageManager).getPackageInfo(packName,0)
    val commitInfo = PackageUtils(packageManager).getApplicationInfo(packName,128)
    val versionName = safeOf(default = "null") { packageInfo.versionName }
    arrayList.add(versionName)
    arraySet.add("0.$versionName")
    val versionCode = safeOf(default = "null") { packageInfo.longVersionCode }
    arrayList.add(versionCode.toString())
    arraySet.add("1.$versionCode")
    val versionCommit = safeOf(default = "null") { commitInfo.metaData.getString("versionCommit") }
    arrayList.add(versionCommit.toString())
    arraySet.add("2.$versionCommit")
    putStringSet(XposedPrefs,packName,arraySet)
    return arrayList
}

/**
 * 获取构建版本名/版本号
 * @return [String]
 */
val getVersionName get() = VERSION_NAME
val getVersionCode get() = VERSION_CODE

fun Context.checkKey(key: String?, keyList: Array<String>): String = safeOfNothing {
    keyList.forEach {
        if (checkPackName(it)) {
            if (key == null) return it
        }
    }
    return "null"
}

/**
 * 获取APP
 */
fun Context.checkPackName(packName: String) = safeOfFalse {
    @Suppress("SENSELESS_COMPARISON")
    PackageUtils(packageManager).getPackageInfo(packName,0) != null
}

/**
 * 获取APP图标
 */
fun Context.getAppIcon(packName: String): Drawable? = safeOf(default = null) {
    return PackageUtils(packageManager).getApplicationInfo(packName,0).loadIcon(packageManager)
}
/**
 * 获取APP名称
 */
fun Context.getAppLabel(packName: String): CharSequence? = safeOf(default = null) {
    return PackageUtils(packageManager).getApplicationInfo(packName,0).loadLabel(packageManager)
}

/**
 * 检查隐藏活动是否存在
 */
fun Context.checkResolveActivity(intent: Intent): Boolean = safeOf(default = true){
    return PackageUtils(packageManager).resolveActivity(intent, 0) != null
}

/**
 * Toast快捷方法
 * @param name 字符串
 * @param long 显示时长
 * @return [Toast]
 */
internal fun Context.toast(name: String, long: Boolean? = false): Any = if (long == true) {
    Toast.makeText(this, name, Toast.LENGTH_LONG).show()
} else {
    Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
}

/**
 * 获取支持的刷新率
 * @return [List]
 */
fun Context.getFpsMode(): Array<String> {
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
    val result = ShellUtils.execCommand(command, true, true).successMsg ?: return arrayOf("获取错误,请勿点击")
    return result.substring(0,result.length - 1).split("@").toMutableList().apply {
        add(getString(R.string.Restore_default_refresh_rate))
    }.toTypedArray()
}

/**
 * 跳转工程模式
 */
fun jumpEngineermode(context: Context) {
    if (context.checkPackName("com.oppo.engineermode")) {
        ShellUtils.execCommand("am start -n com.oppo.engineermode/.aftersale.AfterSalePage", true)
    }else if (context.checkPackName("com.oplus.engineermode")) {
        ShellUtils.execCommand("am start -n com.oplus.engineermode/.aftersale.AfterSalePage", true)
    }
}
/**
 * 跳转充电测试
 */
fun jumpBatteryInfo(context: Context) {
    if (context.checkPackName("com.oppo.engineermode")) {
        ShellUtils.execCommand(
            "am start -n com.oppo.engineermode/.charge.modeltest.BatteryInfoShow",
            true
        )
    }else if (context.checkPackName("com.oplus.engineermode")) {
        ShellUtils.execCommand(
            "am start -n com.oplus.engineermode/.charge.modeltest.BatteryInfoShow",
            true
        )
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
    if (context.checkResolveActivity(isoppoRunning)) {
        ShellUtils.execCommand(
            "am start -n com.android.settings/com.coloros.settings.feature.process.RunningApplicationActivity",
            true
        )
    } else if (context.checkResolveActivity(isoplusRunning)) {
        ShellUtils.execCommand(
            "am start -n com.android.settings/com.oplus.settings.feature.process.RunningApplicationActivity",
            true
        )
    }
}

/**
 * 显示/隐藏APP图标
 */
fun Context.setDesktopIcon(value : Boolean){
    packageManager.setComponentEnabledSetting(ComponentName(APPLICATION_ID, "${APPLICATION_ID}.Hide"),
        if (value) PackageManager.COMPONENT_ENABLED_STATE_DISABLED else PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
        PackageManager.DONT_KILL_APP
    )
}

fun getFlashInfo(): String = safeOf(default = "null"){
    val info = ShellUtils.execCommand("cat /sys/class/block/sda/device/inquiry", true, true).successMsg
    val pattern = Pattern.compile("\\p{L}")
    val matcher = pattern.matcher(info)
    if (!matcher.find()) return@safeOf "null"
    return info.substring(matcher.start())
}

/**
 * 正常编码中一般只会用到 [dp]/[sp] ;
 * 其中[dp]/[sp] 会根据系统分辨率将输入的dp/sp值转换为对应的px
 */
val Float.dp: Float // [xxhdpi](360 -> 1080)
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

val Int.dp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()


val Float.sp: Float // [xxhdpi](360 -> 1080)
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )


val Int.sp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

/**
 * 复制到剪贴板
 */
fun Context.copyStr(string: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(null, string)
    clipboard.setPrimaryClip(clipData)
}

/**
 * Base64转Bitmap图片
 */
fun baseDecode(code: String): Bitmap? {
    val decode: ByteArray = Base64.decode(code.split(",")[1], Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decode, 0, decode.size)
}

/**
 * 返回MaterialDialog Title居中样式
 */
val dialogCentered get() = com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered

/**
 * 判断是否显示Preference图标
 */
fun Context.getXPIcon(resource: Any?,result:(Drawable?,Boolean) -> Unit) {
    if (getBoolean(SettingsPrefs,"hide_xp_page_icon",false)){
        result(null,false)
        return
    }
    when(resource){
        is Int -> result(getDrawable(resource),true)
        is Drawable -> result(resource,true)
        is String -> result(getAppIcon(resource),true)
    }
}