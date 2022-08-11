package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.luckyzyx.luckytool.utils.XposedPrefs

class RemoveVPNActiveNotification : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.server.connectivity.OplusVpnHelper").hook {
            injectMember {
                method {
                    name = "showNotification"
                }
                if (prefs(XposedPrefs).getBoolean("remove_vpn_active_notification", false)) replaceUnit { }
            }
        }.onHookClassNotFoundFailure {
            loggerD(msg = "ClassNotFound->OplusVpnHelper")
        }
    }
}