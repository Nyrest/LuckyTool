package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.screenshot.RemoveScreenshotPrivacyLimit
import com.luckyzyx.luckytool.utils.XposedPrefs

class Screenshot : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.oplus.screenshot"){
            //移除截屏隐私限制
            if (prefs(XposedPrefs).getBoolean("remove_screenshot_privacy_limit",false)) loadHooker(
                RemoveScreenshotPrivacyLimit()
            )
        }
    }
}