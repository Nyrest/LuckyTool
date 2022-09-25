package com.luckyzyx.luckytool.hook.apps.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class RemoveHighPerformanceModeNotifications : YukiBaseHooker() {
    override fun onHook() {
        //Source GTModeBroadcastReceiver
        //Search gt_mode_block_message_setting
        findClass("com.oplus.performance.GTModeBroadcastReceiver").hook {
            injectMember {
                method {
                    name = "f"
                    paramCount = 1
                    returnType = BooleanType
                }
                replaceToTrue()
            }
        }
    }
}