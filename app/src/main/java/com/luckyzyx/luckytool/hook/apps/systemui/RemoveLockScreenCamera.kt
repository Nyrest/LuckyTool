package com.luckyzyx.luckytool.hook.apps.systemui

import android.view.View
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveLockScreenCamera : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.systemui.statusbar.phone.KeyguardBottomAreaView").hook {
            injectMember {
                method {
                    name = "updateCameraVisibility"
                }
                afterHook {
                    (field {
                        name = "mRightAffordanceView"
                        superClass(isOnlySuperClass = true)
                    }.get(instance).self as View).visibility = View.GONE
                }
            }
        }
    }
}