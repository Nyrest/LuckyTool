package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class NetworkSpeed : YukiBaseHooker() {
    override fun onHook() {
        //Search postUpdateNetworkSpeedDelay
        VariousClass(
            "com.oplusos.systemui.statusbar.controller.NetworkSpeedController",
            "com.oplus.systemui.statusbar.phone.netspeed.OplusNetworkSpeedControllExImpl"
        ).hook {
            injectMember {
                method {
                    name = "postUpdateNetworkSpeedDelay"
                    paramCount = 1
                }
                beforeHook {
                    args(0).set(1000L)
                }
            }
        }
    }
}