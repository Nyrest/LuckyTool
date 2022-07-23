package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.tools.hook.alarmclock.RemoveAlarmClockWidgetRedOne

class HookAlarmClock : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //移除桌面时钟组件红一
        if (prefs(PrefsFile).getBoolean("remove_alarmclock_widget_redone",false)) loadHooker(RemoveAlarmClockWidgetRedOne())
    }
}