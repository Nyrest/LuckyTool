package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveStatusBarDevMode : YukiBaseHooker() {
    override fun onHook() {
        VariousClass(
            "com.oplusos.systemui.statusbar.policy.SystemPromptController",
            "com.coloros.systemui.statusbar.policy.ColorSystemPromptController"
        ).hook {
            injectMember {
                method {
                    name = "updateDeveloperMode"
                }
                beforeHook {
                    resultNull()
                }
            }
        }
    }
}