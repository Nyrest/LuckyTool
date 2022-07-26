package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.oplusgames.RemoveRootCheck

class HookOplusGames : YukiBaseHooker() {
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        //游戏滤镜-->Root检测
        if (prefs(prefsFile).getBoolean("remove_root_check", false)) loadHooker(RemoveRootCheck())
    }
}