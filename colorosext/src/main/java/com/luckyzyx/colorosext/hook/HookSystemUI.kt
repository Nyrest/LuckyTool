package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.systemui.LockScreenTextViewColor
import com.luckyzyx.colorosext.hook.systemui.NetworkSpeed
import com.luckyzyx.colorosext.hook.systemui.*

class HookSystemUI : YukiBaseHooker(){
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        //移除锁屏时钟红1
        if (prefs(prefsFile).getBoolean("remove_lock_screen_redone",false)) loadHooker(RemoveLockScreenRedOne())

        //设置锁屏组件文本颜色
        if (prefs(prefsFile).getString("set_lock_screen_textview_color","") != "") loadHooker(
            LockScreenTextViewColor()
        )

        //移除锁屏右下角相机
        if (prefs(prefsFile).getBoolean("remove_lock_screen_camera",false)) loadHooker(RemoveLockScreenCamera())

        //设置状态栏网速刷新率
        if (prefs(prefsFile).getBoolean("network_speed",false)) loadHooker(NetworkSpeed())

        //移除充电完成通知
        if (prefs(prefsFile).getBoolean("remove_charging_completed",false)) loadHooker(RemoveChargingCompleted())

        //移除下拉状态栏时钟红1
        if (prefs(prefsFile).getBoolean("remove_statusbar_clock_redone",false)) loadHooker(RemoveStatusBarClockRedOne())

        //设置下拉状态栏时钟显秒
        if (prefs(prefsFile).getBoolean("statusbar_clock_show_second",false)) loadHooker(StatusBarClockShowSecond())

        //设置状态栏时钟显示上午下午
        if (prefs(prefsFile).getString("statusbar_clock_show_ampm","2") != "2") loadHooker(StatusBarClockShowAMPM())

        //移除状态栏开发者选项警告
        if (prefs(prefsFile).getBoolean("remove_statusbar_devmode",false)) loadHooker(RemoveStatusBarDevMode())

        //移除下拉状态栏底部网络警告
        if (prefs(prefsFile).getBoolean("remove_statusbar_bottom_networkwarn",false)) loadHooker(RemoveStatusBarBottomNetworkWarn())

        //移除状态栏支付保护图标
        if (prefs(prefsFile).getBoolean("remove_statusbar_securepayment_icon",false)) loadHooker(RemoveStatusBarSecurePayment())

    }
}
