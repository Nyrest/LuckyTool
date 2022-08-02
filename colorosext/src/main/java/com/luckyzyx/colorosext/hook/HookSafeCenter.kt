package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.apps.safecenter.UnlockStartupLimit
import com.luckyzyx.colorosext.utils.XposedPrefs

class HookSafeCenter : YukiBaseHooker() {
    override fun onHook() {
        //解锁自启数量限制
        if (prefs(XposedPrefs).getBoolean("unlock_startup_limit",false)) loadHooker(UnlockStartupLimit())

    }
}