package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveStatusbarDateComma
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class StatusBarDate : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui"){
            //移除下拉状态栏日期逗号
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_date_comma",false)) loadHooker(
                RemoveStatusbarDateComma()
            )
        }
    }
}