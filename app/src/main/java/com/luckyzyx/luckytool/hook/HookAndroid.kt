package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.android.DisableFlagSecure
import com.luckyzyx.luckytool.hook.apps.android.RemoveStatusBarTopNotification
import com.luckyzyx.luckytool.hook.apps.android.RemoveSystemScreenshotDelay
import com.luckyzyx.luckytool.hook.apps.android.RemoveVPNActiveNotification
import com.luckyzyx.luckytool.utils.XposedPrefs

class HookAndroid : YukiBaseHooker() {

    override fun onHook() {
        //禁用FLAG_SECURE
        loadHooker(DisableFlagSecure())

        //移除状态栏上层警告
        loadHooker(RemoveStatusBarTopNotification())

        //移除系统截屏延迟
        loadHooker(RemoveSystemScreenshotDelay())

        //移除VPN已激活通知
        loadHooker(RemoveVPNActiveNotification())
    }
}
