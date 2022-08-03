package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.android.DisableFlagSecure
import com.luckyzyx.luckytool.hook.apps.android.RemoveStatusBarTopNotification

class HookAndroid : YukiBaseHooker() {

    override fun onHook() {
        //禁用FLAG_SECURE
        loadHooker(DisableFlagSecure())
        //移除状态栏上层警告
        loadHooker(RemoveStatusBarTopNotification())
    }
}
