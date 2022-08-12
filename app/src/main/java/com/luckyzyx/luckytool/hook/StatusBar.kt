package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.*
import com.luckyzyx.luckytool.utils.XposedPrefs

class StatusBar : YukiBaseHooker() {
    override fun onHook() {
        //状态栏通知
        loadApp("com.android.systemui") {
            //移除充电完成通知
            if (prefs(XposedPrefs).getBoolean("remove_charging_completed", false)) loadHooker(
                RemoveChargingCompleted()
            )
            //移除状态栏开发者选项警告
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_devmode", false)) loadHooker(
                RemoveStatusBarDevMode()
            )

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

            //状态栏磁贴
            //状态栏磁贴列数
            if (prefs(XposedPrefs).getString("set_statusbar_tiles_column", "") != "") loadHooker(
                StatusBarTilesColumn()
            )
            //移除磁贴底部网络警告
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_bottom_networkwarn", false)) loadHooker(
                RemoveStatusBarBottomNetworkWarn()
            )
        }
    }
}