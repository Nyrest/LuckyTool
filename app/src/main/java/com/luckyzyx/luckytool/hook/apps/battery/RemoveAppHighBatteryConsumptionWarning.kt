package com.luckyzyx.luckytool.hook.apps.battery

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveAppHighBatteryConsumptionWarning : YukiBaseHooker() {
    override fun onHook() {
        val method = arrayOf("H", "w", "x", "z")
        // Source NotifyUtil
        // Search power_consumption_optimization_title / pco_notification_text / String \n String
        VariousClass(
            "c4.b"
        ).hook {
            injectMember {
                method {
                    name = method[0]
                    paramCount = 2
                }
                replaceTo(null)
            }
            injectMember {
                method {
                    name = method[1]
                    paramCount = 2
                }
                replaceTo(null)
            }
            injectMember {
                method {
                    name = method[2]
                    paramCount = 2
                }
                replaceTo(null)
            }
            injectMember {
                method {
                    name = method[3]
                    paramCount = 2
                }
                replaceTo(null)
            }
        }
    }
}