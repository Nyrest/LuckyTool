package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.alarmclock.RemoveAlarmClockWidgetRedOne
import com.luckyzyx.colorosext.utils.XposedPrefs

class HookAlarmClock : YukiBaseHooker() {
    override fun onHook() {
        //移除桌面时钟组件红一
        if (prefs(XposedPrefs).getBoolean("remove_alarmclock_widget_redone",false)) loadHooker(RemoveAlarmClockWidgetRedOne())
    }
}