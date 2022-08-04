package com.luckyzyx.luckytool.hook

import android.graphics.Color
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.LockScreenTextViewColor
import com.luckyzyx.luckytool.hook.apps.systemui.NetworkSpeed
import com.luckyzyx.luckytool.hook.apps.systemui.*
import com.luckyzyx.luckytool.utils.XposedPrefs

class HookSystemUI : YukiBaseHooker(){
    override fun onHook() {
        //状态栏时钟相关
        //设置状态栏时钟显秒和上下午
        loadHooker(StatusBarClock())
        //移除下拉状态栏时钟红1
        if (prefs(XposedPrefs).getBoolean("remove_statusbar_clock_redone",false)) loadHooker(RemoveStatusBarClockRedOne())
        //设置下拉状态栏时钟显秒
        if (prefs(XposedPrefs).getBoolean("statusbar_dropdown_clock_show_second",false)) loadHooker(StatusBarClockShowSecond())
        //移除锁屏时钟红1
        if (prefs(XposedPrefs).getBoolean("remove_lock_screen_redone",false)) loadHooker(RemoveLockScreenRedOne())

        //锁屏相关
        //设置锁屏组件文本颜色
        if (prefs(XposedPrefs).getInt("set_lock_screen_textview_color",Color.BLACK) != Color.BLACK) loadHooker(LockScreenTextViewColor())
        //移除锁屏右下角相机
        if (prefs(XposedPrefs).getBoolean("remove_lock_screen_camera",false)) loadHooker(RemoveLockScreenCamera())

        //状态栏相关
        //设置状态栏网速刷新率
        if (prefs(XposedPrefs).getBoolean("set_network_speed",false)) loadHooker(NetworkSpeed())

        //移除充电完成通知
        if (prefs(XposedPrefs).getBoolean("remove_charging_completed",false)) loadHooker(RemoveChargingCompleted())
        //移除状态栏开发者选项警告
        if (prefs(XposedPrefs).getBoolean("remove_statusbar_devmode",false)) loadHooker(RemoveStatusBarDevMode())

        //移除下拉状态栏底部网络警告
        if (prefs(XposedPrefs).getBoolean("remove_statusbar_bottom_networkwarn",false)) loadHooker(RemoveStatusBarBottomNetworkWarn())
        //移除状态栏支付保护图标
        if (prefs(XposedPrefs).getBoolean("remove_statusbar_securepayment_icon",false)) loadHooker(RemoveStatusBarSecurePayment())

    }
}
