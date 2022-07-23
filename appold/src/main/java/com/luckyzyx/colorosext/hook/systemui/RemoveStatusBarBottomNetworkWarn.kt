package com.luckyzyx.tools.hook.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveStatusBarBottomNetworkWarn : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplusos.systemui.qs.widget.OplusQSSecurityText").hook {
            injectMember {
                method {
                    name = "handleRefreshState"
                }
                beforeHook {
                    resultNull()
                }
            }
        }
    }
}