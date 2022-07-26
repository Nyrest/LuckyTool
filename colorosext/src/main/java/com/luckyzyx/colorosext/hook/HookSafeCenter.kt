package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.safecenter.UnlockStartupLimit

class HookSafeCenter : YukiBaseHooker() {
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        //解锁自启数量限制
        if (prefs(prefsFile).getBoolean("unlock_startup_limit",false)) loadHooker(UnlockStartupLimit())

    }
}