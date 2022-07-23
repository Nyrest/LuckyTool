package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.tools.hook.oplusgames.RemoveRootCheck

class HookOplusGames : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //游戏滤镜-->Root检测
        if (prefs(PrefsFile).getBoolean("remove_root_check", false)) loadHooker(RemoveRootCheck())
    }
}