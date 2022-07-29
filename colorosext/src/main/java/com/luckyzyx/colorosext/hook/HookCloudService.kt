package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.heytapcloud.RemoveNetworkRestriction
import com.luckyzyx.colorosext.utils.XposedPrefs

class HookCloudService : YukiBaseHooker() {
    override fun onHook() {
        //移除网络限制
        if (prefs(XposedPrefs).getBoolean("remove_network_limit",false)) loadHooker(RemoveNetworkRestriction())
    }
}