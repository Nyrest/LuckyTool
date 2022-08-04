package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.android.DisableFlagSecureZygote
import com.luckyzyx.luckytool.utils.XposedPrefs

class HookZygote : YukiBaseHooker() {
    override fun onHook() {
        //禁用FLAG_SECURE
        if (prefs(XposedPrefs).getBoolean("disable_flag_secure",false)) {
            loadHooker(DisableFlagSecureZygote())
        }
    }
}