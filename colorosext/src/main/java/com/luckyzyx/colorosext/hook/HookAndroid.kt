package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.android.DisableFlagSecure
import com.luckyzyx.colorosext.hook.android.RemoveStatusBarTopNotification

class HookAndroid : YukiBaseHooker(){
    @Suppress("unused")
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        //禁用FLAG_SECURE
        loadHooker(DisableFlagSecure())
        //移除状态栏上层警告
        loadHooker(RemoveStatusBarTopNotification())
    }
}
