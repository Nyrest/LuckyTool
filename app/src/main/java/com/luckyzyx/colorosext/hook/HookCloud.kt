package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.tools.hook.heytapcloud.RemoveNetworkRestriction

class HookCloud : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //移除网络限制
        if (prefs(PrefsFile).getBoolean("remove_network_restriction",false)) loadHooker(RemoveNetworkRestriction())
    }
}