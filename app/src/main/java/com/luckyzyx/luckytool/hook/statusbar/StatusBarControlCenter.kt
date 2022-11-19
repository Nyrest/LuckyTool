package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.*
import com.luckyzyx.luckytool.utils.tools.A13
import com.luckyzyx.luckytool.utils.tools.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class StatusBarControlCenter : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //下拉状态栏时钟显秒
            if (prefs(XposedPrefs).getBoolean("dropdown_statusbar_clock_show_second", false)) {
                loadHooker(DropDownStatusBarClock())
            }
            //下拉状态栏时钟样式  冒号 红一
            if (prefs(XposedPrefs).getBoolean("remove_dropdown_statusbar_clock_style", false)) {
                loadHooker(RemoveStatusBarClockRedOne())
            }
            //移除下拉状态栏日期逗号
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_date_comma", false)) loadHooker(
                RemoveStatusbarDateComma()
            )
            //移除下拉状态栏多用户图标
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_user_switcher", false)) {
                if (SDK < A13) loadHooker(RemoveStatusBarUserSwitcher())
            }
            //移除下拉状态栏我的设备
            if (prefs(XposedPrefs).getBoolean("remove_drop_down_statusbar_mydevice", false)) {
                if (SDK >= A13) loadHooker(RemoveDropdownStatusbarMydevice())
            }
            //状态栏磁贴列数
            if (prefs(XposedPrefs).getBoolean("statusbar_tile_enable", false)) {
                if (SDK >= A13) loadHooker(StatusBarTilesColumnV13()) else loadHooker(
                    StatusBarTilesColumn()
                )
            }
        }
    }
}