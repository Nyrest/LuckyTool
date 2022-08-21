package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerE
import com.luckyzyx.luckytool.utils.XposedPrefs

class ADBInstallConfirm : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.server.pm.OplusPackageInstallInterceptManager").hook {
            injectMember {
                method {
                    name = "allowInterceptAdbInstallInInstallStage"
                    paramCount = 5
                }
                beforeHook {
                    prefs.clearCache()
                    if (prefs(XposedPrefs).getBoolean("remove_adb_install_confirm",false)) resultFalse()
                }
            }.onNoSuchMemberFailure {
                loggerE(msg = "NoSuchMember -> allowInterceptAdbInstallInInstallStage")
            }
        }.onHookClassNotFoundFailure {
            loggerE(msg = "ClassNotFound -> OplusPackageInstallInterceptManager")
        }
    }
}