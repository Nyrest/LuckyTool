package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.*
import com.luckyzyx.luckytool.utils.XposedPrefs

class StatusBarIcon : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //状态栏图标
            //设置状态栏网速刷新率
            if (prefs(XposedPrefs).getBoolean("set_network_speed", false)) loadHooker(NetworkSpeed())
            //移除状态栏电量百分号
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_battery_percent", false)) loadHooker(
                RemoveStatusBarBatteryPercent()
            )
            //移除状态栏支付保护图标
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_securepayment_icon", false)) loadHooker(
                RemoveStatusBarSecurePayment()
            )
            //移除下拉状态栏多用户图标
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_user_switcher", false)) loadHooker(
                RemoveStatusBarUserSwitcher()
            )
        }
    }
}