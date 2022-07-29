package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.camera.RemoveWatermarkWordLimit
import com.luckyzyx.colorosext.utils.XposedPrefs

class HookCamera : YukiBaseHooker() {
    override fun onHook() {
        //移除水印字数限制
        if (prefs(XposedPrefs).getBoolean("remove_watermark_word_limit",false)) loadHooker(
            RemoveWatermarkWordLimit()
        )
    }
}