package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class ShowChargingRipple : YukiBaseHooker() {
    override fun onHook() {
        resources().hook {
            injectResource {
                conditions {
                    name = "flag_charging_ripple"
                    bool()
                }
                replaceToTrue()
            }
        }
    }
}