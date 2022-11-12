package com.luckyzyx.luckytool.hook.apps.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class DisableDuplicateFloatingWindow : YukiBaseHooker() {
    override fun onHook() {
        //Source ClipboardOverlayController
        findClass("com.android.systemui.clipboardoverlay.ClipboardOverlayController").hook {
            injectMember {
                method {
                    name = "showEditableText"
                    paramCount = 2
                }
                afterHook {
                    (field {
                        name = "mClipboardPreview"
                    }.get(instance).cast<View>()?.parent as View).isVisible = false
                }
            }
        }
    }
}