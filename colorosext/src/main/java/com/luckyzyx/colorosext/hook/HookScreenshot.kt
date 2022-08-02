package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.apps.screenshot.RemoveScreenshotPrivacyLimit
import com.luckyzyx.colorosext.utils.XposedPrefs

class HookScreenshot : YukiBaseHooker() {
    override fun onHook() {
        //移除截屏隐私限制
        if (prefs(XposedPrefs).getBoolean("remove_screenshot_privacy_limit",false)) loadHooker(
            RemoveScreenshotPrivacyLimit()
        )
    }
}