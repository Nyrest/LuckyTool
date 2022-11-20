package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class AppSplashScreen : YukiBaseHooker() {
    override fun onHook() {
        //Source StartingSurfaceController
        findClass("com.android.server.wm.StartingSurfaceController").hook {
            injectMember {
                method {
                    name = "showStartingWindow"
                    paramCount = 5
                }
                if (prefs(XposedPrefs).getBoolean("disable_splash_screen", false)) intercept()
            }
        }
    }
}