package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class Remove72HourPasswordVerification : YukiBaseHooker() {
    override fun onHook() {
        //Source LockSettingsStrongAuth -> StrongAuthTimeoutAlarmListener
        findClass("com.android.server.locksettings.LockSettingsStrongAuth\$StrongAuthTimeoutAlarmListener").hook {
            injectMember {
                method {
                    name = "onAlarm"
                }
                intercept()
            }
        }
    }
}