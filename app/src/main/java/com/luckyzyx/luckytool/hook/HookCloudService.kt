package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.heytapcloud.RemoveNetworkRestriction
import com.luckyzyx.luckytool.utils.XposedPrefs

class HookCloudService : YukiBaseHooker() {
    override fun onHook() {
        //移除网络限制
        if (prefs(XposedPrefs).getBoolean("remove_network_limit",false)) loadHooker(RemoveNetworkRestriction())
    }
}