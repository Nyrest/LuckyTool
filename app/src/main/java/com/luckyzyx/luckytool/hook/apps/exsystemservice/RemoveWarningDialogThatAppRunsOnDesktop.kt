package com.luckyzyx.luckytool.hook.apps.exsystemservice

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveWarningDialogThatAppRunsOnDesktop : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplus.exsystemservice.untrustedtouch.UnTrustedTouchOcclusionService").hook {
            injectMember {
                method {
                    name = "onStartCommand"
                }
                beforeHook {
                    args(0).setNull()
                }
            }
        }
    }
}