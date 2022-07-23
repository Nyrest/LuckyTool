package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.tools.hook.safecenter.UnlockStartupLimit

class HookSafeCenter : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //解锁自启数量限制
        if (prefs(PrefsFile).getBoolean("unlock_startup_limit",false)) loadHooker(UnlockStartupLimit())

    }
}