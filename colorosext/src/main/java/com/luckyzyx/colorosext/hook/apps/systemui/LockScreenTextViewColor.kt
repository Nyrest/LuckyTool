package com.luckyzyx.colorosext.hook.apps.systemui

import android.graphics.Color
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.colorosext.utils.XposedPrefs

class LockScreenTextViewColor : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplusos.systemui.keyguard.clock.RedHorizontalSingleClockView").hook {
            injectMember {
                method {
                    name = "setTextColor"
                    param(IntType)
                }
                beforeHook {
                    args(0).set(prefs(XposedPrefs).getInt("set_lock_screen_textview_color", Color.BLACK))
                }
            }
        }
    }
}