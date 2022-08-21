package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.StatusBarTilesColumn
import com.luckyzyx.luckytool.utils.XposedPrefs

class StatusBarTiles : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //状态栏磁贴列数
            if (prefs(XposedPrefs).getBoolean("statusbar_tile_enable", false)) loadHooker(StatusBarTilesColumn())

        }
    }
}