package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class RemoveStatusBarUserSwitcher : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplusos.systemui.qs.OplusQSFooterImpl").hook {
            injectMember {
                method {
                    name = "showUserSwitcher"
                    returnType = BooleanType
                }
                replaceToFalse()
            }
        }
    }
}