package com.luckyzyx.luckytool.hook.apps.alarmclock

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.HandlerClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class RemoveAlarmClockWidgetRedOne : YukiBaseHooker() {
    override fun onHook() {
        //Source OnePlusWidget
        //Search CharSequence Field
        "com.coloros.widget.smallweather.OnePlusWidget".toClass().field {
            type(CharSequenceType).index().first()
        }.get().set("")

        if (!prefs(XposedPrefs).getBoolean("remove_alarmclock_widget_redone_pro", false)) return
        //Source update one plus clock +4 -> setTextViewText
        searchClass {
            from("m0").absolute()
    //            field {
    //                type = CharSequenceType
    //            }.count(1)
            field {
                type = HandlerClass
            }.count(1)
            field {
                type = BooleanType
            }.count(3)
    //            method {
    //                param(ContextClass, StringType, StringType)
    //                returnType = CharSequenceType
    //            }.count(1)
    //            method {
    //                param(ContextClass, StringType)
    //                returnType = CharSequenceType
    //            }.count(1)
        }.get().takeIf { it != null }?.field {
            type(CharSequenceType).index().first()
        }?.get()?.set("") ?: loggerD(msg = "$packageName\nError -> RemoveAlarmClockWidgetRedOne")
    }
}






