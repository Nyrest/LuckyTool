package com.luckyzyx.colorosext.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.luckyzyx.colorosext.utils.XposedPrefs

class DisableFlagSecure : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.server.wm.WindowState").hook {
            injectMember {
                method {
                    name = "isSecureLocked"
                }
                if (prefs(XposedPrefs).getBoolean("disable_flag_secure",false)) replaceToFalse()
            }.onNoSuchMemberFailure {
                loggerD(msg = "NoSuchMember->isSecureLocked")
            }
        }.onHookClassNotFoundFailure {
            loggerD(msg = "ClassNotFound->WindowState")
        }
    }
}