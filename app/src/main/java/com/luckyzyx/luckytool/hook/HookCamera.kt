package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.camera.Enable10BitImageSupport
import com.luckyzyx.luckytool.hook.apps.camera.RemoveWatermarkWordLimit
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class HookCamera : YukiBaseHooker() {
    override fun onHook() {
        //移除水印字数限制
        if (prefs(XposedPrefs).getBoolean("remove_watermark_word_limit",false)) {
            loadHooker(RemoveWatermarkWordLimit())
        }

        //10亿色影像支持
        if (prefs(XposedPrefs).getBoolean("enable_10_bit_image_support",false)) {
            loadHooker(Enable10BitImageSupport())
        }
    }
}