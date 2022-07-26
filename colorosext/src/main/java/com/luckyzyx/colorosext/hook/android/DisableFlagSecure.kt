package com.luckyzyx.colorosext.hook.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class DisableFlagSecure : YukiBaseHooker() {
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        findClass("com.android.server.wm.WindowState").hook {
            injectMember {
                method {
                    name = "isSecureLocked"
                    returnType = BooleanType
                }
                beforeHook {
                    if (prefs(prefsFile).getBoolean("disable_flag_secure", false)) resultFalse()
                }
            }
        }.onHookClassNotFoundFailure {
            loggerE(msg = "ClassNotFound->WindowState")
        }
    }
}