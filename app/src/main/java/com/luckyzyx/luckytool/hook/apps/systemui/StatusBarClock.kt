package com.luckyzyx.luckytool.hook.apps.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.TypefaceClass
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType
import com.luckyzyx.luckytool.utils.tools.A11
import com.luckyzyx.luckytool.utils.tools.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import com.luckyzyx.luckytool.utils.tools.getColorOSVersion
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*

class StatusBarClock : YukiBaseHooker() {

    private val isYear = prefs(XposedPrefs).getBoolean("statusbar_clock_show_year", false)
    private val isMonth = prefs(XposedPrefs).getBoolean("statusbar_clock_show_month", false)
    private val isDay = prefs(XposedPrefs).getBoolean("statusbar_clock_show_day", false)
    private val isWeek = prefs(XposedPrefs).getBoolean("statusbar_clock_show_week", false)
    private val isPeriod = prefs(XposedPrefs).getBoolean("statusbar_clock_show_period", false)
    private val isDoubleHour = prefs(XposedPrefs).getBoolean("statusbar_clock_show_double_hour", false)
    private val isSecond = prefs(XposedPrefs).getBoolean("statusbar_clock_show_second", false)
    private val isHideSpace = prefs(XposedPrefs).getBoolean("statusbar_clock_hide_spaces", false)
    private val isDoubleRow = prefs(XposedPrefs).getBoolean("statusbar_clock_show_doublerow", false)

    private val singleRowFontSize = prefs(XposedPrefs).getInt("statusbar_clock_singlerow_fontsize", 0)
    private val doubleRowFontSize = prefs(XposedPrefs).getInt("statusbar_clock_doublerow_fontsize", 0)

    private var nowTime: Date? = null
    private var newline = ""

    override fun onHook() {
        var context: Context? = null
        findClass("com.android.systemui.statusbar.policy.Clock").hook {
            injectMember {
                constructor {
                    paramCount = 3
                }
                afterHook {
                    context = args(0).cast<Context>()
                    val clockView = instance<TextView>()
                    clockView.apply {
                        if (this.resources.getResourceEntryName(id) != "clock") return@afterHook
                        isSingleLine = false
                        gravity = Gravity.CENTER
                        if (isDoubleRow) {
                            newline = "\n"
                            var defaultSize = 8F
                            if (doubleRowFontSize != 0) defaultSize = doubleRowFontSize.toFloat()
                            setTextSize(TypedValue.COMPLEX_UNIT_DIP, defaultSize)
                            setLineSpacing(0F, 0.8F)
                        } else {
                            if (singleRowFontSize != 0) {
                                setTextSize(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    singleRowFontSize.toFloat()
                                )
                            }
                        }
                    }

                    val d: Method = clockView.javaClass.superclass.getDeclaredMethod("updateClock")
                    val r = Runnable {
                        d.isAccessible = true
                        d.invoke(clockView)
                    }

                    class T : TimerTask() {
                        override fun run() {
                            Handler(clockView.context.mainLooper).post(r)
                        }
                    }
                    Timer().scheduleAtFixedRate(T(), 1000 - System.currentTimeMillis() % 1000, 1000)
                }
            }
            injectMember {
                method {
                    name = "getSmallTime"
                    returnType = CharSequenceType
                }
                afterHook {
                    instance<TextView>().apply {
                        if (this.resources.getResourceEntryName(id) != "clock") return@afterHook
                    }
                    nowTime = Calendar.getInstance().time
                    result = getDate(context!!) + newline + getTime(context!!)
                }
            }
            if (SDK == A11 && getColorOSVersion == "V12") {
                injectMember {
                    method {
                        name = "updateShowSeconds"
                    }
                    beforeHook {
                        field {
                            name = "mShowSeconds"
                        }.get(instance).setTrue()
                    }
                }
            }
        }

        findClass("com.oplusos.systemui.statusbar.widget.StatClock").hook {
            injectMember {
                method {
                    if (SDK == A11) {
                        name = "onConfigChanged"
                    }
                    if (SDK > A11) name = "onConfigurationChanged"
                }
                if (isDoubleRow && doubleRowFontSize != 0) {
                    replaceUnit {
                        instance<TextView>().apply {
                            typeface = field {
                                name = "defaultFont"
                                type = TypefaceClass
                            }.get(instance).cast<Typeface>()
                        }
                    }
                } else if (!isDoubleRow && singleRowFontSize != 0) {
                    replaceUnit {
                        instance<TextView>().apply {
                            typeface = field {
                                name = "defaultFont"
                                type = TypefaceClass
                            }.get(instance).cast<Typeface>()
                        }
                    }
                }
            }
        }
    }

    private fun getDate(context: Context): String {
        var dateFormat = ""
        if (isZh(context)) {
            if (isYear) dateFormat += "YY年"
            if (isMonth) dateFormat += "M月"
            if (isDay) dateFormat += "d日"
            if (isWeek) dateFormat += "E"
            if (!isHideSpace && !isDoubleRow) dateFormat += " "
        } else {
            if (isYear) {
                dateFormat += "YY"
                if (isMonth || isDay) dateFormat += "/"
            }
            if (isMonth) {
                dateFormat += "M"
                if (isDay) dateFormat += "/"
            }
            if (isDay) dateFormat += "d"
            if (!isHideSpace && !isDoubleRow) dateFormat += " "
            if (isWeek) dateFormat += "E"
            if (!isHideSpace && !isDoubleRow) dateFormat += " "
        }
        return SimpleDateFormat(dateFormat).format(nowTime!!)
    }

    private fun getTime(context: Context): String {
        var timeFormat = ""
        timeFormat += if (is24(context)) "HH:mm" else "hh:mm"
        if (isSecond) timeFormat += ":ss"
        timeFormat = SimpleDateFormat(timeFormat).format(nowTime!!)
        if (isZh(context)) timeFormat =
            getPeriod(context) + timeFormat else timeFormat += getPeriod(context)
        timeFormat = getDoubleHour() + timeFormat
        return timeFormat
    }

    private fun getPeriod(context: Context): String {
        var period = ""
        if (isPeriod) {
            if (isZh(context)) {
                when (SimpleDateFormat("HH").format(nowTime!!)) {
                    "00", "01", "02", "03", "04", "05" -> {
                        period = "凌晨"
                    }
                    "06", "07", "08", "09", "10", "11" -> {
                        period = "上午"
                    }
                    "12" -> {
                        period = "中午"
                    }
                    "13", "14", "15", "16", "17" -> {
                        period = "下午"
                    }
                    "18" -> {
                        period = "傍晚"
                    }
                    "19", "20", "21", "22", "23" -> {
                        period = "晚上"
                    }
                }
                if (!isHideSpace) period += " "
            } else {
                period = " " + SimpleDateFormat("a").format(nowTime!!)
            }
        }
        return period
    }

    @SuppressLint("SimpleDateFormat")
    fun getDoubleHour(): String {
        var doubleHour = ""
        if (isDoubleHour) {
            when (SimpleDateFormat("HH").format(nowTime!!)) {
                "23", "00" -> {
                    doubleHour = "子时"
                }
                "01", "02" -> {
                    doubleHour = "丑时"
                }
                "03", "04" -> {
                    doubleHour = "寅时"
                }
                "05", "06" -> {
                    doubleHour = "卯时"
                }
                "07", "08" -> {
                    doubleHour = "辰时"
                }
                "09", "10" -> {
                    doubleHour = "巳时"
                }
                "11", "12" -> {
                    doubleHour = "午时"
                }
                "13", "14" -> {
                    doubleHour = "未时"
                }
                "15", "16" -> {
                    doubleHour = "申时"
                }
                "17", "18" -> {
                    doubleHour = "酉时"
                }
                "19", "20" -> {
                    doubleHour = "戌时"
                }
                "21", "22" -> {
                    doubleHour = "亥时"
                }
            }
            if (!isHideSpace) doubleHour = "$doubleHour "
        }
        return doubleHour
    }

    private fun isZh(context: Context): Boolean {
        val locale = context.resources.configuration.locales[0]
        val language = locale.language
        return language.endsWith("zh")
    }

    private fun is24(context: Context): Boolean {
        val t = Settings.System.getString(context.contentResolver, Settings.System.TIME_12_24)
        return t == "24"
    }
}