package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.heytapcloud.RemoveNetworkRestriction

class HookCloud : YukiBaseHooker() {
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        //移除网络限制
        if (prefs(prefsFile).getBoolean("remove_network_restriction",false)) loadHooker(RemoveNetworkRestriction())
    }
}