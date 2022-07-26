package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.themestore.UnlockThemeStoreVip

class HookThemeStore : YukiBaseHooker() {
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        //解锁主题商店VIP
        if (prefs(prefsFile).getBoolean("unlock_themestore_vip",false)) loadHooker(
            UnlockThemeStoreVip()
        )
    }
}