package com.luckyzyx.luckytool.hook.apps.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.XposedPrefs
import java.text.SimpleDateFormat
import java.util.*

class StatusBarClock : YukiBaseHooker() {

    //显示年份
    private val isYear = false
    //月份
    private val isMonth = false
    //日期
    private val isDay = false
    //星期
    private val isWeek = false
    //隐藏间隔
    private val isHideSpace = false
    //双排显示
    private val isDoubleLine = false
    //显秒
    private val isSecond = prefs(XposedPrefs).getBoolean("statusbar_clock_show_second",false)
    //时辰
    private val isDoubleHour = false
    //时段  上下午
    private val isPeriod = prefs(XposedPrefs).getBoolean("statusbar_clock_show_period",false)
    //时钟大小
    private val getClockSize = 0
    //双排居中对齐
    private val isCenterAlign = false
    //双排大小
    private val getClockDoubleSize = 0
    //现在时间
    private var nowTime: Date? = null
    //换行字符串
    private var str = ""

    override fun onHook() {
        var context: Context? = null
        findClass("com.android.systemui.statusbar.policy.Clock").hook {
            injectMember {
                constructor {
                    paramCount = 3
                }
                afterHook {
                    //获取Context
                    context = args[0] as Context
                    val textV = instance as TextView
                    if (textV.resources.getResourceEntryName(textV.id) != "clock") return@afterHook
                    //true，则将此字段的属性（行数、水平滚动、转换方法）设置为单行输入；如果为 false，则将这些恢复为默认条件
                    textV.isSingleLine = false

                    //是否双牌显示
                    if (isDoubleLine) {
                        str = "\n"
                        var clockDoubleLineSize = 7F
                        if (getClockDoubleSize != 0) {
                            clockDoubleLineSize = getClockDoubleSize.toFloat()
                        }
                        textV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, clockDoubleLineSize)
                        textV.setLineSpacing(0F, 0.8F)
                    } else {
                        //获取时钟size
                        if (getClockSize != 0) {
                            val clockSize = getClockSize.toFloat()
                            textV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, clockSize)
                        }
                    }
                    //是否双排居中对齐
                    if (isCenterAlign) textV.gravity = Gravity.CENTER

//                    //获取一个方法
//                    val updateClock: Method = textV.javaClass.superclass.getDeclaredMethod("updateClock")
//
//                    val run = Runnable {
//                        //值为true表示反射对象在使用时应禁止 Java 语言访问检查。值false表示反射对象应该强制执行 Java 语言访问检查。
//                        updateClock.isAccessible = true
//                        //在具有指定参数的指定对象上调用此Method对象表示的基础方法
//                        updateClock.invoke(textV)
//                    }
//                    //计时器
//                    class T : TimerTask() {
//                        override fun run() {
//                            android.os.Handler(textV.context.mainLooper).post(run)
//                        }
//                    }
//                    Timer().scheduleAtFixedRate(T(), 1000 - System.currentTimeMillis() % 1000, 1000)
//
                }
            }
        }
        findClass("com.android.systemui.statusbar.policy.Clock").hook {
            injectMember {
                method {
                    name = "getSmallTime"
                }
                afterHook {
                    val textV = instance as TextView
                    if (textV.resources.getResourceEntryName(textV.id) != "clock") return@afterHook
                    //获取系统是否24小时制
                    val t = Settings.System.getString(context!!.contentResolver, Settings.System.TIME_12_24)
                    val is24 = t == "24"
                    //获取当前Time

                    nowTime = Calendar.getInstance().time
//                    loggerD(msg = nowTime.toString())
//                    result = getDate(context!!) + str + getTime(context!!, is24)
                    result = getTime(context!!, is24)
                }
            }
        }

//        findClass("com.oplusos.systemui.statusbar.widget.StatClock").hook {
//            injectMember {
//                method {
//                    name = "onConfigurationChanged"
//                }
//                afterHook {
//                    val textV = instance as TextView
//                    if (textV.resources.getResourceEntryName(textV.id) != "clock") return@afterHook
//                    //双牌显示
//                    if (isDoubleLine) {
//                        var clockDoubleLineSize = 7F
//                        if (getClockDoubleSize != 0) {
//                            clockDoubleLineSize = getClockDoubleSize.toFloat()
//                        }
//                        textV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, clockDoubleLineSize)
//                        textV.setLineSpacing(0F, 0.8F)
//                    } else {
//                        if (getClockSize != 0) {
//                            val clockSize = getClockSize.toFloat()
//                            textV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, clockSize)
//                        }
//                    }
//                }
//            }
//        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(context: Context): String {
        var datePattern = ""
        val isZh = isZh(context)

        if (isYear) {
            if (isZh) {
                datePattern += "YY年"
//                if (!isHideSpace) datePattern = "$datePattern "
            } else {
                datePattern += "YY"
                if (isMonth || isDay) datePattern += "/"
            }
        }
        if (isMonth) {
            if (isZh) {
                datePattern += "M月"
//                if (!isHideSpace) datePattern = "$datePattern "
            } else {
                datePattern += "M"
                if (isDay) datePattern += "/"
            }
        }
        if (isDay) {
            datePattern += if (isZh) {
                "d日"
            } else {
                "d"
            }
        }
        if (isWeek) {
            if (!isHideSpace) datePattern = "$datePattern "
            datePattern += "E"
            if (!isDoubleLine) {
                if (!isHideSpace) datePattern = "$datePattern "
            }
        }
        datePattern = SimpleDateFormat(datePattern).format(nowTime!!)
        return datePattern
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(context: Context, is24: Boolean): String {
        var timePattern = ""
        //判断中文
        val isZh = isZh(context)

        timePattern += if (is24) "HH:mm" else "hh:mm"
        //显秒
        if (isSecond) timePattern += ":ss"
        timePattern = SimpleDateFormat(timePattern).format(nowTime!!)
        //判断中文
        if (isZh) timePattern = getPeriod(isZh) + timePattern else timePattern += getPeriod(isZh)
        timePattern = getDoubleHour() + timePattern
        return timePattern
    }

    @SuppressLint("SimpleDateFormat")
    fun getPeriod(isZh: Boolean): String {
        var period = ""
        //显示时段
        if (isPeriod) {
            if (isZh) {
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
            } else {
                //AM/PM 标记 English
                period = SimpleDateFormat("a").format(nowTime!!)
                if (!isHideSpace) {
                    period = " $period"
                }
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
            if (!isHideSpace) {
                doubleHour += " "
            }
        }
        return doubleHour
    }

    private fun isZh(context: Context): Boolean {
        val locale = context.resources.configuration.locales.get(0)
        val language = locale.language
        return language.endsWith("zh")
    }
}