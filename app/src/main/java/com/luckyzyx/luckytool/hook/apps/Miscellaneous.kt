package com.luckyzyx.luckytool.hook.apps

import android.os.Build.VERSION.SDK_INT
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.DisableDuplicateFloatingWindow
import com.luckyzyx.luckytool.hook.apps.systemui.DisableHeadphoneHighVolumeWarning
import com.luckyzyx.luckytool.hook.apps.systemui.ShowChargingRipple
import com.luckyzyx.luckytool.utils.XposedPrefs

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
        }
    }
}