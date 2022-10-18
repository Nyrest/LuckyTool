package com.luckyzyx.luckytool.hook.apps.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveMobileDataInout : YukiBaseHooker() {
    override fun onHook() {
        VariousClass(
            "com.oplusos.systemui.statusbar.OplusStatusBarMobileView",
            "com.oplus.systemui.statusbar.phone.signal.OplusStatusBarMobileViewExImpl"
        ).hook {
            injectMember {
                method {
                    name = "initViewState"
                }
                afterHook {
                    field {
                        name = "mDataActivity"
                    }.get(instance).cast<View>()?.isVisible = false
                }
            }
            injectMember {
                method {
                    name = when(instanceClass.simpleName){
                        "OplusStatusBarMobileView" -> "updateMobileViewState"
                        "OplusStatusBarMobileViewExImpl" -> "updateState"
                        else -> "updateState"
                    }
                }
                afterHook {
                    field {
                        name = "mDataActivity"
                    }.get(instance).cast<View>()?.isVisible = false
                }
            }
        }
    }
}