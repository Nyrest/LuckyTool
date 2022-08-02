package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.apps.themestore.UnlockThemeStoreVip
import com.luckyzyx.colorosext.utils.XposedPrefs

class HookThemeStore : YukiBaseHooker() {
    override fun onHook() {
        //解锁主题商店VIP
        if (prefs(XposedPrefs).getBoolean("unlock_themestore_vip",false)) loadHooker(
            UnlockThemeStoreVip()
        )
    }
}