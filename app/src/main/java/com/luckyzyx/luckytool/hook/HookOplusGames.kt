package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.oplusgames.EnableDeveloperPage
import com.luckyzyx.luckytool.hook.apps.oplusgames.EnableEVATheme
import com.luckyzyx.luckytool.hook.apps.oplusgames.RemoveRootCheck
import com.luckyzyx.luckytool.hook.apps.oplusgames.RemoveStartupAnimation
import com.luckyzyx.luckytool.utils.tools.XposedPrefs


class HookOplusGames : YukiBaseHooker() {
    override fun onHook() {
        //游戏滤镜-->Root检测
        if (prefs(XposedPrefs).getBoolean("remove_root_check", false)) loadHooker(RemoveRootCheck())

        //简洁页面
        if (prefs(XposedPrefs).getBoolean("remove_startup_animation",false)) loadHooker(RemoveStartupAnimation())

        //启用开发者选项
        if (prefs(XposedPrefs).getBoolean("enable_developer_page",false)) loadHooker(EnableDeveloperPage())

        //启用EVA主题
        if (prefs(XposedPrefs).getBoolean("enable_eva_theme",false)) loadHooker(EnableEVATheme())
    }
}