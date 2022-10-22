package com.luckyzyx.luckytool.hook.apps.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveStartupAnimation : YukiBaseHooker() {
    override fun onHook() {
        //Source GameOptimizedNewView
        //Search -> startAnimationIn -> Method
        findClass("business.secondarypanel.view.GameOptimizedNewView").hook {
            injectMember {
                method {
                    name = "c"
                }
                replaceTo(null)
            }
        }
    }
}
