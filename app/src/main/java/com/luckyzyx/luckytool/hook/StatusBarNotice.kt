package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.*
import com.luckyzyx.luckytool.utils.XposedPrefs

class StatusBarNotice : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //状态栏通知
            //移除充电完成通知
            if (prefs(XposedPrefs).getBoolean("remove_charging_completed", false)) loadHooker(
                RemoveChargingCompleted()
            )
            //移除状态栏开发者选项警告
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_devmode", false)) loadHooker(
                RemoveStatusBarDevMode()
            )
            //移除磁贴底部网络警告
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_bottom_networkwarn", false)) loadHooker(
                RemoveStatusBarBottomNetworkWarn()
            )
        }
    }
}