package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class ShowChargingRipple : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.systemui.statusbar.FeatureFlags").hook {
            injectMember {
                method {
                    name = "isChargingRippleEnabled"
                }
                replaceToTrue()
            }
        }
    }
}