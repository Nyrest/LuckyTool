package com.luckyzyx.tools.hook.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class DisableFlagSecure : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        findClass("com.android.server.wm.WindowState").hook {
            injectMember {
                method {
                    name = "isSecureLocked"
                    returnType = BooleanType
                }
                beforeHook {
                    if (prefs(PrefsFile).getBoolean("disable_flag_secure", false)) resultFalse()
                }
            }
        }.onHookClassNotFoundFailure {
            loggerE(msg = "ClassNotFound->WindowState")
        }
    }
}