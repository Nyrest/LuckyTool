package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class RemoveStatusBarUserSwitcher : YukiBaseHooker() {
    override fun onHook() {
        //Search Log showUserSwitcher
        findClass("com.oplusos.systemui.qs.OplusQSFooterImpl").hook {
            injectMember {
                method {
                    name = "showUserSwitcher"
                    emptyParam()
                    returnType = BooleanType
                }
                replaceToFalse()
            }
        }
    }
}
class RemoveStatusBarUserSwitcherV13 : YukiBaseHooker() {
    override fun onHook() {
        //Search Log showUserSwitcher
        findClass("com.oplusos.systemui.qs.OplusQSFooterViewController").hook {
            injectMember {
                method {
                    name = "showUserSwitcher"
                    emptyParam()
                    returnType = BooleanType
                }
                replaceToFalse()
            }
        }
    }
}