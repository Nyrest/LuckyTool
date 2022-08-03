package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.oplusgames.RemoveRootCheck
import com.luckyzyx.luckytool.utils.XposedPrefs

class HookOplusGames : YukiBaseHooker() {
    override fun onHook() {
        //游戏滤镜-->Root检测
        if (prefs(XposedPrefs).getBoolean("remove_root_check", false)) loadHooker(RemoveRootCheck())
    }
}