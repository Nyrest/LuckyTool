package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveStatusBarDevMode : YukiBaseHooker() {
    override fun onHook() {
        VariousClass(
            "com.coloros.systemui.statusbar.policy.ColorSystemPromptController",
            "com.oplusos.systemui.statusbar.policy.SystemPromptController"
        ).hook {
            injectMember {
                method {
                    name = "updateDeveloperMode"
                }
                replaceTo(null)
            }
        }
    }
}