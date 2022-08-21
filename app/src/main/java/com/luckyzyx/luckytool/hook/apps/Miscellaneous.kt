package com.luckyzyx.luckytool.hook.apps

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.DisableDuplicateFloatingWindow
import com.luckyzyx.luckytool.hook.apps.systemui.ShowChargingRipple
import com.luckyzyx.luckytool.utils.XposedPrefs

class Miscellaneous : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui"){
            //禁用复制悬浮窗
            if (prefs(XposedPrefs).getBoolean("disable_duplicate_floating_window",false)){
                if (Build.VERSION.SDK_INT < 33) return
                loadHooker(DisableDuplicateFloatingWindow())
            }

            //充电纹波
            if (prefs(XposedPrefs).getBoolean("show_charging_ripple",false)) loadHooker(
                ShowChargingRipple()
            )
        }
    }
}