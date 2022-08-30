package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.StatusBarTilesColumn
import com.luckyzyx.luckytool.hook.apps.systemui.StatusBarTilesColumnV13
import com.luckyzyx.luckytool.utils.tools.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class StatusBarTiles : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //状态栏磁贴列数
            if (prefs(XposedPrefs).getBoolean("statusbar_tile_enable", false)) {
                if (SDK >= 33) {
                    loadHooker(StatusBarTilesColumnV13())
                }else loadHooker(StatusBarTilesColumn())
            }
        }
    }
}