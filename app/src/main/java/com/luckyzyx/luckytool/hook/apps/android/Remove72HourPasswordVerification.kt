package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class Remove72HourPasswordVerification : YukiBaseHooker() {
    override fun onHook() {
        //Source LockSettingsStrongAuth
        findClass("com.android.server.locksettings.LockSettingsStrongAuth").hook {
            injectMember {
                method {
                    name = "rescheduleStrongAuthTimeoutAlarm"
                }
                beforeHook {
                    args(0).set(0L)
                }
            }
        }
    }
}