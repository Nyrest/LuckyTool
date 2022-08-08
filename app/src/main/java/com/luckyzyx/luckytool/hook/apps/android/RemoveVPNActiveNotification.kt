package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.XposedPrefs

class RemoveVPNActiveNotification : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.server.connectivity.IOplusVpnHelper").hook {
            injectMember {
                method {
                    name = "showNotification"
                }
                if (prefs(XposedPrefs).getBoolean("remove_vpn_active_notification", false)) replaceTo(null)
            }
        }
    }
}