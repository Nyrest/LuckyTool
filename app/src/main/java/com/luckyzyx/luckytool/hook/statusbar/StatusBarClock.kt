package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveStatusBarClockRedOne
import com.luckyzyx.luckytool.hook.apps.systemui.StatusBarClock
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class StatusBarClock : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui"){
            //状态栏时钟
            if (prefs.getBoolean("statusbar_clock_enable",false)) loadHooker(StatusBarClock())

            //下拉状态栏时钟红一
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_clock_redone",false)) loadHooker(
                RemoveStatusBarClockRedOne()
            )
        }
    }
}