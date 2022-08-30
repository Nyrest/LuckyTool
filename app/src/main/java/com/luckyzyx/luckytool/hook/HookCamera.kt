package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.camera.RemoveWatermarkWordLimit
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class HookCamera : YukiBaseHooker() {
    override fun onHook() {
        //移除水印字数限制
        if (prefs(XposedPrefs).getBoolean("remove_watermark_word_limit",false)) {
            loadHooker(RemoveWatermarkWordLimit())
        }
    }
}