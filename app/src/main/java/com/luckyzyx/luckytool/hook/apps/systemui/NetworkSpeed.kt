package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.LongType

class NetworkSpeed : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplusos.systemui.statusbar.controller.NetworkSpeedController").hook {
            injectMember {
                method {
                    name = "postUpdateNetworkSpeedDelay"
                    param(LongType)
                    paramCount = 1
                }
                beforeHook {
                    args(0).set(1000L)
                }
            }
        }
    }
}
class NetworkSpeedV13 : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplus.systemui.statusbar.phone.netspeed.OplusNetworkSpeedControllExImpl").hook {
            injectMember {
                method {
                    name = "postUpdateNetworkSpeedDelay"
                    param(LongType)
                    paramCount = 1
                }
                beforeHook {
                    args(0).set(1000L)
                }
            }
        }
    }
}