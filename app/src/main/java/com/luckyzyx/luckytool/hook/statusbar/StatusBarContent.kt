package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveDropdownStatusbarMydevice
import com.luckyzyx.luckytool.hook.apps.systemui.StatusBarTilesColumn
import com.luckyzyx.luckytool.hook.apps.systemui.StatusBarTilesColumnV13
import com.luckyzyx.luckytool.utils.tools.A13
import com.luckyzyx.luckytool.utils.tools.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class StatusBarContent : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //状态栏磁贴列数
            if (prefs(XposedPrefs).getBoolean("statusbar_tile_enable", false)) {
                if (SDK >= A13) loadHooker(StatusBarTilesColumnV13()) else loadHooker(StatusBarTilesColumn())
            }

            //移除下拉状态栏我的设备
            if (prefs(XposedPrefs).getBoolean("remove_drop_down_statusbar_mydevice", false)) {
                if (SDK >= A13) loadHooker(RemoveDropdownStatusbarMydevice())
            }
        }
    }
}