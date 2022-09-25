package com.luckyzyx.luckytool.hook.apps.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveDropdownStatusbarMydevice : YukiBaseHooker() {
    override fun onHook() {
        // Source MyDevicePanel
        findClass("com.oplus.systemui.qs.mydevice.MyDevicePanel").hook {
            injectMember {
                method {
                    name = "onFinishInflate"
                }
                afterHook {
                    (field {
                        name = "mDeviceChildContainer"
                    }.get(instance).cast<View>())?.isVisible = false
                    (instance as View).setOnClickListener(null)
                    (instance as View).setOnLongClickListener(null)
                }
            }
        }
    }
}