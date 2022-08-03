package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.alarmclock.RemoveAlarmClockWidgetRedOne
import com.luckyzyx.luckytool.utils.XposedPrefs

class HookAlarmClock : YukiBaseHooker() {
    override fun onHook() {
        //移除桌面时钟组件红一
        if (prefs(XposedPrefs).getBoolean("remove_alarmclock_widget_redone",false)) loadHooker(RemoveAlarmClockWidgetRedOne())
    }
}