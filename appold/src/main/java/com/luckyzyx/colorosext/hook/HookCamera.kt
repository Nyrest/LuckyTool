package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.tools.hook.camera.RemoveWatermarkWordLimit

class HookCamera : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //移除水印字数限制
        if (prefs(PrefsFile).getBoolean("remove_watermark_word_limit",false)) loadHooker(RemoveWatermarkWordLimit())
    }
}