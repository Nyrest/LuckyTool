package com.luckyzyx.luckytool.hook.apps.alarmclock

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType

class RemoveAlarmClockWidgetRedOne : YukiBaseHooker() {
    override fun onHook() {
        //Search -> OnePlusWidget -> CharSequence field
        "com.coloros.widget.smallweather.OnePlusWidget".toClass().field {
            type(CharSequenceType).index().first()
        }.get().set("")
    }
}