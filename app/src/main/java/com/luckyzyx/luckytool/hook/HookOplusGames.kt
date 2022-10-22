package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.oplusgames.RemoveRootCheck
import com.luckyzyx.luckytool.hook.apps.oplusgames.RemoveStartupAnimation
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class HookOplusGames : YukiBaseHooker() {
    override fun onHook() {
        //游戏滤镜-->Root检测
        if (prefs(XposedPrefs).getBoolean("remove_root_check", false)) loadHooker(RemoveRootCheck())

        //简洁页面
        if (prefs(XposedPrefs).getBoolean("remove_startup_animation",false)) loadHooker(RemoveStartupAnimation())
    }
}