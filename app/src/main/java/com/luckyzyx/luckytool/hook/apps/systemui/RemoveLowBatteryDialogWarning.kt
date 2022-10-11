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
                replaceTo(null)
            }
            injectMember {
                method {
                    name = "createSuperSavePowerDialog"
                }
                replaceTo(null)
            }
        }
    }
}