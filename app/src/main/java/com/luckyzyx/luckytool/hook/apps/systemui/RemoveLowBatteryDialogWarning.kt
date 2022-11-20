package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveLowBatteryDialogWarning : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusPowerNotificationWarnings
        findClass("com.oplusos.systemui.notification.power.OplusPowerNotificationWarnings").hook {
            injectMember {
                method {
                    name = "createSavePowerDialog"
                }
                intercept()
            }
            injectMember {
                method {
                    name = "createSuperSavePowerDialog"
                }
                intercept()
            }
        }
    }
}