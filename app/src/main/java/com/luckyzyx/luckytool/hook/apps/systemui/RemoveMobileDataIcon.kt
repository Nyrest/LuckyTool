package com.luckyzyx.luckytool.hook.apps.systemui

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class RemoveMobileDataIcon : YukiBaseHooker() {
    override fun onHook() {
        val removeIcon = prefs(XposedPrefs).getBoolean("remove_mobile_data_icon",false)
        val removeInout = prefs(XposedPrefs).getBoolean("remove_mobile_data_inout",false)
        //Source OplusStatusBarMobileViewExImpl
        VariousClass(
            "com.oplusos.systemui.statusbar.OplusStatusBarMobileView",
            "com.oplus.systemui.statusbar.phone.signal.OplusStatusBarMobileViewExImpl"
        ).hook {
            injectMember {
                method {
                    name = "initViewState"
                }
                afterHook {
                    if (removeIcon) field { name = "mMobileGroup" }.get(instance).cast<ViewGroup>()?.isVisible = false
                    if (removeInout) field { name = "mDataActivity" }.get(instance).cast<View>()?.isVisible = false
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
                    if (removeIcon) field { name = "mMobileGroup" }.get(instance).cast<ViewGroup>()?.isVisible = false
                    if (removeInout) field { name = "mDataActivity" }.get(instance).cast<View>()?.isVisible = false
                }
            }
        }
    }
}