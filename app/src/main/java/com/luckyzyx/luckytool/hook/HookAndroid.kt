package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.android.DisableFlagSecure
import com.luckyzyx.luckytool.hook.apps.android.RemoveStatusBarTopNotification
import com.luckyzyx.luckytool.utils.XposedPrefs

class HookAndroid : YukiBaseHooker() {

    override fun onHook() {
        //禁用FLAG_SECURE
        if (prefs(XposedPrefs).getBoolean("disable_flag_secure",false)) {
            loadHooker(DisableFlagSecure())
        }

        //移除状态栏上层警告
        if (prefs(XposedPrefs).getBoolean("remove_statusbar_top_notification", false)) {
            loadHooker(RemoveStatusBarTopNotification())
        }
    }
}
