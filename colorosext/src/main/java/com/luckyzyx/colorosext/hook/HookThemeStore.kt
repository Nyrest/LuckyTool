package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.tools.hook.themestore.UnlockThemeStoreVip

class HookThemeStore : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //解锁主题商店VIP
        if (prefs(PrefsFile).getBoolean("unlock_themestore_vip",false)) loadHooker(UnlockThemeStoreVip())
    }
}