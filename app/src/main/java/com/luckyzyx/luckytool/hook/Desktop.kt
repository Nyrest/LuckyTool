package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.alarmclock.RemoveAlarmClockWidgetRedOne
import com.luckyzyx.luckytool.hook.apps.launcher.LauncherLayoutRowColume
import com.luckyzyx.luckytool.hook.apps.launcher.RemoveAppUpdateDot
import com.luckyzyx.luckytool.utils.XposedPrefs

class Desktop : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.coloros.alarmclock"){
            //移除桌面时钟组件红一
            if (prefs(XposedPrefs).getBoolean("remove_alarmclock_widget_redone",false)) loadHooker(
                RemoveAlarmClockWidgetRedOne()
            )
        }

        loadApp("com.android.launcher"){
            //移除APP更新圆点
            if (prefs(XposedPrefs).getBoolean("remove_appicon_dot",false)) loadHooker(
                RemoveAppUpdateDot()
            )
            //设置桌面布局行和列
            if (prefs(XposedPrefs).getBoolean("launcher_layout_enable",false)) loadHooker(
                LauncherLayoutRowColume()
            )
        }
    }
}