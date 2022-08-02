package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.apps.android.DisableFlagSecure
import com.luckyzyx.colorosext.hook.apps.android.RemoveStatusBarTopNotification

class HookAndroid : YukiBaseHooker() {

    override fun onHook() {
        //禁用FLAG_SECURE
        loadHooker(DisableFlagSecure())
        //移除状态栏上层警告
        loadHooker(RemoveStatusBarTopNotification())
    }
}
