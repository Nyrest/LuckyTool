package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveStatusBarClockRedOne
import com.luckyzyx.luckytool.hook.apps.systemui.StatusBarClockShowSecond
import com.luckyzyx.luckytool.hook.apps.systemui.TopStatusBarClock
import com.luckyzyx.luckytool.utils.XposedPrefs

class StatusBarClock : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui"){
            //顶部状态栏时钟
            if (prefs.getBoolean("statusbar_clock_enable",false)) loadHooker(TopStatusBarClock())

            //下拉状态栏时钟
            //移除下拉状态栏时钟红1
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_clock_redone",false)) loadHooker(
                RemoveStatusBarClockRedOne()
            )
            //设置下拉状态栏时钟显秒
            if (prefs(XposedPrefs).getBoolean("statusbar_dropdown_clock_show_second",false)) loadHooker(
                StatusBarClockShowSecond()
            )
        }
    }
}