package com.luckyzyx.luckytool.hook.apps

import android.os.Build.VERSION.SDK_INT
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.*
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class Miscellaneous : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui"){
            //禁用复制悬浮窗
            if (prefs(XposedPrefs).getBoolean("disable_duplicate_floating_window",false)){
                if (SDK_INT >= 33) loadHooker(DisableDuplicateFloatingWindow())
            }

            //充电纹波
            if (prefs(XposedPrefs).getBoolean("show_charging_ripple",false)) {
                if (SDK_INT >= 31) loadHooker(ShowChargingRipple())
            }

            //禁用耳机高音量警告
            if (prefs(XposedPrefs).getBoolean("disable_headphone_high_volume_warning",false)){
                loadHooker(DisableHeadphoneHighVolumeWarning())
            }

            //电源菜单相关
            if (prefs(XposedPrefs).getBoolean("power_menu_enable",false)){
                if(SDK_INT in 31..32) loadHooker(PowerMenu())
            }
        }
    }
}