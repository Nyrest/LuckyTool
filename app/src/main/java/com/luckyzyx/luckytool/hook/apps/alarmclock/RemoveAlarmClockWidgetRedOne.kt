package com.luckyzyx.luckytool.hook.apps.alarmclock

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType

class RemoveAlarmClockWidgetRedOne : YukiBaseHooker() {
    override fun onHook() {
        //Search -> OnePlusWidget -> CharSequence field
        "com.coloros.widget.smallweather.OnePlusWidget".clazz.field {
            name {
                equalsOf(other = "Sb",isIgnoreCase = false)
                equalsOf(other = "Sc",isIgnoreCase = false)
                equalsOf(other = "TR",isIgnoreCase = false)
                equalsOf(other = "TT",isIgnoreCase = false)
                equalsOf(other = "Tj",isIgnoreCase = false)
            }
            type = CharSequenceType
        }.get().set("")
    }
}