package com.luckyzyx.luckytool.hook.apps.wirelesssettings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class RemovePersonalHotspotWarning : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplus.wirelesssettings.wifi.tether.WifiApOverworkNotificationReceiver").hook {
            injectMember {
                method {
                    paramCount = 1
                    returnType = BooleanType
                }
                replaceToFalse()
            }
        }
    }
}