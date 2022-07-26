package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.camera.RemoveWatermarkWordLimit

class HookCamera : YukiBaseHooker() {
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        //移除水印字数限制
        if (prefs(prefsFile).getBoolean("remove_watermark_word_limit",false)) loadHooker(
            RemoveWatermarkWordLimit()
        )
    }
}