package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerE
import com.luckyzyx.luckytool.utils.XposedPrefs

class DisableFlagSecure : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.server.wm.WindowState").hook {
            injectMember {
                method {
                    name = "isSecureLocked"
                }
                beforeHook {
                    prefs.clearCache()
                    if (prefs(XposedPrefs).getBoolean("disable_flag_secure",false)) resultFalse()
                }
            }.onNoSuchMemberFailure {
                loggerE(msg = "NoSuchMember -> isSecureLocked")
            }
        }.onHookClassNotFoundFailure {
            loggerE(msg = "ClassNotFound -> WindowState")
        }
    }
}