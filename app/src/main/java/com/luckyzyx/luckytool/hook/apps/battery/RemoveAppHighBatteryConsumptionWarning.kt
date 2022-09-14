package com.luckyzyx.luckytool.hook.apps.battery

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveAppHighBatteryConsumptionWarning : YukiBaseHooker() {
    override fun onHook() {
        // Source NotifyUtil
        // Search power_consumption_optimization_title / String \n String
        VariousClass(
            "c4.b"
        ).hook {
            injectMember {
                method {
                    paramCount = 2
                    param(String,Boolean)
                }
                replaceTo(null)
            }
        }
    }
}