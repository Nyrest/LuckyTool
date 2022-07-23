package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.tools.hook.android.DisableFlagSecure
import com.luckyzyx.tools.hook.android.RemoveStatusBarTopNotification

class HookAndroid : YukiBaseHooker(){
    @Suppress("unused")
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //禁用FLAG_SECURE
        loadHooker(DisableFlagSecure())
        //移除状态栏上层警告
        loadHooker(RemoveStatusBarTopNotification())
    }
}
