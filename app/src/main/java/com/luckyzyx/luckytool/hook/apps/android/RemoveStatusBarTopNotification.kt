package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerE

class RemoveStatusBarTopNotification : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.server.wm.AlertWindowNotification").hook {
            injectMember {
                method {
                    name = "onPostNotification"
                }
                beforeHook {
                    resultNull()
                }
            }.onNoSuchMemberFailure {
                loggerE(msg = "NoSuchMember->onPostNotification")
            }
        }.onHookClassNotFoundFailure {
            loggerE(msg = "ClassNotFound->AlertWindowNotification")
        }
    }
}